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

package com.azokle.weather.graphs.common

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.dp

fun DrawScope.drawPoint(
    center: Offset,
    args: GraphArgs
) {
    val haloRadius = args.pointCenterRadius * 2.5f
    val outlineRadius = args.pointCenterRadius + (args.pointOutlineWidth / 2)
    
    // Soft outer glow / halo
    drawCircle(
        color = args.pointCenterColor.copy(alpha = 0.18f),
        radius = haloRadius,
        center = center,
        style = Fill
    )
    // Outline ring
    drawCircle(
        color = args.pointOutlineColor,
        radius = outlineRadius,
        center = center,
        style = Stroke(width = args.pointOutlineWidth)
    )
    // Center dot
    drawCircle(
        color = args.pointCenterColor,
        radius = args.pointCenterRadius,
        center = center,
        style = Fill
    )
}

fun DrawScope.drawLabeledPoint(
    label: String,
    center: Offset,
    args: GraphArgs,
    measurer: TextMeasurer
) {
    drawPointLabel(
        label, measurer,
        pointCenter = center,
        args = args
    )
    drawPoint(
        center,
        args = args
    )
}

private fun DrawScope.drawPointLabel(
    text: String,
    measurer: TextMeasurer,
    pointCenter: Offset,
    args: GraphArgs,
) {
    val labelMeasured = measurer.measure(
        text = text,
        style = args.axisTextStyle.copy(
            color = args.pointLabelColor,
            fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold
        )
    )
    
    val hPadding = 6.dp.toPx()
    val vPadding = 2.dp.toPx()
    val pillWidth = labelMeasured.size.width + (hPadding * 2)
    val pillHeight = labelMeasured.size.height + (vPadding * 2)
    val cornerRadius = CornerRadius(pillHeight / 2, pillHeight / 2)
    
    val pillX = (pointCenter.x - (pillWidth / 2)).coerceIn(
        minimumValue = args.startGutter + args.textPaddingMinHorizontal,
        maximumValue = size.width - args.endGutter - pillWidth - args.textPaddingMinHorizontal
    )
    val pillY = (pointCenter.y - pillHeight - args.pointTextPaddingBottom).coerceAtLeast(args.topGutter + 2.dp.toPx())
    
    // Pill background
    drawRoundRect(
        color = args.pointOutlineColor,
        topLeft = Offset(pillX, pillY),
        size = Size(pillWidth, pillHeight),
        cornerRadius = cornerRadius,
        style = Fill
    )
    
    // Subtle pill border
    drawRoundRect(
        color = args.axisColor.copy(alpha = 0.25f),
        topLeft = Offset(pillX, pillY),
        size = Size(pillWidth, pillHeight),
        cornerRadius = cornerRadius,
        style = Stroke(width = 1.dp.toPx())
    )
    
    // Text inside pill
    drawText(
        textLayoutResult = labelMeasured,
        topLeft = Offset(
            x = pillX + hPadding,
            y = pillY + vPadding
        )
    )
}