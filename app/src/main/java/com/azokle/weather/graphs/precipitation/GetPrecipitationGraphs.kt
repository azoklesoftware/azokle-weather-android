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

package com.azokle.weather.graphs.precipitation

import com.azokle.weather.condition.Condition
import com.azokle.weather.condition.ConditionRepository
import com.azokle.weather.forecast.ForecastResult
import com.azokle.weather.graphs.common.GraphTime
import com.azokle.weather.place.Coordinates
import com.azokle.weather.precipitation.MixedPrecipitation
import com.azokle.weather.precipitation.PrecipitationRepository
import com.azokle.weather.units.Units
import java.time.LocalDate
import java.time.LocalDateTime

class GetPrecipitationGraphs(
    private val precipRepo: PrecipitationRepository,
    private val condRepo: ConditionRepository
) {
    suspend operator fun invoke(
        coords: Coordinates,
        units: Units,
        now: LocalDateTime
    ): ForecastResult<PrecipitationGraphs> {
        val precip = precipRepo.period(coords, units) ?: return ForecastResult.FailedToDownload
        val cond = condRepo.period(coords, units) ?: return ForecastResult.FailedToDownload
        val precipDays = precip.daysFrom(now.toLocalDate()) ?: return ForecastResult.Outdated
        val condDays = cond.daysFrom(now.toLocalDate()) ?: return ForecastResult.Outdated
        return ForecastResult.Success(
            data = PrecipitationGraphs(
                max = precipDays.maxOf { it.max },
                graphs = precipDays.mapIndexed { dayIdx, day ->
                    PrecipitationGraph(
                        day = day.first().hour.toLocalDate(),
                        points = buildList {
                            addAll(
                                day.mapIndexed { momentIdx, moment ->
                                    PrecipitationGraphPoint(
                                        time = GraphTime(
                                            hour = moment.hour,
                                            now = now
                                        ),
                                        precip = moment.precipitation,
                                        cond = condDays[dayIdx][momentIdx].condition
                                    )
                                }
                            )
                        }
                    )
                }
            )
        )
    }
}

data class PrecipitationGraphs(
    val max: MixedPrecipitation,
    val graphs: List<PrecipitationGraph>
)

data class PrecipitationGraph(
    val day: LocalDate,
    val points: List<PrecipitationGraphPoint>
)

data class PrecipitationGraphPoint(
    val time: GraphTime,
    val precip: MixedPrecipitation,
    val cond: Condition
)