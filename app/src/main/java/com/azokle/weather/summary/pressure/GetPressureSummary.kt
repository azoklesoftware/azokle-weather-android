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

package com.azokle.weather.summary.pressure

import com.azokle.weather.pressure.Pressure
import com.azokle.weather.pressure.PressureRepository
import com.azokle.weather.forecast.ForecastResult
import com.azokle.weather.place.Coordinates
import com.azokle.weather.units.Units
import java.time.LocalDateTime
import kotlin.math.absoluteValue

class GetPressureSummary(private val repo: PressureRepository) {
    suspend operator fun invoke(
        coords: Coordinates,
        units: Units,
        now: LocalDateTime
    ): ForecastResult<PressureSummary> {
        val pressurePeriod = repo.period(coords, units) ?: return ForecastResult.FailedToDownload
        val pressureToday = pressurePeriod.getDay(now.toLocalDate()) ?: return ForecastResult.Outdated
        val pressureNow = pressurePeriod[now]?.pressure ?: return ForecastResult.Outdated

        val nowHpa = pressureNow.convertTo(Pressure.Unit.Hectopascal).value
        val pastHpa = pressurePeriod.momentsUntil(now, takeMoments = 2)?.firstOrNull()
            ?.pressure?.convertTo(Pressure.Unit.Hectopascal)?.value
            ?: return ForecastResult.Outdated
        val diffHpa = (nowHpa - pastHpa).absoluteValue
        val trend = when {
            diffHpa < 1 -> PressureTrend.Stable
            diffHpa > 0 -> PressureTrend.Rising
            else -> PressureTrend.Falling
        }

        return ForecastResult.Success(PressureSummary(
            now = pressureNow,
            average = pressureToday.average,
            trend = trend
        ))
    }
}

data class PressureSummary(
    val now: Pressure,
    val average: Pressure,
    val trend: PressureTrend
)

enum class PressureTrend {
    Rising, Falling, Stable
}