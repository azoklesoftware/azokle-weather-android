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
import com.azokle.weather.humidity.Humidity
import com.azokle.weather.humidity.HumidityMoment
import com.azokle.weather.humidity.HumidityPeriod
import com.azokle.weather.summary.humidity.HumiditySummary
import com.azokle.weather.summary.humidity.GetHumiditySummary
import com.azokle.weather.temperature.Temperature
import com.azokle.weather.temperature.TemperatureMoment
import com.azokle.weather.temperature.TemperaturePeriod
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.temporal.ChronoUnit

class HumiditySummaryTest {
    

    @Test
    fun `gets humidity and dew point of now`() = runTest {
        val firstMoment = unixEpochStart
        val now = firstMoment.plus(10, ChronoUnit.MINUTES)
        val humidityPeriod = HumidityPeriod(listOf(HumidityMoment(firstMoment, Humidity(0.0))))
        val dewPointPeriod = TemperaturePeriod(
            listOf(
                TemperatureMoment(
                    firstMoment,
                    Temperature.fromDegreesCelsius(0.0)
                )
            )
        )
        val useCase = GetHumiditySummary(
            humidityRepo = FakeHumidityRepository(humidityPeriod),
            dewPointRepo = FakeTemperatureRepository(dewPointPeriod),
        )
        assertEquals(
            ForecastResult.Success(
                HumiditySummary(
                    humidityNow = Humidity(0.0),
                    dewPointNow = Temperature.fromDegreesCelsius(0.0)
                )
            ),
            useCase(coords, units, now)
        )
    }

    @Test
    fun `summary is outdated when no data from now`() = runTest {
        val firstMoment = unixEpochStart
        val now = firstMoment.plus(1, ChronoUnit.HOURS).plus(10, ChronoUnit.MINUTES)
        val humidityPeriod = HumidityPeriod(listOf(HumidityMoment(firstMoment, Humidity(0.0))))
        val dewPointPeriod = TemperaturePeriod(
            listOf(
                TemperatureMoment(
                    firstMoment,
                    Temperature.fromDegreesCelsius(0.0)
                )
            )
        )
        val useCase = GetHumiditySummary(
            humidityRepo = FakeHumidityRepository(humidityPeriod),
            dewPointRepo = FakeTemperatureRepository(dewPointPeriod),
        )
        assertEquals(ForecastResult.Outdated, useCase(coords, units, now))
    }
}