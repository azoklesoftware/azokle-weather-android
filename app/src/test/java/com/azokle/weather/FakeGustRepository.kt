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

import com.azokle.weather.gust.GustPeriod
import com.azokle.weather.gust.GustRepository
import com.azokle.weather.place.Coordinates
import com.azokle.weather.units.Units

class FakeGustRepository(private val cannedPeriod: GustPeriod) : GustRepository {
    override suspend fun period(coords: Coordinates, units: Units): GustPeriod = cannedPeriod
}