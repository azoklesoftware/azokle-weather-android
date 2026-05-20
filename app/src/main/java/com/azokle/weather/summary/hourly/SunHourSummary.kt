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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.azokle.weather.R
import com.azokle.weather.common.AppTheme
import com.azokle.weather.sun.SunEvent
import com.azokle.weather.common.rememberDateTimeFormatter
import java.time.LocalDateTime

@Composable
fun SunHourSummary(state: HourSummary.Sun, modifier: Modifier = Modifier) {
    val formatter = rememberDateTimeFormatter(ofPattern = R.string.date_time_pattern_hour_minute)
    HourSummary(
        time = { Text(state.time.format(formatter)) },
        icon = {
            Image(
                painter = painterResource(id = if (state.event == SunEvent.Sunrise) AppTheme.icons.sunrise else AppTheme.icons.sunset),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        },
        pop = null,
        temperature = { Text(stringResource(if (state.event == SunEvent.Sunrise) R.string.sunrise else R.string.sunset)) },
        modifier = modifier
    )
}

@Preview
@Composable
private fun SunHourSummaryPreview() {
    AppTheme {
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
                .height(96.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SunHourSummary(
                state = HourSummary.Sun(
                    time = LocalDateTime.parse("2023-01-01T06:23"),
                    event = SunEvent.Sunrise
                )
            )
            SunHourSummary(
                state = HourSummary.Sun(
                    time = LocalDateTime.parse("2023-01-01T17:10"),
                    event = SunEvent.Sunset
                )
            )
        }
    }
}