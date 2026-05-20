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
import com.azokle.weather.gust.GustMoment
import com.azokle.weather.gust.GustPeriod
import com.azokle.weather.summary.wind.WindSummary
import com.azokle.weather.summary.wind.GetWindSummary
import com.azokle.weather.wind.Wind
import com.azokle.weather.wind.WindDirection
import com.azokle.weather.wind.WindMoment
import com.azokle.weather.wind.WindPeriod
import com.azokle.weather.wind.WindSpeed
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.temporal.ChronoUnit

class WindSummaryTest {
    

    @Test
    fun `gets current wind speed, direction and gust speed`() = runTest {
        val time = unixEpochStart
        val now = time.plus(10, ChronoUnit.MINUTES)
        val windPeriod = WindPeriod(
            listOf(
                WindMoment(
                    time,
                    Wind(WindSpeed.fromMetersPerSecond(0.0), WindDirection(0.0))
                )
            )
        )
        val gustPeriod = GustPeriod(listOf(GustMoment(time, WindSpeed.fromMetersPerSecond(1.0))))
        val useCase = GetWindSummary(
            windRepo = FakeWindRepository(windPeriod),
            gustRepo = FakeGustRepository(gustPeriod),
        )
        val summary = useCase(coords, units, now)
        assertEquals(
            ForecastResult.Success(
                WindSummary(
                    windNow = Wind(WindSpeed.fromMetersPerSecond(0.0), WindDirection(0.0)),
                    gustNow = WindSpeed.fromMetersPerSecond(1.0)
                )
            ),
            summary
        )
    }

    @Test
    fun `outdated when no now`() = runTest {
        val time = unixEpochStart
        val now = time.plus(1, ChronoUnit.HOURS).plus(10, ChronoUnit.MINUTES)
        val windPeriod = WindPeriod(
            listOf(
                WindMoment(
                    time,
                    Wind(WindSpeed.fromMetersPerSecond(0.0), WindDirection(0.0))
                )
            )
        )
        val gustPeriod = GustPeriod(listOf(GustMoment(time, WindSpeed.fromMetersPerSecond(1.0))))
        val useCase = GetWindSummary(
            windRepo = FakeWindRepository(windPeriod),
            gustRepo = FakeGustRepository(gustPeriod),
        )
        val summary = useCase(coords, units, now)
        assertEquals(ForecastResult.Outdated, summary)
    }
}