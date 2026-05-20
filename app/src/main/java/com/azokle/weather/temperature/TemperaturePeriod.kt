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

package com.azokle.weather.temperature

import com.azokle.weather.forecast.HourPeriod
import java.time.LocalDate
import java.time.LocalDateTime

class TemperaturePeriod(moments: List<TemperatureMoment>) : HourPeriod<TemperatureMoment>(moments) {
    val minimum get() = minOf { it.temperature }

    val maximum get() = maxOf { it.temperature }

    override fun getDay(day: LocalDate) =
        super.getDay(day)?.let { TemperaturePeriod(it) }

    override fun momentsFrom(hourInclusive: LocalDateTime, takeMoments: Int?) =
        super.momentsFrom(hourInclusive, takeMoments)?.let { TemperaturePeriod(it) }

    override fun daysFrom(dayInclusive: LocalDate, takeDays: Int?) =
        super.daysFrom(dayInclusive, takeDays)?.map { TemperaturePeriod(it) }
}
