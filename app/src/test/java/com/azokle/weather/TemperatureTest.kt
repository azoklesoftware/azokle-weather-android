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
import org.junit.Assert.*
import org.junit.Test

class TemperatureTest {
    @Test
    fun `convert to fahrenheit`() {
        val temperature = Temperature.fromDegreesCelsius(0.0)
        assertEquals(0.0, temperature.value, 0.0)
        assertEquals(Temperature.Unit.DegreesCelsius, temperature.unit)

        val fahrenheit = temperature.convertTo(Temperature.Unit.DegreesFahrenheit)
        assertEquals(32.0, fahrenheit.value, 0.01)
        assertEquals(Temperature.Unit.DegreesFahrenheit, fahrenheit.unit)
    }

    @Test
    fun equals() {
        val one = Temperature.fromDegreesCelsius(0.0)
        val two = Temperature.fromDegreesCelsius(0.0)
        two.convertTo(Temperature.Unit.DegreesFahrenheit)
        assertEquals(one, two)
    }

    @Test
    fun `greater than`() {
        val less = Temperature.fromDegreesCelsius(0.0)
        val greater = Temperature.fromDegreesCelsius(1.0)
        greater.convertTo(Temperature.Unit.DegreesFahrenheit)
        assertTrue(greater > less)
    }

    @Test
    fun plus() {
        val one = Temperature.fromDegreesCelsius(1.0)
        val two = Temperature.fromDegreesCelsius(2.0).convertTo(Temperature.Unit.DegreesFahrenheit)
        val sum = one + two
        assertEquals(
            Temperature.fromDegreesCelsius(3.0),
            sum
        )
    }
}