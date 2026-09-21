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

package com.azokle.weather.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Standard Claymorphism styling specifications.
 */
object ClayDefaults {
    val CornerRadiusHero: Dp = 32.dp
    val CornerRadiusLarge: Dp = 26.dp
    val CornerRadiusMedium: Dp = 20.dp
    val CornerRadiusSmall: Dp = 14.dp
    val CornerRadiusPill: Dp = 999.dp

    val ShapeHero = RoundedCornerShape(CornerRadiusHero)
    val ShapeLarge = RoundedCornerShape(CornerRadiusLarge)
    val ShapeMedium = RoundedCornerShape(CornerRadiusMedium)
    val ShapeSmall = RoundedCornerShape(CornerRadiusSmall)
    val ShapePill = RoundedCornerShape(CornerRadiusPill)

    val ElevationHigh: Dp = 8.dp
    val ElevationMedium: Dp = 4.dp
    val ElevationLow: Dp = 2.dp
}

/**
 * High-performance Claymorphic container effect with hardware-accelerated drawing and cached styles.
 */
fun Modifier.clayContainer(
    surfaceColor: Color,
    shape: RoundedCornerShape = ClayDefaults.ShapeLarge,
    elevation: Dp = ClayDefaults.ElevationMedium,
    highlightColor: Color = Color.White.copy(alpha = 0.55f),
    innerShadowColor: Color = Color.Black.copy(alpha = 0.12f),
    strokeWidth: Dp = 1.5.dp,
    clipContent: Boolean = true
): Modifier = this
    .drawWithCache {
        val cornerRadiusPx = shape.topStart.toPx(size, this)
        val cr = CornerRadius(cornerRadiusPx, cornerRadiusPx)
        val strokePx = strokeWidth.toPx()
        val borderBrush = Brush.linearGradient(
            colors = listOf(
                highlightColor,
                highlightColor.copy(alpha = 0.15f),
                innerShadowColor.copy(alpha = 0.05f),
                innerShadowColor
            ),
            start = Offset(0f, 0f),
            end = Offset(size.width, size.height)
        )
        val strokeStyle = Stroke(width = strokePx)

        onDrawBehind {
            // Draw solid clay base
            drawRoundRect(
                color = surfaceColor,
                size = size,
                cornerRadius = cr
            )

            // Draw top-left highlight and bottom-right depth contour
            drawRoundRect(
                brush = borderBrush,
                size = size,
                cornerRadius = cr,
                style = strokeStyle
            )
        }
    }
    .then(if (clipContent) Modifier.clip(shape) else Modifier)

/**
 * High-performance clickable modifier for tactile elements.
 */
fun Modifier.clayClickable(
    enabled: Boolean = true,
    onClickLabel: String? = null,
    onClick: () -> Unit
): Modifier = composed {
    val interactionSource = remember { MutableInteractionSource() }
    this.clickable(
        interactionSource = interactionSource,
        indication = null,
        enabled = enabled,
        onClickLabel = onClickLabel,
        onClick = onClick
    )
}
