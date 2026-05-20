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

package com.azokle.weather.forecast

import com.azokle.weather.gust.GustPeriod
import com.azokle.weather.humidity.HumidityPeriod
import com.azokle.weather.pop.PopPeriod
import com.azokle.weather.precipitation.PrecipitationPeriod
import com.azokle.weather.pressure.PressurePeriod
import com.azokle.weather.sun.SunPeriod
import com.azokle.weather.temperature.TemperaturePeriod
import com.azokle.weather.uvindex.UvIndexPeriod
import com.azokle.weather.visibility.VisibilityPeriod
import com.azokle.weather.condition.ConditionPeriod
import com.azokle.weather.wind.WindPeriod

data class Forecast(
    val temperature: TemperaturePeriod,
    val feelsLike: TemperaturePeriod,
    val dewPoint: TemperaturePeriod,
    val sun: SunPeriod?,
    val pop: PopPeriod,
    val precipitation: PrecipitationPeriod,
    val uvIndex: UvIndexPeriod,
    val wind: WindPeriod,
    val gust: GustPeriod,
    val pressure: PressurePeriod,
    val visibility: VisibilityPeriod,
    val humidity: HumidityPeriod,
    val weatherDescription: ConditionPeriod
) {
    init {
        requireMatching(
            temperature,
            feelsLike,
            dewPoint,
            pop,
            precipitation,
            uvIndex,
            wind,
            gust,
            pressure,
            visibility,
            humidity,
            weatherDescription
        )
    }
}