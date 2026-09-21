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

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope

fun DrawScope.drawVerticalAxis(
    steps: Int,
    args: GraphArgs,
    onStepDrawn: (frac: Float, endX: Float, y: Float) -> Unit
) {
    val x = size.width - args.endGutter
    for (i in 0..steps) {
        val frac = i / steps.toFloat()
        val y = size.height - args.bottomGutter - ((size.height - args.topGutter - args.bottomGutter) * frac)
        drawLine(
            color = args.axisColor.copy(alpha = 0.20f),
            start = Offset(args.startGutter, y),
            end = Offset(x, y),
            strokeWidth = args.axisWidth
        )
        onStepDrawn(frac, x, y)
    }
}