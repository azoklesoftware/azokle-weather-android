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

package com.azokle.weather.summary.humidity

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.azokle.weather.R
import com.azokle.weather.humidity.Humidity
import com.azokle.weather.humidity.string
import com.azokle.weather.summary.SummaryTile
import com.azokle.weather.temperature.Temperature
import com.azokle.weather.temperature.string

@Composable
fun HumiditySummary(state: HumiditySummary, modifier: Modifier = Modifier) {
    SummaryTile(
        label = { Text(stringResource(R.string.humidity)) },
        value = { Text(text = state.humidityNow.string()) },
        bottom = {
            Text(
                stringResource(
                    R.string.dew_point_value_right_now,
                    state.dewPointNow.string()
                )
            )
        },
        modifier = modifier
    )
}

@Preview
@Composable
private fun HumiditySummaryPreview() {
    MaterialTheme {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
                .size(200.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            HumiditySummary(
                state = HumiditySummary(
                    humidityNow = Humidity(92.0),
                    dewPointNow = Temperature.fromDegreesCelsius(19.0)
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}