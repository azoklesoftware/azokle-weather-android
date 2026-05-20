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

package com.azokle.weather.place.picker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.azokle.weather.App
import com.azokle.weather.place.Place
import com.azokle.weather.place.saved.DeletePlace
import com.azokle.weather.place.saved.SavedPlace
import com.azokle.weather.place.saved.GetSavedPlaces
import com.azokle.weather.place.search.SearchPlaces
import com.azokle.weather.place.selected.SelectPlace
import com.azokle.weather.place.selected.SelectedPlaceRepository
import com.azokle.weather.units.SelectedUnitsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant

class PlacePickerViewModel(
    private val selectedPlaceRepo: SelectedPlaceRepository,
    private val selectedUnitsRepo: SelectedUnitsRepository,
    private val selectPlace: SelectPlace,
    private val getSavedPlaces: GetSavedPlaces,
    private val searchPlaces: SearchPlaces,
    private val deletePlace: DeletePlace
) : ViewModel() {
    private val _state = MutableStateFlow(
        PlacePickerState(
            loading = false,
            selectedPlace = null,
            results = PlacePickerResults.Initial
        )
    )
    val state get() = _state.asStateFlow()

    fun getSelectedPlace() {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true)
            val place = selectedPlaceRepo.getSelectedPlace()
            _state.value = _state.value.copy(
                loading = false,
                selectedPlace = place
            )
        }
    }

    fun selectPlace(place: Place) {
        viewModelScope.launch {
            selectPlace.invoke(place)
            _state.value = _state.value.copy(
                loading = false,
                selectedPlace = place
            )
        }
    }

    fun getSavedPlaces() {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true)
            _state.value = _state.value.copy(loading = false, results = getSavedPlacesResults())
        }
    }

    fun searchPlaces(query: String, languageCode: String) {
        val trimmedQuery = query.trim()
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true)
            val results = searchPlaces.invoke(trimmedQuery, languageCode)
            _state.value = _state.value.copy(
                loading = false,
                results = PlacePickerResults.SearchedPlaces(trimmedQuery, results)
            )
        }
    }

    fun deletePlace(place: Place) {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true)
            deletePlace.invoke(place)
            _state.value = _state.value.copy(
                loading = false,
                results = getSavedPlacesResults(),
                selectedPlace = selectedPlaceRepo.getSelectedPlace()
            )
        }
    }

    private suspend fun getSavedPlacesResults(): PlacePickerResults.SavedPlaces {
        val selectedUnits = selectedUnitsRepo.getSelectedUnits()
        val selectedPlace = selectedPlaceRepo.getSelectedPlace()
        val places = getSavedPlaces.invoke(
            selectedPlace = selectedPlace,
            selectedUnits = selectedUnits,
            now = Instant.now()
        )
        return PlacePickerResults.SavedPlaces(places)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val container = (checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]) as App).container
                return PlacePickerViewModel(
                    container.selectedPlaceRepo,
                    container.selectedUnitsRepo,
                    container.selectPlace,
                    container.getSavedPlaces,
                    container.searchPlaces,
                    container.deletePlace
                ) as T
            }
        }
    }
}

data class PlacePickerState(
    val loading: Boolean,
    val selectedPlace: Place?,
    val results: PlacePickerResults
)

sealed interface PlacePickerResults {
    data object Initial : PlacePickerResults
    data class SavedPlaces(val places: List<SavedPlace>) : PlacePickerResults
    data class SearchedPlaces(val query: String, val places: List<Place>?) : PlacePickerResults
}