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

import com.azokle.weather.condition.Condition
import com.azokle.weather.condition.ConditionMoment
import com.azokle.weather.condition.ConditionPeriod
import com.azokle.weather.forecast.ForecastResult
import com.azokle.weather.summary.now.NowSummary
import com.azokle.weather.summary.now.GetNowSummary
import com.azokle.weather.temperature.Temperature
import com.azokle.weather.temperature.TemperatureMoment
import com.azokle.weather.temperature.TemperaturePeriod
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.temporal.ChronoUnit

class GetNowSummaryTest {
    

    @Test
    fun `summarizes current temperature, feels like and description and returns min and max temp of today`() =
        runTest {
            val firstDayFirstMoment = unixEpochStart.plus(22, ChronoUnit.HOURS)
            val now = firstDayFirstMoment.plus(10, ChronoUnit.MINUTES)
            val firstDaySecondMoment = firstDayFirstMoment.plus(1, ChronoUnit.HOURS)
            val secondDayFirstMoment = firstDaySecondMoment.plus(1, ChronoUnit.HOURS)
            val temperaturePeriod = TemperaturePeriod(
                moments = listOf(
                    TemperatureMoment(firstDayFirstMoment, Temperature.fromDegreesCelsius(0.0)),
                    TemperatureMoment(firstDaySecondMoment, Temperature.fromDegreesCelsius(1.0)),
                    TemperatureMoment(secondDayFirstMoment, Temperature.fromDegreesCelsius(20.0))
                )
            )
            val feelsLikePeriod = TemperaturePeriod(
                moments = listOf(
                    TemperatureMoment(firstDayFirstMoment, Temperature.fromDegreesCelsius(-1.0)),
                    TemperatureMoment(firstDaySecondMoment, Temperature.fromDegreesCelsius(0.0)),
                    TemperatureMoment(secondDayFirstMoment, Temperature.fromDegreesCelsius(20.0))
                )
            )
            val conditionPeriod = ConditionPeriod(
                moments = listOf(
                    ConditionMoment(firstDayFirstMoment, Condition(wmoCode = 1, isDay = false)),
                    ConditionMoment(firstDaySecondMoment, Condition(wmoCode = 2, isDay = false)),
                    ConditionMoment(secondDayFirstMoment, Condition(wmoCode = 3, isDay = true))
                )
            )
            val useCase = GetNowSummary(
                tempRepo = FakeTemperatureRepository(temperaturePeriod),
                feelsRepo = FakeTemperatureRepository(feelsLikePeriod),
                descRepo = FakeConditionRepository(conditionPeriod),
            )
            val summary = useCase(coords, units, now)
            assertEquals(
                ForecastResult.Success(
                    NowSummary(
                        temp = Temperature.fromDegreesCelsius(0.0),
                        feelsLike = Temperature.fromDegreesCelsius(-1.0),
                        minTemp = Temperature.fromDegreesCelsius(0.0),
                        maxTemp = Temperature.fromDegreesCelsius(1.0),
                        cond = Condition(1, false)
                    )
                ),
                summary
            )
        }

    @Test
    fun `summary is outdated when no data after now`() = runTest {
        val firstMoment = unixEpochStart
        val afterFirstMoment = firstMoment.plus(1, ChronoUnit.HOURS)
        val now = afterFirstMoment.plus(10, ChronoUnit.MINUTES)
        val temperaturePeriod = TemperaturePeriod(
            moments = listOf(
                TemperatureMoment(firstMoment, Temperature.fromDegreesCelsius(0.0)),
            )
        )
        val feelsLikePeriod = TemperaturePeriod(
            moments = listOf(
                TemperatureMoment(firstMoment, Temperature.fromDegreesCelsius(-1.0)),
            )
        )
        val conditionPeriod = ConditionPeriod(
            moments = listOf(
                ConditionMoment(firstMoment, Condition(wmoCode = 1, isDay = false)),
            )
        )
        val useCase = GetNowSummary(
            tempRepo = FakeTemperatureRepository(temperaturePeriod),
            feelsRepo = FakeTemperatureRepository(feelsLikePeriod),
            descRepo = FakeConditionRepository(conditionPeriod),
        )
        assertEquals(ForecastResult.Outdated, useCase(coords, units, now))
    }
}