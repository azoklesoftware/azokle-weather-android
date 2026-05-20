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

package com.azokle.weather

import com.azokle.weather.forecast.ForecastResult
import com.azokle.weather.graphs.precipitation.PrecipitationTotal
import com.azokle.weather.graphs.precipitation.GetPrecipitationTotals
import com.azokle.weather.graphs.precipitation.TotalPrecipitationInHours
import com.azokle.weather.precipitation.MixedPrecipitation
import com.azokle.weather.precipitation.PrecipitationMoment
import com.azokle.weather.precipitation.PrecipitationPeriod
import com.azokle.weather.precipitation.Rain
import com.azokle.weather.precipitation.Showers
import com.azokle.weather.precipitation.Snow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class GetPrecipitationTotalsTest {
    

    @Test
    fun `generates last and future for today and future for other days`() = runTest {
        val startOfFirstDay = unixEpochStart
        val startOfSecondDay = startOfFirstDay.plus(1, ChronoUnit.DAYS)
        val repo = FakePrecipitationRepository(PrecipitationPeriod(buildList {
            for (i in 0..23) {
                add(
                    PrecipitationMoment(
                        hour = startOfFirstDay.plus(i.toLong(), ChronoUnit.HOURS),
                        precipitation = MixedPrecipitation.fromMillimeters(
                            Rain.fromMillimeters(1.0),
                            Showers.Zero,
                            Snow.fromMillimeters(5.0)
                        )
                    )
                )
            }
            for (i in 0..23) {
                add(
                    PrecipitationMoment(
                        hour = startOfSecondDay.plus(i.toLong(), ChronoUnit.HOURS),
                        precipitation = MixedPrecipitation.fromMillimeters(
                            Rain.Zero,
                            Showers.fromMillimeters(2.0),
                            Snow.Zero
                        )
                    )
                )
            }
        }))
        val useCase = GetPrecipitationTotals(repo)
        val totals = (useCase(
            coords,
            units,
            startOfFirstDay.plus(8, ChronoUnit.HOURS)
        ) as ForecastResult.Success).data
        assertEquals(
            listOf(
                PrecipitationTotal.Today(
                    day = LocalDate.parse("1970-01-01"),
                    past = TotalPrecipitationInHours(
                        hours = 8,
                        total = MixedPrecipitation.fromMillimeters(
                            rain = Rain.fromMillimeters(8.0),
                            showers = Showers.Zero,
                            snow = Snow.fromMillimeters(40.0)
                        ),
                    ),
                    future = TotalPrecipitationInHours(
                        hours = 24,
                        total = MixedPrecipitation.fromMillimeters(
                            rain = Rain.fromMillimeters(16.0),
                            showers = Showers.fromMillimeters(16.0),
                            snow = Snow.fromMillimeters(80.0)
                        )
                    )
                ),
                PrecipitationTotal.OtherDay(
                    day = LocalDate.parse("1970-01-02"),
                    total = Showers.fromMillimeters(48.0)
                )
            ),
            totals
        )
    }
}