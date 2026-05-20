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

package com.azokle.weather.uvindex

import java.util.Objects

class UvIndex(val value: Int) : Comparable<UvIndex> {
    val risk: Risk = when {
        value < 3 -> Risk.Low
        value < 5 -> Risk.Moderate
        value < 7 -> Risk.High
        value < 10 -> Risk.VeryHigh
        else -> Risk.Extreme
    }

    enum class Risk {
        Low,
        Moderate,
        High,
        VeryHigh,
        Extreme
    }

    override fun compareTo(other: UvIndex): Int = value.compareTo(other.value)

    override fun equals(other: Any?): Boolean =
        other is UvIndex && other.value == value

    override fun hashCode(): Int = Objects.hash(value)

    override fun toString(): String = "${String.format("%.2f", value)} ($risk)"
}