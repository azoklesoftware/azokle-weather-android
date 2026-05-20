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

package com.azokle.weather.summary.now

import com.azokle.weather.forecast.ForecastResult
import com.azokle.weather.temperature.Temperature
import com.azokle.weather.temperature.TemperatureRepository
import com.azokle.weather.units.Units
import com.azokle.weather.condition.Condition
import com.azokle.weather.condition.ConditionRepository
import com.azokle.weather.place.Coordinates
import java.time.LocalDateTime

class GetNowSummary(
    private val tempRepo: TemperatureRepository,
    private val feelsRepo: TemperatureRepository,
    private val descRepo: ConditionRepository,
) {
    suspend operator fun invoke(coords: Coordinates, units: Units, now: LocalDateTime) : ForecastResult<NowSummary> {
        val tempPeriod = tempRepo.period(coords, units) ?: return ForecastResult.FailedToDownload
        val feelsPeriod = feelsRepo.period(coords, units) ?: return ForecastResult.FailedToDownload
        val descPeriod = descRepo.period(coords, units) ?: return ForecastResult.FailedToDownload
        val tempToday = tempPeriod.getDay(now.toLocalDate(),) ?: return ForecastResult.Outdated
        return ForecastResult.Success(NowSummary(
            temp = tempPeriod[now]?.temperature ?: return ForecastResult.Outdated,
            feelsLike = feelsPeriod[now]?.temperature ?: return ForecastResult.Outdated,
            minTemp = tempToday.minimum,
            maxTemp = tempToday.maximum,
            cond = descPeriod[now]?.condition ?: return ForecastResult.Outdated
        ))
    }
}

data class NowSummary(
    val temp: Temperature,
    val feelsLike: Temperature,
    val minTemp: Temperature,
    val maxTemp: Temperature,
    val cond: Condition
)