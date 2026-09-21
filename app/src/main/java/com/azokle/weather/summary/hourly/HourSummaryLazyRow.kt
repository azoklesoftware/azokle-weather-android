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

package com.azokle.weather.summary.hourly

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.azokle.weather.common.AppTheme
import com.azokle.weather.common.ClayDefaults
import com.azokle.weather.common.clayClickable
import com.azokle.weather.common.clayContainer
import com.azokle.weather.condition.Condition
import com.azokle.weather.pop.Pop
import com.azokle.weather.sun.SunEvent
import com.azokle.weather.temperature.Temperature
import java.time.LocalDateTime

private val contentPadding = 16.dp

@Composable
fun HourSummaryLazyRow(
    state: List<HourSummary>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .clayClickable(onClick = onClick)
            .clayContainer(
                surfaceColor = AppTheme.colors.claySurface,
                shape = ClayDefaults.ShapeLarge,
                elevation = ClayDefaults.ElevationMedium,
                highlightColor = AppTheme.colors.clayHighlight,
                innerShadowColor = AppTheme.colors.clayInnerShadow
            )
    ) {
        HourSummaryMaxHeightDummy(
            modifier = Modifier.padding(vertical = contentPadding)
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = contentPadding),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(
                items = state,
                key = { item ->
                    when (item) {
                        is HourSummary.Weather -> "weather_${item.time}"
                        is HourSummary.Sun -> "sun_${item.time}"
                    }
                }
            ) {
                when (it) {
                    is HourSummary.Weather -> WeatherHourSummary(it, Modifier.fillMaxHeight())
                    is HourSummary.Sun -> SunHourSummary(it, Modifier.fillMaxHeight())
                }
            }
        }
    }
}

@Composable
fun HourSummaryLazyRowSkeleton(color: State<Color>, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clayContainer(
                surfaceColor = color.value,
                shape = ClayDefaults.ShapeLarge,
                elevation = ClayDefaults.ElevationMedium,
                highlightColor = AppTheme.colors.clayHighlight,
                innerShadowColor = AppTheme.colors.clayInnerShadow
            )
    ) {
        HourSummaryMaxHeightDummy(modifier = Modifier.padding(vertical = contentPadding))
    }
}

@Preview
@Composable
private fun HourlySummaryPreview() {
    AppTheme {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            HourSummaryLazyRow(
                state = listOf(
                    HourSummary.Weather(
                        time = LocalDateTime.parse("2023-01-01T18:00"),
                        isNow = true,
                        temp = Temperature.fromDegreesCelsius(2.0),
                        pop = Pop(75.0),
                        desc = Condition(wmoCode = 1, isDay = true)
                    ),
                    HourSummary.Weather(
                        time = LocalDateTime.parse("2023-01-01T19:00"),
                        isNow = false,
                        temp = Temperature.fromDegreesCelsius(3.0),
                        pop = Pop(0.0),
                        desc = Condition(wmoCode = 2, isDay = true)
                    ),
                    HourSummary.Sun(
                        time = LocalDateTime.parse("2023-01-01T19:31"),
                        event = SunEvent.Sunset
                    ),
                    HourSummary.Weather(
                        time = LocalDateTime.parse("2023-01-01T20:00"),
                        isNow = false,
                        temp = Temperature.fromDegreesCelsius(5.0),
                        pop = Pop(0.0),
                        desc = Condition(wmoCode = 2, isDay = false)
                    ),
                ), onClick = {}
            )
        }
    }
}