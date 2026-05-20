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

package com.azokle.weather.place.selected

import android.content.SharedPreferences
import androidx.core.content.edit
import com.azokle.weather.place.Coordinates
import com.azokle.weather.place.Place
import com.azokle.weather.place.saved.SavedPlacesRepository

private const val SELECTED_PLACE_KEY = "selected_place_coords"

class PrefsSelectedPlaceRepository(
    private val prefs: SharedPreferences,
    private val savedPlacesRepository: SavedPlacesRepository
) : SelectedPlaceRepository {
    override suspend fun selectPlace(place: Place) =
        prefs.edit { putString(SELECTED_PLACE_KEY, place.location.coordinates.id) }

    override suspend fun getSelectedPlace(): Place? {
        val coords = prefs.getString(SELECTED_PLACE_KEY, null)?.let(Coordinates::fromId) ?: return null
        return savedPlacesRepository.getSavedPlace(coords)
    }
}