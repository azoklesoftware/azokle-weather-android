/*
 * Copyright 2024 Azokle Private Limited
 *
 * This file is part of Azokle Weather.
 *
 * Azokle Weather is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Azokle Weather is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Azokle Weather. If not, see <https://www.gnu.org/licenses/>.
 */

package com.azokle.weather.graphs.pop

import android.content.Context
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.azokle.weather.common.AppTheme
import com.azokle.weather.graphs.common.GraphArgs
import com.azokle.weather.graphs.common.GraphTime
import com.azokle.weather.graphs.common.drawLabeledPoint
import com.azokle.weather.graphs.common.drawPastOverlayWithPoint
import com.azokle.weather.graphs.common.drawTimeAxis
import com.azokle.weather.graphs.common.drawVerticalAxis
import com.azokle.weather.pop.Pop
import com.azokle.weather.pop.string
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun PopGraph(state: PopGraph, args: GraphArgs, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val measurer = rememberTextMeasurer()
    val plotColor = AppTheme.colors.popColor
    Canvas(modifier) {
        drawVerticalAxis(
            context = context,
            measurer = measurer,
            args = args,
        )
        drawHorizontalAxisAndPlot(
            state = state,
            context = context,
            measurer = measurer,
            plotColor = plotColor,
            args = args,
        )
    }
}

private fun DrawScope.drawHorizontalAxisAndPlot(
    state: PopGraph,
    context: Context,
    measurer: TextMeasurer,
    plotColor: Color,
    args: GraphArgs,
) {
    val range = 100f
    var nowCenter: Offset? = null
    var maxCenter: Pair<Offset, Pop>? = null
    val pointOffsets = mutableListOf<Offset>()

    drawTimeAxis(
        measurer = measurer,
        args = args
    ) { i, x ->
        // Plot point calculation
        val point = state.points.getOrNull(i) ?: return@drawTimeAxis
        val pop = point.pop.value
        val minY = args.topGutter + args.axisWidth + (args.plotWidth / 2)
        val maxY = size.height - args.bottomGutter - args.axisWidth - (args.plotWidth / 2)
        val y = (((1 - (pop.value / range)) * (size.height - args.bottomGutter - args.topGutter)) + args.topGutter).toFloat().coerceIn(minY, maxY)
        val offset = Offset(x, y)
        pointOffsets.add(offset)

        // Max and now indicator are drawn after the plot so it's on top of it
        if (point.pop.meta == GraphPop.Meta.Maximum) maxCenter = offset to point.pop.value
        if (point.time.meta == GraphTime.Meta.Present) nowCenter = offset
    }

    if (pointOffsets.isNotEmpty()) {
        val plotPath = Path()
        val plotFillPath = Path()

        plotPath.moveTo(pointOffsets[0].x, pointOffsets[0].y)
        plotFillPath.moveTo(pointOffsets[0].x, pointOffsets[0].y)

        for (i in 0 until pointOffsets.size - 1) {
            val p0 = pointOffsets.getOrElse(i - 1) { pointOffsets[i] }
            val p1 = pointOffsets[i]
            val p2 = pointOffsets[i + 1]
            val p3 = pointOffsets.getOrElse(i + 2) { pointOffsets[i + 1] }

            val cp1x = p1.x + (p2.x - p0.x) / 6f
            val cp1y = p1.y + (p2.y - p0.y) / 6f
            val cp2x = p2.x - (p3.x - p1.x) / 6f
            val cp2y = p2.y - (p3.y - p1.y) / 6f

            plotPath.cubicTo(cp1x, cp1y, cp2x, cp2y, p2.x, p2.y)
            plotFillPath.cubicTo(cp1x, cp1y, cp2x, cp2y, p2.x, p2.y)
        }

        val plotBottom = size.height - args.bottomGutter
        val firstX = pointOffsets.first().x
        val lastX = pointOffsets.last().x

        plotFillPath.lineTo(x = lastX, y = plotBottom)
        plotFillPath.lineTo(x = firstX, y = plotBottom)
        plotFillPath.close()

        val gradientStart = args.topGutter
        val gradientEnd = size.height - args.bottomGutter

        // Draw the fill under the smooth curve
        drawPath(
            plotFillPath,
            brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                colors = listOf(
                    plotColor.copy(alpha = args.plotFillAlpha),
                    plotColor.copy(alpha = 0.08f)
                ),
                startY = gradientStart,
                endY = gradientEnd
            )
        )

        // Clip and draw the smooth curve line
        clipPath(
            path = Path().apply {
                addRect(
                    Rect(
                        offset = Offset(x = args.startGutter, y = args.topGutter),
                        size = Size(
                            width = lastX - args.startGutter,
                            height = size.height - args.topGutter - args.bottomGutter
                        )
                    )
                )
            }
        ) {
            drawPath(
                plotPath,
                color = plotColor,
                style = Stroke(
                    width = args.plotWidth,
                    join = StrokeJoin.Round,
                    cap = StrokeCap.Round
                )
            )
        }
    }

    maxCenter?.let { (offset, pop) ->
        drawLabeledPoint(
            label = pop.string(context, args.numberFormat),
            center = offset,
            args = args,
            measurer = measurer
        )
    }
    nowCenter?.let {
        drawPastOverlayWithPoint(it, args)
    }
}

private fun DrawScope.drawVerticalAxis(
    context: Context,
    measurer: TextMeasurer,
    args: GraphArgs
) {
    drawVerticalAxis(
        steps = 5,
        args = args
    ) { frac, endX, y ->
        val pop = Pop(frac * 100.0)
        val popMeasured = measurer.measure(
            text = pop.string(context, args.numberFormat),
            style = args.axisTextStyle
        )
        drawText(
            textLayoutResult = popMeasured,
            color = args.axisColor,
            topLeft = Offset(
                x = endX + args.endAxisTextPaddingStart,
                y = y - (popMeasured.size.height / 2)
            )
        )
    }
}

@Preview
@Composable
private fun PopGraphPreview() {
    AppTheme(darkTheme = false) {
        PopGraph(
            state = previewState, modifier = Modifier
                .height(300.dp)
                .width(400.dp)
                .background(MaterialTheme.colorScheme.background),
            args = GraphArgs.rememberPopArgs()
        )
    }
}

@Preview
@Composable
private fun PopGraphNowStartPreview() {
    AppTheme {
        PopGraph(
            state = previewState.copy(points = previewState.points.mapIndexed { idx, pt ->
                pt.copy(
                    time = GraphTime(
                        pt.time.value,
                        meta = if (idx == 0) GraphTime.Meta.Present else GraphTime.Meta.Future
                    )
                )
            }),
            args = GraphArgs.rememberPopArgs(),
            modifier = Modifier
                .width(400.dp)
                .height(300.dp)
                .background(MaterialTheme.colorScheme.background)
        )
    }
}

@Preview
@Composable
private fun ConditionGraphNowEndPreview() {
    AppTheme {
        PopGraph(
            state = previewState.copy(points = previewState.points.mapIndexed { idx, pt ->
                pt.copy(
                    time = GraphTime(
                        pt.time.value,
                        meta = if (idx == previewState.points.lastIndex) GraphTime.Meta.Present else GraphTime.Meta.Past
                    )
                )
            }),
            args = GraphArgs.rememberPopArgs(),
            modifier = Modifier
                .width(400.dp)
                .height(300.dp)
                .background(MaterialTheme.colorScheme.background),
        )
    }
}

private val previewState = PopGraph(
    day = LocalDate.parse("1970-01-01"),
    points = listOf(
        PopGraphPoint(
            time = GraphTime(
                value = LocalTime.parse("00:00"),
                meta = GraphTime.Meta.Past
            ),
            pop = GraphPop(
                Pop(0.0),
                meta = GraphPop.Meta.Regular
            ),
        ),
        PopGraphPoint(
            time = GraphTime(
                value = LocalTime.parse("01:00"),
                meta = GraphTime.Meta.Past
            ),
            pop = GraphPop(
                Pop(0.0),
                meta = GraphPop.Meta.Regular
            ),
        ),
        PopGraphPoint(
            time = GraphTime(
                value = LocalTime.parse("02:00"),
                meta = GraphTime.Meta.Past
            ),
            pop = GraphPop(
                Pop(0.0),
                meta = GraphPop.Meta.Regular
            ),
        ),
        PopGraphPoint(
            time = GraphTime(
                value = LocalTime.parse("03:00"),
                meta = GraphTime.Meta.Past
            ),
            pop = GraphPop(
                Pop(0.0),
                meta = GraphPop.Meta.Regular
            ),
        ),
        PopGraphPoint(
            time = GraphTime(
                value = LocalTime.parse("04:00"),
                meta = GraphTime.Meta.Past
            ),
            pop = GraphPop(
                Pop(0.0),
                meta = GraphPop.Meta.Regular
            ),
        ),
        PopGraphPoint(
            time = GraphTime(
                value = LocalTime.parse("05:00"),
                meta = GraphTime.Meta.Past
            ),
            pop = GraphPop(
                Pop(0.0),
                meta = GraphPop.Meta.Regular
            ),
        ),
        PopGraphPoint(
            time = GraphTime(
                value = LocalTime.parse("06:00"),
                meta = GraphTime.Meta.Past
            ),
            pop = GraphPop(
                Pop(5.0),
                meta = GraphPop.Meta.Regular
            ),
        ),
        PopGraphPoint(
            time = GraphTime(
                value = LocalTime.parse("07:00"),
                meta = GraphTime.Meta.Past
            ),
            pop = GraphPop(
                Pop(5.0),
                meta = GraphPop.Meta.Regular
            ),
        ),
        PopGraphPoint(
            time = GraphTime(
                value = LocalTime.parse("08:00"),
                meta = GraphTime.Meta.Present
            ),
            pop = GraphPop(
                Pop(5.0),
                meta = GraphPop.Meta.Regular
            ),
        ),
        PopGraphPoint(
            time = GraphTime(
                value = LocalTime.parse("09:00"),
                meta = GraphTime.Meta.Future
            ),
            pop = GraphPop(
                Pop(10.0),
                meta = GraphPop.Meta.Regular
            ),
        ),
        PopGraphPoint(
            time = GraphTime(
                value = LocalTime.parse("10:00"),
                meta = GraphTime.Meta.Future
            ),
            pop = GraphPop(
                Pop(12.0),
                meta = GraphPop.Meta.Regular
            ),
        ),
        PopGraphPoint(
            time = GraphTime(
                value = LocalTime.parse("11:00"),
                meta = GraphTime.Meta.Future
            ),
            pop = GraphPop(
                Pop(12.0),
                meta = GraphPop.Meta.Regular
            ),
        ),
        PopGraphPoint(
            time = GraphTime(
                value = LocalTime.parse("12:00"),
                meta = GraphTime.Meta.Future
            ),
            pop = GraphPop(
                Pop(0.0),
                meta = GraphPop.Meta.Regular
            ),
        ),
        PopGraphPoint(
            time = GraphTime(
                value = LocalTime.parse("13:00"),
                meta = GraphTime.Meta.Future
            ),
            pop = GraphPop(
                Pop(0.0),
                meta = GraphPop.Meta.Regular
            ),
        ),
        PopGraphPoint(
            time = GraphTime(
                value = LocalTime.parse("14:00"),
                meta = GraphTime.Meta.Future
            ),
            pop = GraphPop(
                Pop(0.0),
                meta = GraphPop.Meta.Regular
            ),
        ),
        PopGraphPoint(
            time = GraphTime(
                value = LocalTime.parse("15:00"),
                meta = GraphTime.Meta.Future
            ),
            pop = GraphPop(
                Pop(50.0),
                meta = GraphPop.Meta.Regular
            ),
        ),
        PopGraphPoint(
            time = GraphTime(
                value = LocalTime.parse("16:00"),
                meta = GraphTime.Meta.Future
            ),
            pop = GraphPop(
                Pop(70.0),
                meta = GraphPop.Meta.Regular
            ),
        ),
        PopGraphPoint(
            time = GraphTime(
                value = LocalTime.parse("17:00"),
                meta = GraphTime.Meta.Future
            ),
            pop = GraphPop(
                Pop(100.0),
                meta = GraphPop.Meta.Maximum
            ),
        ),
        PopGraphPoint(
            time = GraphTime(
                value = LocalTime.parse("18:00"),
                meta = GraphTime.Meta.Future
            ),
            pop = GraphPop(
                Pop(100.0),
                meta = GraphPop.Meta.Regular
            ),
        ),
        PopGraphPoint(
            time = GraphTime(
                value = LocalTime.parse("19:00"),
                meta = GraphTime.Meta.Future
            ),
            pop = GraphPop(
                Pop(100.0),
                meta = GraphPop.Meta.Regular
            ),
        ),
        PopGraphPoint(
            time = GraphTime(
                value = LocalTime.parse("20:00"),
                meta = GraphTime.Meta.Future
            ),
            pop = GraphPop(
                Pop(100.0),
                meta = GraphPop.Meta.Regular
            ),
        ),
        PopGraphPoint(
            time = GraphTime(
                value = LocalTime.parse("21:00"),
                meta = GraphTime.Meta.Future
            ),
            pop = GraphPop(
                Pop(90.0),
                meta = GraphPop.Meta.Regular
            ),
        ),
        PopGraphPoint(
            time = GraphTime(
                value = LocalTime.parse("22:00"),
                meta = GraphTime.Meta.Future
            ),
            pop = GraphPop(
                Pop(90.0),
                meta = GraphPop.Meta.Regular
            ),
        ),
        PopGraphPoint(
            time = GraphTime(
                value = LocalTime.parse("23:00"),
                meta = GraphTime.Meta.Future
            ),
            pop = GraphPop(
                Pop(90.0),
                meta = GraphPop.Meta.Regular
            ),
        ),
        PopGraphPoint(
            time = GraphTime(
                value = LocalTime.parse("00:00"),
                meta = GraphTime.Meta.Future
            ),
            pop = GraphPop(
                Pop(90.0),
                meta = GraphPop.Meta.Regular
            ),
        ),
    )
)