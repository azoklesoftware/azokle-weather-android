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

import com.azokle.weather.humidity.Humidity
import com.azokle.weather.humidity.HumidityMoment
import com.azokle.weather.humidity.HumidityPeriod
import org.junit.Assert.*
import org.junit.Test
import java.time.temporal.ChronoUnit

class HumidityPeriodTest {
    @Test
    fun average() {
        val firstMoment = unixEpochStart
        val secondMoment = firstMoment.plus(1, ChronoUnit.HOURS)
        val period = HumidityPeriod(
            moments = listOf(
                HumidityMoment(firstMoment, Humidity(50.0)),
                HumidityMoment(secondMoment, Humidity(90.0))
            )
        )
        assertEquals(Humidity(70.0), period.average)
    }
}