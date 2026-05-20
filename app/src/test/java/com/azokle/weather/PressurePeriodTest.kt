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

import com.azokle.weather.pressure.Pressure
import com.azokle.weather.pressure.PressureMoment
import com.azokle.weather.pressure.PressurePeriod
import org.junit.Assert.*
import org.junit.Test
import java.time.temporal.ChronoUnit

class PressurePeriodTest {
    @Test
    fun minimum() {
        val firstMoment = unixEpochStart
        val secondMoment = firstMoment.plus(1, ChronoUnit.HOURS)
        val period = PressurePeriod(
            moments = listOf(
                PressureMoment(hour = firstMoment, Pressure.fromHectopascal(1000.0)),
                PressureMoment(hour = secondMoment, Pressure.fromHectopascal(1000.0))
            )
        )
        assertEquals(Pressure.fromHectopascal(1000.0), period.minimum)
    }

    @Test
    fun average() {
        val firstMoment = unixEpochStart
        val secondMoment = firstMoment.plus(1, ChronoUnit.HOURS)
        val period = PressurePeriod(
            moments = listOf(
                PressureMoment(firstMoment, Pressure.fromHectopascal(1000.0)),
                PressureMoment(secondMoment, Pressure.fromHectopascal(1010.0))
            )
        )
        assertEquals(Pressure.fromHectopascal(1005.0), period.average)
    }
}