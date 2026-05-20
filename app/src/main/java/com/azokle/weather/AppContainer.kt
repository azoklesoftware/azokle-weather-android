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

package com.azokle.weather

import android.content.Context
import android.content.SharedPreferences
import com.azokle.weather.common.UserAgentProvider
import com.azokle.weather.condition.ConditionRepository
import com.azokle.weather.condition.EagerConditionRepository
import com.azokle.weather.condition.StaticConditionRepository
import com.azokle.weather.forecast.ForecastConverter
import com.azokle.weather.forecast.ForecastDataCacher
import com.azokle.weather.forecast.ForecastDataDownloader
import com.azokle.weather.forecast.ForecastRepository
import com.azokle.weather.graphs.pop.GetPopGraphs
import com.azokle.weather.graphs.precipitation.GetPrecipitationGraphs
import com.azokle.weather.graphs.precipitation.GetPrecipitationTotals
import com.azokle.weather.graphs.temperature.GetTemperatureGraphSummaries
import com.azokle.weather.graphs.temperature.GetTemperatureGraphs
import com.azokle.weather.gust.EagerGustRepository
import com.azokle.weather.gust.GustRepository
import com.azokle.weather.humidity.EagerHumidityRepository
import com.azokle.weather.humidity.HumidityRepository
import com.azokle.weather.place.saved.DeletePlace
import com.azokle.weather.place.saved.FileSavedPlacesRepository
import com.azokle.weather.place.saved.GetSavedPlaces
import com.azokle.weather.place.saved.SavedPlacesRepository
import com.azokle.weather.place.search.SearchPlaces
import com.azokle.weather.place.selected.PrefsSelectedPlaceRepository
import com.azokle.weather.place.selected.SelectPlace
import com.azokle.weather.place.selected.SelectedPlaceRepository
import com.azokle.weather.pop.EagerPopRepository
import com.azokle.weather.pop.PopRepository
import com.azokle.weather.precipitation.EagerPrecipitationRepository
import com.azokle.weather.precipitation.PrecipitationRepository
import com.azokle.weather.pressure.EagerPressureRepository
import com.azokle.weather.pressure.PressureRepository
import com.azokle.weather.summary.daily.GetDailySummary
import com.azokle.weather.summary.feelslike.GetFeelsLikeSummary
import com.azokle.weather.summary.hourly.GetHourlySummary
import com.azokle.weather.summary.humidity.GetHumiditySummary
import com.azokle.weather.summary.now.GetNowSummary
import com.azokle.weather.summary.precipitation.GetPrecipitationSummary
import com.azokle.weather.summary.pressure.GetPressureSummary
import com.azokle.weather.summary.sun.GetSunSummary
import com.azokle.weather.summary.uvindex.GetUvIndexSummary
import com.azokle.weather.summary.visibility.GetVisibilitySummary
import com.azokle.weather.summary.wind.GetWindSummary
import com.azokle.weather.sun.EagerSunRepository
import com.azokle.weather.sun.SunRepository
import com.azokle.weather.temperature.EagerDewPointRepository
import com.azokle.weather.temperature.EagerFeelsLikeRepository
import com.azokle.weather.temperature.EagerTemperatureRepository
import com.azokle.weather.temperature.StaticTemperatureRepository
import com.azokle.weather.temperature.TemperatureRepository
import com.azokle.weather.units.PrefsSelectedUnitsRepository
import com.azokle.weather.units.SelectedUnitsRepository
import com.azokle.weather.uvindex.EagerUvIndexRepository
import com.azokle.weather.uvindex.UvIndexRepository
import com.azokle.weather.visibility.EagerVisibilityRepository
import com.azokle.weather.visibility.VisibilityRepository
import com.azokle.weather.wind.EagerWindRepository
import com.azokle.weather.wind.WindRepository

class AppContainer(private val appContext: Context) {
    val prefs: SharedPreferences get() = appContext.getSharedPreferences("prefs", Context.MODE_PRIVATE)
    private val root get() = appContext.filesDir
    private val userAgentProvider get() = UserAgentProvider(appContext)

    private val forecastCacher by lazy { ForecastDataCacher(root) }
    private val forecastRepo by lazy {
        ForecastRepository(
            cacher = forecastCacher,
            downloader = ForecastDataDownloader(userAgentProvider),
            converter = ForecastConverter()
        )
    }

    private val tempRepo: TemperatureRepository get() = EagerTemperatureRepository(forecastRepo)
    private val feelsRepo: TemperatureRepository get() = EagerFeelsLikeRepository(forecastRepo)
    private val conditionRepo: ConditionRepository get() = EagerConditionRepository(forecastRepo)
    private val sunRepo: SunRepository get() = EagerSunRepository(forecastRepo)
    private val popRepo: PopRepository get() = EagerPopRepository(forecastRepo)
    private val precipRepo: PrecipitationRepository get() = EagerPrecipitationRepository(forecastRepo)
    private val uvIndexRepo: UvIndexRepository get() = EagerUvIndexRepository(forecastRepo)
    private val windRepo: WindRepository get() = EagerWindRepository(forecastRepo)
    private val gustRepo: GustRepository get() = EagerGustRepository(forecastRepo)
    private val pressureRepo: PressureRepository get() = EagerPressureRepository(forecastRepo)
    private val humidityRepo: HumidityRepository get() = EagerHumidityRepository(forecastRepo)
    private val dewPointRepo: TemperatureRepository get() = EagerDewPointRepository(forecastRepo)
    private val visibilityRepo: VisibilityRepository get() = EagerVisibilityRepository(forecastRepo)

    private val staticTempRepo: TemperatureRepository get() = StaticTemperatureRepository(forecastRepo)
    private val staticConditionRepo: ConditionRepository get() = StaticConditionRepository(forecastRepo)

    val selectedPlaceRepo: SelectedPlaceRepository by lazy { PrefsSelectedPlaceRepository(prefs, savedPlacesRepo) }
    val selectedUnitsRepo: SelectedUnitsRepository by lazy { PrefsSelectedUnitsRepository(prefs) }

    val getNowSummary get() = GetNowSummary(tempRepo, feelsRepo, conditionRepo)
    val getHourlySummary get() = GetHourlySummary(tempRepo, popRepo, conditionRepo, sunRepo)
    val getDailySummary get() = GetDailySummary(tempRepo, conditionRepo, popRepo)
    val getPrecipitationSummary get() = GetPrecipitationSummary(precipRepo)
    val getUvIndexSummary get() = GetUvIndexSummary(uvIndexRepo)
    val getWindSummary get() = GetWindSummary(windRepo, gustRepo)
    val getSunSummary get() = GetSunSummary(sunRepo, conditionRepo)
    val getPressureSummary get() = GetPressureSummary(pressureRepo)
    val getHumiditySummary get() = GetHumiditySummary(humidityRepo, dewPointRepo)
    val getFeelsLikeSummary get() = GetFeelsLikeSummary(tempRepo, feelsRepo)
    val getVisibilitySummary get() = GetVisibilitySummary(visibilityRepo)

    val getTemperatureGraphs get() = GetTemperatureGraphs(tempRepo, conditionRepo)
    val getPopGraphs get() = GetPopGraphs(popRepo)
    val getPrecipitationTotals get() = GetPrecipitationTotals(precipRepo)
    val getTemperatureGraphSummaries get() = GetTemperatureGraphSummaries(tempRepo, conditionRepo, feelsRepo)
    val getPrecipitationGraphs get() = GetPrecipitationGraphs(precipRepo, conditionRepo)

    private val savedPlacesRepo: SavedPlacesRepository by lazy { FileSavedPlacesRepository(root) }
    val getSavedPlaces get() = GetSavedPlaces(savedPlacesRepo, staticTempRepo, staticConditionRepo)
    val searchPlaces get() = SearchPlaces(userAgentProvider)
    val selectPlace get() = SelectPlace(selectedPlaceRepo, savedPlacesRepo)
    val deletePlace get() = DeletePlace(savedPlacesRepo, forecastCacher)
}