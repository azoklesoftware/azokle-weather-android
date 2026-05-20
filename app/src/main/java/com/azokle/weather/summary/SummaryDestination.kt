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

package com.azokle.weather.summary

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import com.azokle.weather.common.rememberAppLocale
import com.azokle.weather.place.picker.PlacePickerViewModel
import java.time.LocalDate

@Composable
fun SummaryDestination(
    onHourlySectionClick: () -> Unit,
    onDayClick: (LocalDate) -> Unit,
    onSettingsButtonClick: () -> Unit,
    onPrecipitationClick: () -> Unit
) {
    val placePickerVM = viewModel<PlacePickerViewModel>(factory = PlacePickerViewModel.Factory)
    val summaryVM = viewModel<SummaryViewModel>(factory = SummaryViewModel.Factory)

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event != Lifecycle.Event.ON_RESUME) return@LifecycleEventObserver
            placePickerVM.getSelectedPlace()
            summaryVM.getSummary()
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    val pickerState = placePickerVM.state.collectAsState().value
    val selectedPlace = pickerState.selectedPlace
    var searchActive by remember(selectedPlace) { mutableStateOf(false) }
    var searchQuery by remember(searchActive, selectedPlace) {
        mutableStateOf(
            if (searchActive) ""
            else selectedPlace?.name ?: ""
        )
    }
    LaunchedEffect(searchActive) {
        if (!searchActive) {
            searchQuery = selectedPlace?.name ?: ""
            summaryVM.getSummary()
        } else {
            placePickerVM.getSavedPlaces()
        }
    }

    val appLocale = rememberAppLocale()

    SummaryScreen(
        summaryState = summaryVM.state.collectAsState().value,
        onHourlySectionClick = onHourlySectionClick,
        onDayClick = onDayClick,
        onSettingsButtonClick = onSettingsButtonClick,
        onPrecipitationClick = onPrecipitationClick,

        pickerState = pickerState,
        searchQuery = searchQuery,
        onSearchQueryChange = { searchQuery = it },
        searchActive = searchActive,
        onSearchActiveChange = { searchActive = it },
        onSearchQueryClearClick = { searchQuery = "" },
        onSearch = { placePickerVM.searchPlaces(query = searchQuery, languageCode = appLocale.language) },
        onPlaceClick = placePickerVM::selectPlace,
        onPlaceDeleteClick = placePickerVM::deletePlace,

        onTryAgainClick = summaryVM::getSummary,
        onSelectPlaceClick = { searchActive = true }
    )
}