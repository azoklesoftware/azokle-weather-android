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

package com.azokle.weather.summary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.azokle.weather.common.AppTheme
import com.azokle.weather.common.ClayDefaults
import com.azokle.weather.common.clayClickable
import com.azokle.weather.common.clayContainer

@Composable
fun SummaryTile(
    label: @Composable () -> Unit,
    value: @Composable () -> Unit,
    bottom: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    supportingValue: (@Composable () -> Unit)? = null,
) {
    BoxWithConstraints(modifier) {
        val minHeight = minWidth.coerceAtLeast(140.dp)
        val clickModifier = if (onClick != null) {
            Modifier.clayClickable(onClick = onClick)
        } else {
            Modifier
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = minHeight)
                .then(clickModifier)
                .clayContainer(
                    surfaceColor = AppTheme.colors.claySurface,
                    shape = ClayDefaults.ShapeMedium,
                    elevation = ClayDefaults.ElevationMedium,
                    highlightColor = AppTheme.colors.clayHighlight,
                    innerShadowColor = AppTheme.colors.clayInnerShadow
                )
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                Column {
                    CompositionLocalProvider(
                        LocalTextStyle provides MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.3.sp
                        ),
                        LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.85f),
                        content = label
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    CompositionLocalProvider(
                        LocalTextStyle provides MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        LocalContentColor provides MaterialTheme.colorScheme.onSurface,
                        content = value
                    )
                    supportingValue?.let {
                        Spacer(modifier = Modifier.height(2.dp))
                        CompositionLocalProvider(
                            LocalTextStyle provides MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant,
                            content = it
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                CompositionLocalProvider(
                    LocalTextStyle provides MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Normal
                    ),
                    LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.9f),
                    content = bottom
                )
            }
        }
    }
}