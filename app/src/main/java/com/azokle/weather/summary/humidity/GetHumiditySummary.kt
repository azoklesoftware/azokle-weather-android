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

package com.azokle.weather.summary.humidity

import com.azokle.weather.humidity.Humidity
import com.azokle.weather.humidity.HumidityRepository
import com.azokle.weather.forecast.ForecastResult
import com.azokle.weather.place.Coordinates
import com.azokle.weather.temperature.Temperature
import com.azokle.weather.temperature.TemperatureRepository
import com.azokle.weather.units.Units
import java.time.LocalDateTime

class GetHumiditySummary(
    private val humidityRepo: HumidityRepository,
    private val dewPointRepo: TemperatureRepository,
) {
    suspend operator fun invoke(coords: Coordinates, units: Units, now: LocalDateTime): ForecastResult<HumiditySummary> {
        val humidityPeriod = humidityRepo.period(coords, units) ?: return ForecastResult.FailedToDownload
        val dewPointPeriod = dewPointRepo.period(coords, units) ?: return ForecastResult.FailedToDownload
        return ForecastResult.Success(HumiditySummary(
            humidityNow = humidityPeriod[now]?.humidity ?: return ForecastResult.Outdated,
            dewPointNow = dewPointPeriod[now]?.temperature ?: return ForecastResult.Outdated
        ))
    }
}

data class HumiditySummary(
    val humidityNow: Humidity,
    val dewPointNow: Temperature
)