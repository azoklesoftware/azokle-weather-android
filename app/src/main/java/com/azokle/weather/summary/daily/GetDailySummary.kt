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

package com.azokle.weather.summary.daily

import com.azokle.weather.pop.Pop
import com.azokle.weather.pop.PopRepository
import com.azokle.weather.forecast.ForecastResult
import com.azokle.weather.temperature.Temperature
import com.azokle.weather.temperature.TemperatureRepository
import com.azokle.weather.units.Units
import com.azokle.weather.condition.Condition
import com.azokle.weather.condition.ConditionRepository
import com.azokle.weather.place.Coordinates
import java.time.LocalDate
import java.time.LocalDateTime

class GetDailySummary(
    private val tempRepo: TemperatureRepository,
    private val descRepo: ConditionRepository,
    private val popRepo: PopRepository,
) {
    suspend operator fun invoke(coords: Coordinates, units: Units, now: LocalDateTime): ForecastResult<DailySummary> {
        val tempPeriod = tempRepo.period(coords, units) ?: return ForecastResult.FailedToDownload
        val descPeriod = descRepo.period(coords, units) ?: return ForecastResult.FailedToDownload
        val popPeriod = popRepo.period(coords, units) ?: return ForecastResult.FailedToDownload

        val futureTempDays = tempPeriod.daysFrom(now.toLocalDate()) ?: return ForecastResult.Outdated
        val popDays = popPeriod.momentsFrom(now)?.daysFrom(now.toLocalDate()) ?: return ForecastResult.Outdated
        val descDays = descPeriod.momentsFrom(now)?.daysFrom(now.toLocalDate()) ?: return ForecastResult.Outdated

        val minOfAllDays = futureTempDays.minOf { it.minimum }
        val maxOfAllDays = futureTempDays.maxOf { it.maximum }

        return ForecastResult.Success(DailySummary(
            minTemp = minOfAllDays,
            maxTemp = maxOfAllDays,
            days = buildList {
                for (i in futureTempDays.indices) {
                    add(
                        DaySummary(
                            isToday = i == 0,
                            time = futureTempDays[i].first().hour.toLocalDate(),
                            tempNow = futureTempDays[i][now]?.temperature,
                            min = futureTempDays[i].minimum,
                            max = futureTempDays[i].maximum,
                            pop = popDays[i].maximum.takeIf { it.value > 0 },
                            desc = descDays[i].day ?: descDays[i].night!!
                        )
                    )
                }
            }
        ))
    }
}

data class DailySummary(
    val minTemp: Temperature,
    val maxTemp: Temperature,
    val days: List<DaySummary>
)

data class DaySummary(
    val isToday: Boolean,
    val time: LocalDate,
    val tempNow: Temperature?,
    val min: Temperature,
    val max: Temperature,
    val pop: Pop?,
    val desc: Condition
)