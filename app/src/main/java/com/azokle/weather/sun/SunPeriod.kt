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

package com.azokle.weather.sun

import java.time.Duration
import java.time.LocalDateTime

class SunPeriod(val moments: List<SunMoment>) {
    init {
        requireNotEmpty()
        requireAscending()
    }

    fun momentsFrom(time: LocalDateTime, takeMomentsUpToHoursInFuture: Int? = null): List<SunMoment>? =
        moments.filter {
            val durationBetween = Duration.between(time, it.time)
            val hoursBetween = durationBetween.toHours()
            val maxHours = takeMomentsUpToHoursInFuture ?: Int.MAX_VALUE
            durationBetween >= Duration.ZERO && hoursBetween in 0..maxHours
        }.takeIf { it.isNotEmpty() }

    private fun requireNotEmpty() =
        require(moments.isNotEmpty()) { "Moments of SunPeriod must not be empty." }

    private fun requireAscending() {
        if (moments.size == 1) return
        var previousMoment = moments[0]
        for (i in 1..moments.lastIndex) {
            val nextMoment = moments[i]
            require(previousMoment.time < nextMoment.time) {
                "Moments of SunPeriod must be sorted, but contained ${previousMoment.time} before ${nextMoment.time}."
            }
            previousMoment = nextMoment
        }
    }
}