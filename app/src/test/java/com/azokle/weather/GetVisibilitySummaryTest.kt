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

import com.azokle.weather.forecast.ForecastResult
import com.azokle.weather.summary.visibility.GetVisibilitySummary
import com.azokle.weather.visibility.Visibility
import com.azokle.weather.visibility.VisibilityMoment
import com.azokle.weather.visibility.VisibilityPeriod
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.temporal.ChronoUnit

class GetVisibilitySummaryTest {
    

    private val repo = FakeVisibilityRepository(
        VisibilityPeriod(
            listOf(
                VisibilityMoment(unixEpochStart, Visibility.fromMeters(1.0)),
                VisibilityMoment(
                    unixEpochStart.plus(1, ChronoUnit.HOURS),
                    Visibility.fromMeters(2.0)
                ),
                VisibilityMoment(
                    unixEpochStart.plus(2, ChronoUnit.HOURS),
                    Visibility.fromMeters(3.0)
                )
            )
        )
    )

    @Test
    fun `gets distance and description of now`() = runTest {
        val now = unixEpochStart.plus(1, ChronoUnit.HOURS).plus(10, ChronoUnit.MINUTES)
        val useCase = GetVisibilitySummary(repo)
        assertEquals(
            Visibility.fromMeters(2.0),
            (useCase(coords, units, now) as ForecastResult.Success).data.now
        )
    }

    @Test
    fun `summary is outdated when no now`() = runTest {
        val now = unixEpochStart.plus(3, ChronoUnit.HOURS).plus(10, ChronoUnit.MINUTES)
        val useCase = GetVisibilitySummary(repo)
        assertEquals(ForecastResult.Outdated, useCase(coords, units, now))
    }
}