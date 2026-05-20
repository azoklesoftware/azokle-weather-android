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

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.azokle.weather.R
import com.azokle.weather.common.AppTheme
import com.azokle.weather.common.rememberDateTimeFormatter
import com.azokle.weather.condition.Condition
import com.azokle.weather.condition.image
import com.azokle.weather.pop.Pop
import com.azokle.weather.pop.string
import com.azokle.weather.summary.PopAndDrop
import com.azokle.weather.temperature.Temperature
import com.azokle.weather.temperature.string
import java.time.LocalDateTime

@Composable
fun WeatherHourSummary(state: HourSummary.Weather, modifier: Modifier = Modifier) {
    val formatter = rememberDateTimeFormatter(ofPattern = R.string.date_time_pattern_hour)
    HourSummary(
        time = { Text(if (state.isNow) stringResource(R.string.date_time_now) else state.time.format(formatter)) },
        icon = {
            Image(
                painter = state.desc.image(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        },
        pop = state.pop?.string()?.let {
            @Composable {
                PopAndDrop(it)
            }
        },
        temperature = { Text(state.temp.string()) },
        modifier = modifier
    )
}

@Preview
@Composable
private fun WeatherSummaryPreview() {
    AppTheme {
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            WeatherHourSummary(
                state = HourSummary.Weather(
                    time = LocalDateTime.parse("2023-01-01T14:00"),
                    isNow = true,
                    temp = Temperature.fromDegreesCelsius(20.0),
                    pop = Pop(50.0),
                    desc = Condition(wmoCode = 51, isDay = true)
                ),
            )
            WeatherHourSummary(
                state = HourSummary.Weather(
                    time = LocalDateTime.parse("2023-01-01T15:00"),
                    isNow = false,
                    temp = Temperature.fromDegreesCelsius(20.0),
                    pop = null,
                    desc = Condition(wmoCode = 1, isDay = true)
                )
            )
        }
    }
}