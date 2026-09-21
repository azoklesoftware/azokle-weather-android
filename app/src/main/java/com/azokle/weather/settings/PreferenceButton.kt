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

package com.azokle.weather.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.azokle.weather.common.AppTheme
import com.azokle.weather.common.ClayDefaults
import com.azokle.weather.common.TextSkeleton
import com.azokle.weather.common.clayClickable
import com.azokle.weather.common.clayContainer

@Composable
fun PreferenceButton(
    title: String,
    value: String,
    onClick: () -> Unit
) {
    PreferenceButton(
        title = {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        value = {
            Text(
                text = value,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        onClick = onClick
    )
}

@Composable
fun PreferenceButtonSkeleton(color: State<Color>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clayContainer(
                surfaceColor = color.value,
                shape = ClayDefaults.ShapeMedium,
                elevation = ClayDefaults.ElevationLow,
                highlightColor = AppTheme.colors.clayHighlight,
                innerShadowColor = AppTheme.colors.clayInnerShadow
            )
            .padding(vertical = 16.dp, horizontal = 16.dp)
    ) {
        Column {
            TextSkeleton(
                color = color,
                shape = ClayDefaults.ShapeSmall,
                contentPadding = PaddingValues(vertical = 2.dp),
                modifier = Modifier.width(140.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            TextSkeleton(
                color = color,
                shape = ClayDefaults.ShapeSmall,
                contentPadding = PaddingValues(vertical = 2.dp),
                modifier = Modifier.width(100.dp)
            )
        }
    }
}

@Composable
private fun PreferenceButton(
    title: @Composable () -> Unit,
    value: @Composable () -> Unit,
    onClick: (() -> Unit)?
) {
    val clickModifier = if (onClick != null) {
        Modifier.clayClickable(onClick = onClick)
    } else Modifier

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .then(clickModifier)
            .clayContainer(
                surfaceColor = AppTheme.colors.claySurface,
                shape = ClayDefaults.ShapeMedium,
                elevation = ClayDefaults.ElevationLow,
                highlightColor = AppTheme.colors.clayHighlight,
                innerShadowColor = AppTheme.colors.clayInnerShadow
            )
            .padding(vertical = 14.dp, horizontal = 18.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.weight(1f)) {
                CompositionLocalProvider(
                    LocalTextStyle provides MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    LocalContentColor provides MaterialTheme.colorScheme.onSurface,
                    content = title
                )
                Spacer(modifier = Modifier.height(2.dp))
                CompositionLocalProvider(
                    LocalTextStyle provides MaterialTheme.typography.bodyMedium,
                    LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant,
                    content = value
                )
            }
        }
    }
}