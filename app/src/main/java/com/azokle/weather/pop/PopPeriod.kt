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

package com.azokle.weather.pop

import com.azokle.weather.forecast.HourPeriod
import java.time.LocalDate
import java.time.LocalDateTime

class PopPeriod(moments: List<PopMoment>) : HourPeriod<PopMoment>(moments) {
    val maximum get() = maxOf { it.pop }
    val once: Pop get() {
        val firstPop = first().pop
        if (size == 1) return firstPop

        var probNone = 1 - (firstPop.value / 100)
        for (i in 1..lastIndex) {
            probNone *= 1 - (get(i).pop.value / 100)
        }
        val probOnce = 1 - probNone
        return Pop(value = probOnce * 100)
    }

    override fun momentsFrom(hourInclusive: LocalDateTime, takeMoments: Int?) =
        super.momentsFrom(hourInclusive, takeMoments)?.let { PopPeriod(it) }

    override fun daysFrom(dayInclusive: LocalDate, takeDays: Int?) =
        super.daysFrom(dayInclusive, takeDays)?.map { PopPeriod(it) }
}