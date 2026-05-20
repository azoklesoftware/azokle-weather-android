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

import com.azokle.weather.gust.GustMoment
import com.azokle.weather.gust.GustPeriod
import com.azokle.weather.humidity.HumidityMoment
import com.azokle.weather.humidity.HumidityPeriod
import com.azokle.weather.pop.PopMoment
import com.azokle.weather.pop.PopPeriod
import com.azokle.weather.precipitation.PrecipitationMoment
import com.azokle.weather.precipitation.PrecipitationPeriod
import com.azokle.weather.pressure.PressureMoment
import com.azokle.weather.pressure.PressurePeriod
import com.azokle.weather.sun.SunEvent
import com.azokle.weather.sun.SunMoment
import com.azokle.weather.sun.SunPeriod
import com.azokle.weather.temperature.TemperatureMoment
import com.azokle.weather.temperature.TemperaturePeriod
import com.azokle.weather.units.Units
import com.azokle.weather.uvindex.UvIndexMoment
import com.azokle.weather.uvindex.UvIndexPeriod
import com.azokle.weather.visibility.VisibilityMoment
import com.azokle.weather.visibility.VisibilityPeriod
import com.azokle.weather.condition.Condition
import com.azokle.weather.condition.ConditionMoment
import com.azokle.weather.condition.ConditionPeriod
import com.azokle.weather.precipitation.MixedPrecipitation
import com.azokle.weather.wind.Wind
import com.azokle.weather.wind.WindMoment
import com.azokle.weather.wind.WindPeriod
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ForecastConverter {
    suspend fun fromData(data: ForecastData, toUnits: Units): Forecast =
        withContext(Dispatchers.Default) {
            val temperatureMoments = mutableListOf<TemperatureMoment>()
            val feelsLikeMoments = mutableListOf<TemperatureMoment>()
            val dewPointMoments = mutableListOf<TemperatureMoment>()
            val popMoments = mutableListOf<PopMoment>()
            val precipMoments = mutableListOf<PrecipitationMoment>()
            val uvIndexMoments = mutableListOf<UvIndexMoment>()
            val windMoments = mutableListOf<WindMoment>()
            val gustMoments = mutableListOf<GustMoment>()
            val pressureMoments = mutableListOf<PressureMoment>()
            val visibilityMoments = mutableListOf<VisibilityMoment>()
            val humidityMoments = mutableListOf<HumidityMoment>()
            val conditionMoments = mutableListOf<ConditionMoment>()

            for (i in data.times.indices) {
                val time = data.times[i]
                temperatureMoments.add(TemperatureMoment(time, data.temperature[i].convertTo(toUnits.temperature)))
                feelsLikeMoments.add(TemperatureMoment(time, data.feelsLikeTemperature[i].convertTo(toUnits.temperature)))
                dewPointMoments.add(TemperatureMoment(time, data.dewPointTemperature[i].convertTo(toUnits.temperature)))
                popMoments.add(PopMoment(time, data.pop[i]))
                val rain = data.rain[i].convertTo(toUnits.rain)
                val showers = data.showers[i].convertTo(toUnits.showers)
                val snowfall = data.snow[i].convertTo(toUnits.snow)
                precipMoments.add(PrecipitationMoment(time, MixedPrecipitation.fromMillimeters(rain, showers, snowfall).convertTo(toUnits.precipitation)))
                uvIndexMoments.add(UvIndexMoment(time, data.uvIndex[i]))
                windMoments.add(WindMoment(time, Wind(data.windSpeed[i].convertTo(toUnits.windSpeed), data.windDirection[i])))
                gustMoments.add(GustMoment(time, data.gustSpeed[i].convertTo(toUnits.windSpeed)))
                pressureMoments.add(PressureMoment(time, data.pressure[i].convertTo(toUnits.pressure)))
                visibilityMoments.add(VisibilityMoment(time, data.visibility[i].convertTo(toUnits.visibility)))
                humidityMoments.add(HumidityMoment(time, data.humidity[i]))
                conditionMoments.add(ConditionMoment(time, Condition(data.wmoCode[i], data.isDay[i])))
            }

            val temperature = TemperaturePeriod(temperatureMoments)
            val feelsLike = TemperaturePeriod(feelsLikeMoments)
            val dewPoint = TemperaturePeriod(dewPointMoments)
            val pop = PopPeriod(popMoments)
            val precipitation = PrecipitationPeriod(precipMoments)
            val uvIndex = UvIndexPeriod(uvIndexMoments)
            val wind = WindPeriod(windMoments)
            val gust = GustPeriod(gustMoments)
            val pressure = PressurePeriod(pressureMoments)
            val visibility = VisibilityPeriod(visibilityMoments)
            val humidity = HumidityPeriod(humidityMoments)
            val weatherDescription = ConditionPeriod(conditionMoments)

            val sunriseMoments = data.sunrises.map { SunMoment(it, SunEvent.Sunrise) }
            val sunsetMoments = data.sunsets.map { SunMoment(it, SunEvent.Sunset) }
            val sun = (sunriseMoments + sunsetMoments)
                .sortedBy { it.time }
                .takeIf { it.isNotEmpty() }
                ?.let { SunPeriod(it) }

            return@withContext Forecast(
                temperature = temperature,
                feelsLike = feelsLike,
                dewPoint = dewPoint,
                sun = sun,
                pop = pop,
                precipitation = precipitation,
                uvIndex = uvIndex,
                wind = wind,
                gust = gust,
                pressure = pressure,
                visibility = visibility,
                humidity = humidity,
                weatherDescription = weatherDescription
            )
        }
}