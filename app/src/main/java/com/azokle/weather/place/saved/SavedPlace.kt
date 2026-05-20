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

package com.azokle.weather.place.saved

import com.azokle.weather.condition.Condition
import com.azokle.weather.place.Place
import com.azokle.weather.temperature.Temperature
import java.time.LocalTime

data class SavedPlace(
    val place: Place,
    val time: LocalTime,
    val selected: Boolean,
    val conditions: Conditions?
) {
    data class Conditions(
        val temp: Temperature,
        val minTemp: Temperature,
        val maxTemp: Temperature,
        val condition: Condition
    )
}