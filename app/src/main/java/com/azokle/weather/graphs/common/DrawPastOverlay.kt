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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp

fun DrawScope.drawPastOverlay(
    nowX: Float,
    args: GraphArgs
) {
    // Translucent past area mask
    drawRect(
        color = args.pastOverlayColor,
        topLeft = Offset(args.startGutter, args.topGutter),
        size = Size(
            width = (nowX - args.startGutter).coerceAtLeast(0f),
            height = (size.height - args.topGutter - args.bottomGutter).coerceAtLeast(0f)
        )
    )
    // Present time vertical line with subtle glow
    drawLine(
        color = args.pointCenterColor.copy(alpha = 0.25f),
        start = Offset(x = nowX, y = args.topGutter),
        end = Offset(x = nowX, y = size.height - args.bottomGutter),
        strokeWidth = 3.dp.toPx()
    )
    drawLine(
        color = args.pointCenterColor.copy(alpha = 0.8f),
        start = Offset(x = nowX, y = args.topGutter),
        end = Offset(x = nowX, y = size.height - args.bottomGutter),
        strokeWidth = 1.dp.toPx()
    )
}

fun DrawScope.drawPastOverlayWithPoint(
    nowCenter: Offset,
    args: GraphArgs
) {
    drawPastOverlay(nowCenter.x, args)
    drawPoint(nowCenter, args)
}