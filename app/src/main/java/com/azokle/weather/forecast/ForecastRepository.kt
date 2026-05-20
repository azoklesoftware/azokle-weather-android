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

import com.azokle.weather.place.Coordinates
import com.azokle.weather.units.Units
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.time.Duration
import java.time.Instant

class ForecastRepository(
    private val cacher: ForecastDataCacher,
    private val downloader: ForecastDataDownloader,
    private val converter: ForecastConverter
) {
    private val coordsToMutex = mutableMapOf<Coordinates, Mutex>()

    suspend fun forecast(
        coords: Coordinates,
        units: Units,
        updatePolicy: UpdatePolicy = UpdatePolicy.Eager
    ): Forecast? {
        var data: ForecastData?
        coordsToMutex.getOrPut(coords, defaultValue = { Mutex() }).withLock {
            val cached = cacher.get(coords)
            data = if (shouldUpdate(cached, updatePolicy)) {
                val newData = downloader.downloadForecast(coords)
                if (newData == null) cached else {
                    cacher.save(coords, newData)
                    newData
                }
            } else {
                cached
            }
        }
        return data?.let { converter.fromData(it, units) }
    }

    private fun shouldUpdate(data: ForecastData?, updatePolicy: UpdatePolicy): Boolean =
        if (updatePolicy == UpdatePolicy.Static) false
        else data == null || Duration.between(
            data.timestamp,
            Instant.now()
        ) >= Duration.ofHours(if (updatePolicy == UpdatePolicy.Eager) 1 else 6)
}

enum class UpdatePolicy {
    Eager, Frugal, Static
}