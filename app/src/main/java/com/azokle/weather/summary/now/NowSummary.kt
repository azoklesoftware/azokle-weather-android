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

package com.azokle.weather.summary.now

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.azokle.weather.R
import com.azokle.weather.common.AppTheme
import com.azokle.weather.common.ClayDefaults
import com.azokle.weather.common.HighLowText
import com.azokle.weather.common.TextSkeleton
import com.azokle.weather.common.clayContainer
import com.azokle.weather.condition.Condition
import com.azokle.weather.condition.image
import com.azokle.weather.condition.string
import com.azokle.weather.temperature.Temperature
import com.azokle.weather.temperature.string

@Composable
fun NowSummary(state: NowSummary, modifier: Modifier = Modifier) {
    NowSummary(
        date = { Text(stringResource(id = R.string.date_time_now).uppercase()) },
        temperature = { Text(state.temp.string()) },
        icon = {
            Image(
                painter = state.cond.image(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        },
        highLow = {
            HighLowText(
                high = state.maxTemp.string(),
                low = state.minTemp.string()
            )
        },
        feelsLike = {
            Text(
                stringResource(
                    id = R.string.feels_like_value,
                    state.feelsLike.string()
                )
            )
        },
        condition = { Text(state.cond.string()) },
        modifier = modifier
    )
}

@Composable
fun NowSummary(
    temperature: @Composable () -> Unit,
    icon: @Composable () -> Unit,
    highLow: @Composable () -> Unit,
    feelsLike: @Composable () -> Unit,
    condition: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    date: (@Composable () -> Unit)? = null,
) {
    Box(
        modifier = modifier
            .clayContainer(
                surfaceColor = AppTheme.colors.clayHeroSurface,
                shape = ClayDefaults.ShapeHero,
                elevation = ClayDefaults.ElevationHigh,
                highlightColor = AppTheme.colors.clayHighlight,
                innerShadowColor = AppTheme.colors.clayInnerShadow
            )
            .padding(horizontal = 24.dp, vertical = 20.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (date != null) {
                    CompositionLocalProvider(
                        LocalTextStyle provides MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.2.sp
                        ),
                        LocalContentColor provides MaterialTheme.colorScheme.primary,
                        content = date
                    )
                } else {
                    Spacer(modifier = Modifier.width(1.dp))
                }
                CompositionLocalProvider(
                    LocalTextStyle provides MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    LocalContentColor provides MaterialTheme.colorScheme.onSurface,
                    content = condition
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                CompositionLocalProvider(
                    LocalTextStyle provides MaterialTheme.typography.displayLarge.copy(
                        fontWeight = FontWeight.Black,
                        letterSpacing = (-1.5).sp
                    ),
                    LocalContentColor provides MaterialTheme.colorScheme.onSurface,
                    content = temperature
                )
                Box(
                    modifier = Modifier
                        .size(72.dp)
                ) {
                    icon()
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                CompositionLocalProvider(
                    LocalTextStyle provides MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant,
                    content = highLow
                )
                CompositionLocalProvider(
                    LocalTextStyle provides MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.85f),
                    content = feelsLike
                )
            }
        }
    }
}

@Composable
fun NowSummarySkeleton(
    color: State<Color>,
    modifier: Modifier = Modifier,
    withDate: Boolean = false
) {
    NowSummary(
        date = if (withDate) {
            @Composable {
                TextSkeleton(
                    color = color,
                    shape = ClayDefaults.ShapeSmall,
                    contentPadding = PaddingValues(vertical = 2.dp),
                    modifier = Modifier.width(64.dp)
                )
            }
        } else null,
        temperature = {
            TextSkeleton(
                color = color,
                shape = ClayDefaults.ShapeMedium,
                contentPadding = PaddingValues(vertical = 2.dp),
                modifier = Modifier.width(140.dp).height(54.dp)
            )
        },
        icon = {
            TextSkeleton(
                color = color,
                shape = ClayDefaults.ShapePill,
                modifier = Modifier.size(64.dp)
            )
        },
        highLow = {
            TextSkeleton(
                color = color,
                shape = ClayDefaults.ShapeSmall,
                contentPadding = PaddingValues(vertical = 2.dp),
                modifier = Modifier.width(100.dp)
            )
        },
        feelsLike = {
            TextSkeleton(
                color = color,
                shape = ClayDefaults.ShapeSmall,
                contentPadding = PaddingValues(vertical = 2.dp),
                modifier = Modifier.width(80.dp)
            )
        },
        condition = {
            TextSkeleton(
                color = color,
                shape = ClayDefaults.ShapeSmall,
                contentPadding = PaddingValues(vertical = 2.dp),
                modifier = Modifier.width(70.dp)
            )
        },
        modifier = modifier
    )
}

@Preview
@Composable
private fun NowSummaryPreview() {
    AppTheme(darkTheme = true) {
        Surface(modifier = Modifier.width(400.dp)) {
            NowSummary(
                state = NowSummary(
                    temp = Temperature.fromDegreesCelsius(20.0),
                    feelsLike = Temperature.fromDegreesCelsius(18.0),
                    minTemp = Temperature.fromDegreesCelsius(15.0),
                    maxTemp = Temperature.fromDegreesCelsius(25.0),
                    cond = Condition(
                        wmoCode = 53,
                        isDay = true
                    )
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}