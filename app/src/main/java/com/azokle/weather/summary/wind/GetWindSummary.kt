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

package com.azokle.weather.summary.wind

import com.azokle.weather.forecast.ForecastResult
import com.azokle.weather.gust.GustRepository
import com.azokle.weather.place.Coordinates
import com.azokle.weather.units.Units
import com.azokle.weather.wind.Wind
import com.azokle.weather.wind.WindRepository
import com.azokle.weather.wind.WindSpeed
import java.time.LocalDateTime

class GetWindSummary(
    private val windRepo: WindRepository,
    private val gustRepo: GustRepository,
) {
    suspend operator fun invoke(
        coords: Coordinates,
        units: Units,
        now: LocalDateTime
    ): ForecastResult<WindSummary> {
        val windPeriod = windRepo.period(coords, units) ?: return ForecastResult.FailedToDownload
        val gustPeriod = gustRepo.period(coords, units) ?: return ForecastResult.FailedToDownload
        return ForecastResult.Success(
            WindSummary(
                windNow = windPeriod[now]?.wind ?: return ForecastResult.Outdated,
                gustNow = gustPeriod[now]?.speed ?: return ForecastResult.Outdated
            )
        )
    }
}

data class WindSummary(
    val windNow: Wind,
    val gustNow: WindSpeed
)