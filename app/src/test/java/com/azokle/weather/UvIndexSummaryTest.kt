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
import com.azokle.weather.summary.uvindex.UseProtection
import com.azokle.weather.summary.uvindex.GetUvIndexSummary
import com.azokle.weather.uvindex.UvIndex
import com.azokle.weather.uvindex.UvIndexMoment
import com.azokle.weather.uvindex.UvIndexPeriod
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.temporal.ChronoUnit

private val dangerous = UvIndex(3)
private val safe = UvIndex(2)

class UvIndexSummaryTest {
    

    @Test
    fun `gets now`() = runTest {
        val firstMoment = unixEpochStart
        val now = firstMoment.plus(10, ChronoUnit.MINUTES)
        val period = UvIndexPeriod(listOf(UvIndexMoment(firstMoment, UvIndex(0))))
        val useCase = GetUvIndexSummary(FakeUvIndexRepository(period))
        assertEquals(
            UvIndex(0),
            (useCase(coords, units, now) as ForecastResult.Success).data.now
        )
    }

    @Test
    fun `no next window when no dangerous periods today`() = runTest {
        val firstMoment = unixEpochStart
        val now = firstMoment.plus(10, ChronoUnit.MINUTES)
        val period = UvIndexPeriod(listOf(UvIndexMoment(firstMoment, safe)))
        val useCase = GetUvIndexSummary(FakeUvIndexRepository(period))
        assertEquals(
            UseProtection.None,
            (useCase(coords, units, now) as ForecastResult.Success).data.useProtection
        )
    }

    @Test
    fun `window when danger starts and ends later today`() = runTest {
        val startTime = unixEpochStart
        val now = startTime.plus(10, ChronoUnit.MINUTES)
        val firstSafe = startTime
        val firstDanger = startTime.plus(1, ChronoUnit.HOURS)
        val secondSafe = firstDanger.plus(1, ChronoUnit.HOURS)
        val period = UvIndexPeriod(
            moments = listOf(
                UvIndexMoment(firstSafe, safe),
                UvIndexMoment(firstDanger, dangerous),
                UvIndexMoment(secondSafe, safe),
            )
        )
        val useCase = GetUvIndexSummary(FakeUvIndexRepository(period))
        assertEquals(
            UseProtection.FromUntil(
                firstDanger.toLocalTime(),
                secondSafe.toLocalTime()
            ),
            (useCase(coords, units, now) as ForecastResult.Success).data.useProtection
        )
    }

    @Test
    fun `window when danger starts now and ends later today`() = runTest {
        val firstDanger = unixEpochStart
        val now = firstDanger.plus(10, ChronoUnit.MINUTES)
        val firstSafe = firstDanger.plus(1, ChronoUnit.HOURS)
        val period = UvIndexPeriod(
            moments = listOf(
                UvIndexMoment(firstDanger, dangerous),
                UvIndexMoment(firstSafe, safe),
            )
        )
        val useCase = GetUvIndexSummary(FakeUvIndexRepository(period))
        assertEquals(
            UseProtection.Until(firstSafe.toLocalTime()),
            (useCase(coords, units, now) as ForecastResult.Success).data.useProtection
        )
    }

    @Test
    fun `window when danger started earlier and ends later today`() = runTest {
        val startTime = unixEpochStart
        val firstDanger = startTime.plus(1, ChronoUnit.HOURS)
        val secondDanger = firstDanger.plus(1, ChronoUnit.HOURS)
        val now = secondDanger.plus(10, ChronoUnit.MINUTES)
        val firstSafe = secondDanger.plus(1, ChronoUnit.HOURS)
        val period = UvIndexPeriod(
            moments = listOf(
                UvIndexMoment(firstDanger, dangerous),
                UvIndexMoment(secondDanger, dangerous),
                UvIndexMoment(firstSafe, safe),
            )
        )
        val useCase = GetUvIndexSummary(FakeUvIndexRepository(period))
        assertEquals(
            UseProtection.Until(firstSafe.toLocalTime()),
            (useCase(coords, units, now) as ForecastResult.Success).data.useProtection
        )
    }

    @Test
    fun `window when danger started earlier and does not end`() = runTest {
        val startTime = unixEpochStart
        val firstDanger = startTime.plus(1, ChronoUnit.HOURS)
        val secondDanger = firstDanger.plus(1, ChronoUnit.HOURS)
        val now = secondDanger.plus(10, ChronoUnit.MINUTES)
        val thirdDanger = secondDanger.plus(1, ChronoUnit.HOURS)
        val period = UvIndexPeriod(
            moments = listOf(
                UvIndexMoment(firstDanger, dangerous),
                UvIndexMoment(secondDanger, dangerous),
                UvIndexMoment(thirdDanger, dangerous),
            )
        )
        val useCase = GetUvIndexSummary(FakeUvIndexRepository(period))
        assertEquals(
            UseProtection.UntilEndOfDay,
            (useCase(coords, units, now) as ForecastResult.Success).data.useProtection
        )
    }

    @Test
    fun `next window is resistant to multiple future windows`() = runTest {
        val startTime = unixEpochStart
        val firstDanger = startTime.plus(1, ChronoUnit.HOURS)
        val secondDanger = firstDanger.plus(1, ChronoUnit.HOURS)
        val now = secondDanger.plus(10, ChronoUnit.MINUTES)
        val firstSafe = secondDanger.plus(1, ChronoUnit.HOURS)
        val thirdDanger = firstSafe.plus(1, ChronoUnit.HOURS)
        val secondSafe = thirdDanger.plus(1, ChronoUnit.HOURS)
        val period = UvIndexPeriod(
            moments = listOf(
                UvIndexMoment(firstDanger, dangerous),
                UvIndexMoment(secondDanger, dangerous),
                UvIndexMoment(firstSafe, safe),
                UvIndexMoment(thirdDanger, dangerous),
                UvIndexMoment(secondSafe, safe),
            )
        )
        val useCase = GetUvIndexSummary(FakeUvIndexRepository(period))
        assertEquals(
            UseProtection.Until(firstSafe.toLocalTime()),
            (useCase(coords, units, now) as ForecastResult.Success).data.useProtection
        )
    }

    @Test
    fun `outdated when no moments from now`() = runTest {
        val firstMoment = unixEpochStart
        val afterFirstMoment = firstMoment.plus(1, ChronoUnit.HOURS)
        val now = afterFirstMoment.plus(10, ChronoUnit.MINUTES)
        val period = UvIndexPeriod(listOf(UvIndexMoment(firstMoment, safe)))
        val useCase = GetUvIndexSummary(FakeUvIndexRepository(period))
        assertEquals(ForecastResult.Outdated, useCase(coords, units, now))
    }
}