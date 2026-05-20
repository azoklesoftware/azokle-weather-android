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

package com.azokle.weather.summary.precipitation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
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
import com.azokle.weather.precipitation.MixedPrecipitation
import com.azokle.weather.precipitation.Precipitation
import com.azokle.weather.precipitation.Rain
import com.azokle.weather.precipitation.Showers
import com.azokle.weather.precipitation.Snow
import com.azokle.weather.precipitation.string
import com.azokle.weather.precipitation.typeString
import com.azokle.weather.precipitation.unitString
import com.azokle.weather.precipitation.valueString
import com.azokle.weather.summary.SummaryTile
import com.azokle.weather.summary.ValueAndUnit
import com.azokle.weather.common.AppTheme
import com.azokle.weather.common.rememberDateTimeFormatter
import java.time.LocalDate

@Composable
fun PrecipitationSummary(
    state: PrecipitationSummary,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    SummaryTile(
        label = { Label(past = state.past) },
        value = { Value(past = state.past) },
        supportingValue = {
            Text(
                stringResource(
                    R.string.precip_value_in_last_hours,
                    "${state.past.inHours}"
                )
            )
        },
        bottom = { Bottom(future = state.future) },
        onClick = onClick,
        modifier = modifier
    )
}

@Composable
private fun Label(past: PastPrecipitation) {
    Text(past.total.typeString())
}

@Composable
private fun Value(past: PastPrecipitation) {
    if (past.total.unit == Precipitation.Unit.Inches) {
        Text(text = past.total.string())
    } else {
        ValueAndUnit(
            value = past.total.valueString(),
            unit = past.total.unitString()
        )
    }
}

@Composable
fun Bottom(future: FuturePrecipitation) {
    val formatter = rememberDateTimeFormatter(ofPattern = R.string.date_time_pattern_dow)
    Text(
        text = when (future) {
            is FuturePrecipitation.InHours -> stringResource(
                when (future.total) {
                    is MixedPrecipitation -> R.string.precip_value_mixed_in_next_hours
                    is Rain -> R.string.precip_value_rain_in_next_hours
                    is Showers -> R.string.precip_value_showers_in_next_hours
                    is Snow -> R.string.precip_value_snow_in_next_hours
                },
                future.total.string(),
                future.inHours
            )

            is FuturePrecipitation.OnDay -> stringResource(
                when (future.total) {
                    is MixedPrecipitation -> R.string.precip_value_mixed_on_day
                    is Rain -> R.string.precip_value_rain_on_day
                    is Showers -> R.string.precip_value_showers_on_day
                    is Snow -> R.string.precip_value_snow_on_day
                },
                future.total.string(),
                formatter.format(future.onDay)
            )

            is FuturePrecipitation.None -> stringResource (
                R.string.precip_value_none_in_next_days,
                future.inDays
            )
        }
    )
}

@Preview
@Composable
private fun PrecipitationSummaryPreview() {
    AppTheme {
        Surface {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp)
                    .width(200.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                PrecipitationSummary(
                    state = PrecipitationSummary(
                        past = PastPrecipitation(
                            inHours = 12,
                            total = Rain.fromMillimeters(12.59)
                        ),
                        future = FuturePrecipitation.OnDay(
                            onDay = LocalDate.parse("2023-01-01"),
                            total = Rain.fromMillimeters(5.0)
                        )
                    ),
                    onClick = {},
                    modifier = Modifier.aspectRatio(1f)
                )
                PrecipitationSummary(
                    state = PrecipitationSummary(
                        past = PastPrecipitation(
                            inHours = 24,
                            total = Rain.Zero
                        ),
                        future = FuturePrecipitation.InHours(
                            inHours = 24,
                            total = Rain.Zero
                        ),
                    ),
                    onClick = {},
                    modifier = Modifier.aspectRatio(1f)
                )
                PrecipitationSummary(
                    state = PrecipitationSummary(
                        past = PastPrecipitation(
                            inHours = 24,
                            total = Snow.fromMillimeters(100.0).convertTo(Precipitation.Unit.Inches)
                        ),
                        future = FuturePrecipitation.None(inDays = 7)
                    ),
                    onClick = {},
                    modifier = Modifier.aspectRatio(1f)
                )
            }
        }
    }
}