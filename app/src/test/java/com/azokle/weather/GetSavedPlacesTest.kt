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

import com.azokle.weather.condition.Condition
import com.azokle.weather.condition.ConditionMoment
import com.azokle.weather.condition.ConditionPeriod
import com.azokle.weather.place.Coordinates
import com.azokle.weather.place.Location
import com.azokle.weather.place.saved.GetSavedPlaces
import com.azokle.weather.place.Place
import com.azokle.weather.place.saved.SavedPlace
import com.azokle.weather.temperature.Temperature
import com.azokle.weather.temperature.TemperatureMoment
import com.azokle.weather.temperature.TemperaturePeriod
import com.azokle.weather.units.Units
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit

class GetSavedPlacesTest {
    @Test
    fun `gets saved places with conditions attached if available`() = runTest {
        val momentInstant = Instant.ofEpochSecond(0)
        val momentDateTime = momentInstant.atZone(ZoneOffset.UTC).toLocalDateTime()
        val now = momentInstant.plus(10, ChronoUnit.MINUTES)
        val firstPlace = Place(
            name = "first", "", "", "",
            Location(ZoneId.of("GMT"), Coordinates(latitude = 0.0, longitude = 1.0))
        )
        val secondPlace = Place(
            name = "second", "", "", "",
            Location(ZoneId.of("GMT+1"), Coordinates(latitude = 0.0, longitude = 10.0))
        )
        val tempRepo = FakeMultipleCoordsTemperatureRepository(
            mapOf(
                firstPlace.location.coordinates to TemperaturePeriod(
                    listOf(
                        TemperatureMoment(
                            hour = momentDateTime,
                            temperature = Temperature.fromDegreesCelsius(10.0)
                        )
                    )
                ),
                secondPlace.location.coordinates to null
            )
        )
        val condRepo = FakeMultipleCoordsConditionRepository(
            mapOf(
                firstPlace.location.coordinates to ConditionPeriod(
                    listOf(
                        ConditionMoment(
                            hour = momentDateTime,
                            condition = Condition(0, true)
                        )
                    )
                ),
                secondPlace.location.coordinates to null
            )
        )
        val savedPlacesRepo = FakeSavedPlacesRepository(listOf(firstPlace, secondPlace))
        val useCase = GetSavedPlaces(savedPlacesRepo, tempRepo, condRepo)
        val result = useCase.invoke(secondPlace, Units.Default, now)
        assertEquals(
            listOf(
                SavedPlace(
                    place = firstPlace,
                    time = LocalTime.parse("00:10"),
                    selected = false,
                    conditions = SavedPlace.Conditions(
                        temp = Temperature.fromDegreesCelsius(10.0),
                        minTemp = Temperature.fromDegreesCelsius(10.0),
                        maxTemp = Temperature.fromDegreesCelsius(10.0),
                        condition = Condition(0, true)
                    )
                ),
                SavedPlace(
                    place = secondPlace,
                    time = LocalTime.parse("01:10"),
                    selected = true,
                    conditions = null,
                ),
            ),
            result
        )
    }
}