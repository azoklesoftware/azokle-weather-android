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

package com.azokle.weather.summary.uvindex

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.azokle.weather.common.AppTheme
import com.azokle.weather.uvindex.UvIndex

@Composable
fun AppleUvIndexScale(uvIndexNow: UvIndex, modifier: Modifier = Modifier) {
    val nowColor = MaterialTheme.colorScheme.onSurface
    val nowOutlineColor = MaterialTheme.colorScheme.surfaceVariant
    val nowOutlineThickness = with(LocalDensity.current) { 4.dp.toPx() }
    Canvas(
        modifier = Modifier
            .height(6.dp)
            .then(modifier)
            .clip(RoundedCornerShape(percent = 100))
            .background(Brush.horizontalGradient(colorStops = AppTheme.colors.uvIndexColorStops.toTypedArray()))
    ) {
        val nowRadius = size.height / 2
        val nowStart = ((uvIndexNow.value / 11.0).coerceIn(0.0, 1.0) * size.width).toFloat()
        val nowEdgePadding = nowRadius
        val nowCenter = Offset(
            x = (nowStart - nowRadius).coerceIn(
                minimumValue = nowEdgePadding,
                maximumValue = size.width - nowEdgePadding
            ),
            y = nowRadius
        )
        drawCircle(
            color = nowOutlineColor,
            radius = nowRadius,
            center = nowCenter,
            style = Stroke(width = nowOutlineThickness)
        )
        drawCircle(
            color = nowColor,
            radius = nowRadius,
            center = nowCenter
        )
    }
}