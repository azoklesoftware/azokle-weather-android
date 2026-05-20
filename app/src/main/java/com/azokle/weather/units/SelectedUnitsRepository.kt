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

package com.azokle.weather.units

import com.azokle.weather.precipitation.Precipitation
import com.azokle.weather.pressure.Pressure
import com.azokle.weather.temperature.Temperature
import com.azokle.weather.visibility.Visibility
import com.azokle.weather.wind.WindSpeed

interface SelectedUnitsRepository {
    suspend fun getSelectedUnits(): Units
    suspend fun selectRainUnit(unit: Precipitation.Unit)
    suspend fun selectShowersUnit(unit: Precipitation.Unit)
    suspend fun selectSnowUnit(unit: Precipitation.Unit)
    suspend fun selectMixedPrecipitationUnit(unit: Precipitation.Unit)
    suspend fun selectTemperatureUnit(unit: Temperature.Unit)
    suspend fun selectPressureUnit(unit: Pressure.Unit)
    suspend fun selectVisibilityUnit(unit: Visibility.Unit)
    suspend fun selectWindSpeedUnit(unit: WindSpeed.Unit)
}