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

package com.azokle.weather.summary.visibility

import com.azokle.weather.forecast.ForecastResult
import com.azokle.weather.place.Coordinates
import com.azokle.weather.units.Units
import com.azokle.weather.visibility.Visibility
import com.azokle.weather.visibility.VisibilityRepository
import java.time.LocalDateTime

class GetVisibilitySummary(private val repo: VisibilityRepository) {
    suspend operator fun invoke(
        coords: Coordinates,
        units: Units,
        now: LocalDateTime
    ): ForecastResult<VisibilitySummary> {
        val period = repo.period(coords, units) ?: return ForecastResult.FailedToDownload
        return ForecastResult.Success(
            data = VisibilitySummary(period[now]?.visibility ?: return ForecastResult.Outdated)
        )
    }
}

data class VisibilitySummary(val now: Visibility)