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

import com.azokle.weather.precipitation.MixedPrecipitation
import com.azokle.weather.precipitation.PrecipitationMoment
import com.azokle.weather.precipitation.PrecipitationPeriod
import com.azokle.weather.precipitation.Rain
import com.azokle.weather.precipitation.Showers
import com.azokle.weather.precipitation.Snow
import org.junit.Assert.assertEquals
import org.junit.Test

class PrecipitationPeriodTest {
    @Test
    fun depth() {
        val period = PrecipitationPeriod(
            moments = listOf(
                PrecipitationMoment(
                    unixEpochStart,
                    MixedPrecipitation.fromMillimeters(Rain.fromMillimeters(1.0), Showers.Zero, Snow.Zero),
                )
            )
        )
        assertEquals(MixedPrecipitation.fromMillimeters(Rain.fromMillimeters(1.0), Showers.Zero, Snow.Zero), period.total)
    }
}