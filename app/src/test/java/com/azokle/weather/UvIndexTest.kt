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

import com.azokle.weather.uvindex.UvIndex
import org.junit.Assert.*
import org.junit.Test

class UvIndexTest {
    @Test
    fun risk() {
        assertEquals(UvIndex.Risk.Low, UvIndex(value = 0).risk)
        assertEquals(UvIndex.Risk.Moderate, UvIndex(value = 3).risk)
        assertEquals(UvIndex.Risk.High, UvIndex(value = 6).risk)
        assertEquals(UvIndex.Risk.VeryHigh, UvIndex(value = 8).risk)
        assertEquals(UvIndex.Risk.Extreme, UvIndex(value = 19).risk)
    }

    @Test
    fun equals() {
        assertEquals(UvIndex(1), UvIndex(1))
    }

    @Test
    fun comparison() {
        assertTrue(UvIndex(1) > UvIndex(0))
    }
}