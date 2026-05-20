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
import org.junit.Assert.*
import org.junit.Test

class HumidityTest {
    @Test
    fun equals() {
        assertEquals(Humidity(1.0), Humidity(1.0))
    }

    @Test
    fun compare() {
        assertTrue(Humidity(1.0) > Humidity(0.0))
    }

    @Test
    fun plus() {
        assertEquals(Humidity(1.0) + Humidity(1.0), Humidity(2.0))
    }

    @Test
    fun divide() {
        assertEquals(Humidity(50.0) / 2, Humidity(25.0))
    }
}