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

package com.azokle.weather.graphs.temperature

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.azokle.weather.R
import com.azokle.weather.common.AppTheme
import com.azokle.weather.common.HighLowText
import com.azokle.weather.condition.Condition
import com.azokle.weather.condition.image
import com.azokle.weather.condition.string
import com.azokle.weather.summary.now.NowSummary
import com.azokle.weather.temperature.Temperature
import com.azokle.weather.temperature.string
import java.time.LocalDate

@Composable
fun TemperatureGraphSummary(state: TemperatureGraphSummary, modifier: Modifier = Modifier) {
    val now = state.now
    NowSummary(
        temperature = {
            when (now) {
                null -> {
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(state.maxTemp.string())
                        Text(
                            text = state.minTemp.string(),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                else -> Text(text = now.temp.string())
            }
        },
        icon = {
            Image(
                painter = state.condition.image(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        },
        highLow = {
            when (now) {
                null -> when {
                    state.minTemp.unit == Temperature.Unit.DegreesCelsius -> Text(stringResource(R.string.cond_screen_temp_unit_celsius))
                    else -> Text(stringResource(R.string.cond_screen_temp_unit_fahrenheit))
                }

                else -> HighLowText(
                    high = state.maxTemp.string(),
                    low = state.minTemp.string()
                )
            }
        },
        feelsLike = {
            if (now != null) Text(
                stringResource(
                    R.string.feels_like_value,
                    now.feelsLike.string()
                )
            )
        },
        condition = { if (now != null) Text(state.condition.string()) },
        modifier = modifier
    )
}

@Preview
@Composable
private fun TemperatureGraphSummaryTodayPreview() {
    AppTheme {
        Surface {
            TemperatureGraphSummary(
                state = TemperatureGraphSummary(
                    day = LocalDate.parse("1970-01-03"),
                    minTemp = Temperature.fromDegreesCelsius(10.0),
                    maxTemp = Temperature.fromDegreesCelsius(30.0),
                    condition = Condition(wmoCode = 53, isDay = true),
                    now = null
                ),
                modifier = Modifier
                    .width(400.dp)
                    .padding(16.dp)
            )
        }
    }
}

@Preview
@Composable
private fun TemperatureGraphSummaryDayPreview() {
    AppTheme {
        Surface {
            TemperatureGraphSummary(
                state = TemperatureGraphSummary(
                    day = LocalDate.parse("1970-01-03"),
                    minTemp = Temperature.fromDegreesCelsius(10.0),
                    maxTemp = Temperature.fromDegreesCelsius(30.0),
                    condition = Condition(wmoCode = 53, isDay = true),
                    now = null,
                ),
                modifier = Modifier
                    .width(400.dp)
                    .padding(16.dp)
            )
        }
    }
}