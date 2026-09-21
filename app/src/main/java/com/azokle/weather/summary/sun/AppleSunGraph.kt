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

package com.azokle.weather.summary.sun

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.azokle.weather.common.AppTheme
import java.time.LocalTime
import kotlin.math.PI
import kotlin.math.sin

import androidx.compose.ui.draw.drawWithCache

@Composable
fun AppleSunGraph(
    now: LocalTime,
    sunrise: LocalTime,
    sunset: LocalTime,
    modifier: Modifier = Modifier
) {
    val dayColor = AppTheme.colors.accentWarm
    val nightColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.35f)
    val horizonColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f)
    val sunColor = Color(0xFFFFB300)

    Canvas(
        modifier = modifier.drawWithCache {
            val width = size.width
            val height = size.height
            if (width <= 0f || height <= 0f) {
                return@drawWithCache onDrawBehind {}
            }

            fun calcX(time: LocalTime): Float =
                ((time.toSecondOfDay() / 60f) / 1440f) * width

            fun calcAngle(x: Float): Float =
                ((x / width) * 2 * PI + (PI / 2)).toFloat()

            fun calcY(angle: Float): Float =
                sin(angle) * (height / 2.2f) + height / 2f

            val sunriseX = calcX(sunrise)
            val sunsetX = calcX(sunset)
            val sunriseY = calcY(calcAngle(sunriseX))

            val beforeDay = Path()
            val day = Path()
            val afterDay = Path()

            val step = 6f
            var x = 0f
            while (x <= width) {
                val angle = calcAngle(x)
                val y = calcY(angle)
                val path = if (y > sunriseY) {
                    if (x <= sunriseX) beforeDay else afterDay
                } else {
                    day
                }
                path.run {
                    if (isEmpty) moveTo(x, y) else lineTo(x, y)
                }
                x += step
            }

            val horizonPathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f)
            val beforeDayStroke = Stroke(width = 2.5f)
            val dayStroke = Stroke(width = 3f)

            onDrawBehind {
                // Draw horizon line
                drawLine(
                    color = horizonColor,
                    start = Offset(x = 0f, y = sunriseY),
                    end = Offset(x = width, y = sunriseY),
                    strokeWidth = 1.5f,
                    pathEffect = horizonPathEffect
                )

                // Draw trajectory paths
                drawPath(beforeDay, color = nightColor, style = beforeDayStroke)
                drawPath(day, color = dayColor, style = dayStroke)
                drawPath(afterDay, color = nightColor, style = beforeDayStroke)

                // Draw current sun position
                val nowX = calcX(now)
                val nowAngle = calcAngle(nowX)
                val nowY = calcY(nowAngle)

                // Outer glow aura
                drawCircle(
                    color = sunColor.copy(alpha = 0.25f),
                    radius = 10f,
                    center = Offset(nowX, nowY)
                )
                // Main sun orb
                drawCircle(
                    color = sunColor,
                    radius = 5f,
                    center = Offset(nowX, nowY)
                )
            }
        }
    ) { }
}

@Preview
@Composable
private fun AppleSunGraphPreview() {
    MaterialTheme {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .width(200.dp)
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AppleSunGraph(
                now = LocalTime.of(12, 30),
                sunrise = LocalTime.of(6, 0),
                sunset = LocalTime.of(18, 30),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            )
        }
    }
}