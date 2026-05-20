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

package com.azokle.weather.wind

import java.util.Objects

class Wind(
    val speed: WindSpeed,
    val from: WindDirection
) {
    val to: WindDirection = WindDirection(degrees = from.degrees + 180)

    override fun equals(other: Any?): Boolean =
        other is Wind && other.speed == speed && other.from == from

    override fun hashCode(): Int = Objects.hash(speed, from)

    override fun toString(): String = "$speed from $from"
}