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

import com.azokle.weather.temperature.Temperature
import com.azokle.weather.temperature.TemperatureMoment
import com.azokle.weather.temperature.TemperaturePeriod
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.temporal.ChronoUnit

class TemperaturePeriodTest {
    @Test
    fun `minimum and maximum`() {
        val firstMoment = unixEpochStart
        val secondMoment = firstMoment.plus(1, ChronoUnit.HOURS)
        val period = TemperaturePeriod(
            moments = listOf(
                TemperatureMoment(firstMoment, Temperature.fromDegreesCelsius(1.0)),
                TemperatureMoment(secondMoment, Temperature.fromDegreesCelsius(2.0)),
            )
        )
        assertEquals(Temperature.fromDegreesCelsius(1.0), period.minimum)
        assertEquals(Temperature.fromDegreesCelsius(2.0), period.maximum)
    }
}