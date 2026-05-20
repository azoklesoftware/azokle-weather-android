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

package com.azokle.weather.summary.hourly

import com.azokle.weather.condition.Condition
import com.azokle.weather.condition.ConditionRepository
import com.azokle.weather.forecast.ForecastResult
import com.azokle.weather.place.Coordinates
import com.azokle.weather.pop.Pop
import com.azokle.weather.pop.PopRepository
import com.azokle.weather.sun.SunEvent
import com.azokle.weather.sun.SunRepository
import com.azokle.weather.temperature.Temperature
import com.azokle.weather.temperature.TemperatureRepository
import com.azokle.weather.units.Units
import java.time.LocalDateTime

class GetHourlySummary(
    private val tempRepo: TemperatureRepository,
    private val popRepo: PopRepository,
    private val descRepo: ConditionRepository,
    private val sunRepo: SunRepository,
) {
    suspend operator fun invoke(
        coords: Coordinates,
        units: Units,
        now: LocalDateTime
    ): ForecastResult<List<HourSummary>> {
        val tempPeriod = tempRepo.period(coords, units) ?: return ForecastResult.FailedToDownload
        val popPeriod = popRepo.period(coords, units) ?: return ForecastResult.FailedToDownload
        val descPeriod = descRepo.period(coords, units) ?: return ForecastResult.FailedToDownload
        val sunPeriod = sunRepo.period(coords, units)

        val futureTemps = tempPeriod.momentsFrom(now, takeMoments = 24) ?: return ForecastResult.Outdated
        val futurePops = popPeriod.momentsFrom(now, takeMoments = 24) ?: return ForecastResult.Outdated
        val futureDesc = descPeriod.momentsFrom(now, takeMoments = 24) ?: return ForecastResult.Outdated
        val combinedWeatherData = buildList {
            for (i in futureTemps.indices) {
                add(
                    HourSummary.Weather(
                        time = futureTemps[i].hour,
                        isNow = i == 0,
                        temp = futureTemps[i].temperature,
                        pop = futurePops[i].pop.takeIf { it.value > 0 },
                        desc = futureDesc[i].condition
                    )
                )
            }
        }
        val combinedSunData = sunPeriod
            ?.momentsFrom(now, takeMomentsUpToHoursInFuture = 24)
            ?.map {
                HourSummary.Sun(
                    time = it.time,
                    event = it.event
                )
            }
            ?: listOf()

        return ForecastResult.Success((combinedWeatherData + combinedSunData).sortedBy { it.time })
    }
}

sealed interface HourSummary {
    val time: LocalDateTime

    data class Weather(
        override val time: LocalDateTime,
        val isNow: Boolean,
        val temp: Temperature,
        val pop: Pop?,
        val desc: Condition
    ) : HourSummary

    data class Sun(
        override val time: LocalDateTime,
        val event: SunEvent
    ) : HourSummary
}