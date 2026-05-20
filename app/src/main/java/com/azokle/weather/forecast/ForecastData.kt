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

import com.azokle.weather.pop.Pop
import com.azokle.weather.humidity.Humidity
import com.azokle.weather.precipitation.Rain
import com.azokle.weather.precipitation.Showers
import com.azokle.weather.precipitation.Snow
import com.azokle.weather.pressure.Pressure
import com.azokle.weather.temperature.Temperature
import com.azokle.weather.uvindex.UvIndex
import com.azokle.weather.visibility.Visibility
import com.azokle.weather.wind.WindDirection
import com.azokle.weather.wind.WindSpeed
import java.time.Instant
import java.time.LocalDateTime

class ForecastData(
    val timestamp: Instant,
    val times: List<LocalDateTime>,
    val temperature: List<Temperature>,
    val feelsLikeTemperature: List<Temperature>,
    val dewPointTemperature: List<Temperature>,
    val sunrises: List<LocalDateTime>,
    val sunsets: List<LocalDateTime>,
    val pop: List<Pop>,
    val rain: List<Rain>,
    val showers: List<Showers>,
    val snow: List<Snow>,
    val uvIndex: List<UvIndex>,
    val windSpeed: List<WindSpeed>,
    val windDirection: List<WindDirection>,
    val gustSpeed: List<WindSpeed>,
    val pressure: List<Pressure>,
    val visibility: List<Visibility>,
    val humidity: List<Humidity>,
    val wmoCode: List<Int>,
    val isDay: List<Boolean>,
)