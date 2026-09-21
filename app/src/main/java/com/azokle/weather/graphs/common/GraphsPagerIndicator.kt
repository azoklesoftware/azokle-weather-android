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

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.azokle.weather.R
import com.azokle.weather.common.AppTheme
import com.azokle.weather.common.ClayDefaults
import com.azokle.weather.common.capitalize
import com.azokle.weather.common.clayClickable
import com.azokle.weather.common.clayContainer
import com.azokle.weather.common.rememberAppLocale
import com.azokle.weather.common.rememberDateTimeFormatter
import java.time.LocalDate

@Composable
fun GraphsPagerIndicator(
    state: List<LocalDate>,
    selected: Int,
    onClick: (date: LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val formatter = rememberDateTimeFormatter(ofPattern = R.string.date_time_pattern_dow)
    val scrollState = rememberScrollState()

    LaunchedEffect(selected) {
        if (state.isNotEmpty() && selected in state.indices) {
            val approxItemWidthPx = 72 * 3 // approx density dp to px
            val targetScroll = (selected * approxItemWidthPx) - (approxItemWidthPx * 1.5).toInt()
            scrollState.animateScrollTo(targetScroll.coerceAtLeast(0))
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        state.forEachIndexed { idx, date ->
            val isSelected = idx == selected
            val dayOfWeek = formatter.format(date).capitalize(rememberAppLocale())
            val dayOfMonth = "${date.dayOfMonth}"

            val textColor by animateColorAsState(
                targetValue = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                label = "DateTextColor"
            )

            val containerModifier = if (isSelected) {
                Modifier.clayContainer(
                    surfaceColor = AppTheme.colors.claySurface,
                    shape = ClayDefaults.ShapeMedium,
                    elevation = ClayDefaults.ElevationMedium,
                    highlightColor = AppTheme.colors.clayHighlight,
                    innerShadowColor = AppTheme.colors.clayInnerShadow
                )
            } else {
                Modifier
                    .clip(ClayDefaults.ShapeMedium)
                    .clayClickable(onClick = { onClick(date) })
            }

            Box(
                modifier = containerModifier
                    .width(64.dp)
                    .height(60.dp)
                    .then(
                        if (isSelected) Modifier
                            .clip(ClayDefaults.ShapeMedium)
                            .clayClickable(onClick = { onClick(date) })
                        else Modifier
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = dayOfWeek,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = textColor
                    )
                    Text(
                        text = dayOfMonth,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                        color = textColor
                    )
                }
            }
        }
    }
}

@Composable
fun GraphsPagerIndicatorSkeleton(
    color: State<Color>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(5) {
            Box(
                modifier = Modifier
                    .width(64.dp)
                    .height(60.dp)
                    .clayContainer(
                        surfaceColor = color.value,
                        shape = ClayDefaults.ShapeMedium,
                        elevation = ClayDefaults.ElevationLow,
                        highlightColor = AppTheme.colors.clayHighlight,
                        innerShadowColor = AppTheme.colors.clayInnerShadow
                    )
            )
        }
    }
}

@Preview
@Composable
private fun GraphsPagerIndicatorPreview() {
    AppTheme {
        GraphsPagerIndicator(
            state = listOf(
                LocalDate.parse("1970-01-01"),
                LocalDate.parse("1970-01-02"),
                LocalDate.parse("1970-01-03"),
                LocalDate.parse("1970-01-04"),
                LocalDate.parse("1970-01-05"),
                LocalDate.parse("1970-01-06")
            ),
            selected = 2,
            onClick = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}