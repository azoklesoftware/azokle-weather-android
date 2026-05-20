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
import kotlin.math.ceil

class WindDirection(degrees: Double) {
    val degrees: Double = degrees + ceil(-degrees / 360) * 360
    val compass: Compass = Compass.entries[(degrees / 22.5 + 0.5).toInt() % 16]

    enum class Compass {
        N, NNE, NE, ENE,
        E, ESE, SE, SSE,
        S, SSW, SW, WSW,
        W, WNW, NW, NNW
    }

    override fun equals(other: Any?): Boolean =
        other is WindDirection && other.degrees == degrees

    override fun hashCode(): Int = Objects.hash(degrees)

    override fun toString(): String = "${String.format("%.2f", degrees)}° ($compass)"
}