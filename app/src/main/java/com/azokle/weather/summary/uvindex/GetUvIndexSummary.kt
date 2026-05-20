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

package com.azokle.weather.summary.uvindex

import com.azokle.weather.forecast.ForecastResult
import com.azokle.weather.place.Coordinates
import com.azokle.weather.units.Units
import com.azokle.weather.uvindex.UvIndex
import com.azokle.weather.uvindex.UvIndexRepository
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit

class GetUvIndexSummary(private val repo: UvIndexRepository) {
    suspend operator fun invoke(
        coords: Coordinates,
        units: Units,
        now: LocalDateTime
    ): ForecastResult<UvIndexSummary> {
        val uvPeriod = repo.period(coords, units) ?: return ForecastResult.FailedToDownload
        val futureUv = uvPeriod.getDay(now.toLocalDate())?.momentsFrom(now) ?: return ForecastResult.Outdated
        val protection = futureUv.protectionWindows.firstOrNull()?.let {
            if (it.startInclusive == now.truncatedTo(ChronoUnit.HOURS)) {
                if (it.endExclusive == null) {
                    UseProtection.UntilEndOfDay
                } else {
                    UseProtection.Until(
                        endExclusive = it.endExclusive.toLocalTime()
                    )
                }
            } else {
                if (it.endExclusive == null) {
                    UseProtection.FromUntilEndOfDay(
                        startInclusive = it.startInclusive.toLocalTime()
                    )
                } else {
                    UseProtection.FromUntil(
                        startInclusive = it.startInclusive.toLocalTime(),
                        endExclusive = it.endExclusive.toLocalTime()
                    )
                }
            }
        } ?: UseProtection.None
        return ForecastResult.Success(
            UvIndexSummary(
                now = uvPeriod[now]?.uvIndex ?: return ForecastResult.Outdated,
                useProtection = protection
            )
        )
    }
}

data class UvIndexSummary(
    val now: UvIndex,
    val useProtection: UseProtection
)

sealed interface UseProtection {
    data class FromUntil(
        val startInclusive: LocalTime,
        val endExclusive: LocalTime
    ) : UseProtection

    data class Until(val endExclusive: LocalTime) : UseProtection

    data object UntilEndOfDay : UseProtection

    data class FromUntilEndOfDay(val startInclusive: LocalTime) : UseProtection

    data object None : UseProtection
}