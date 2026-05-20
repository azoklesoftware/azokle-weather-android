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
import com.azokle.weather.precipitation.MixedPrecipitation
import com.azokle.weather.precipitation.PrecipitationMoment
import com.azokle.weather.precipitation.PrecipitationPeriod
import com.azokle.weather.precipitation.Rain
import com.azokle.weather.precipitation.Showers
import com.azokle.weather.precipitation.Snow
import com.azokle.weather.summary.precipitation.FuturePrecipitation
import com.azokle.weather.summary.precipitation.PastPrecipitation
import com.azokle.weather.summary.precipitation.PrecipitationSummary
import com.azokle.weather.summary.precipitation.GetPrecipitationSummary
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit

class GetPrecipitationSummaryTest {
    

    private fun dayOfPrecipitation(
        startTime: LocalDateTime,
        millimetersPerHour: Double
    ): List<PrecipitationMoment> =
        List(24) { hour ->
            PrecipitationMoment(
                hour = startTime.plus(hour.toLong(), ChronoUnit.HOURS),
                precipitation = MixedPrecipitation.fromMillimeters(
                    Rain.fromMillimeters(
                        millimetersPerHour
                    ), Showers.Zero, Snow.Zero
                )
            )
        }

    @Test
    fun `when past and future moments exist, past and future are correct`() = runTest {
        val startTime = unixEpochStart
        val period = PrecipitationPeriod(dayOfPrecipitation(startTime, 1.0))
        val middle = startTime.plus(8, ChronoUnit.HOURS).plus(10, ChronoUnit.MINUTES)
        val useCase = GetPrecipitationSummary(FakePrecipitationRepository(period))
        val summary = useCase(coords, units, middle)
        assertEquals(
            ForecastResult.Success(
                PrecipitationSummary(
                    past = PastPrecipitation(
                        inHours = 8,
                        total = Rain.fromMillimeters(8.0)
                    ),
                    future = FuturePrecipitation.InHours(
                        inHours = 16,
                        total = Rain.fromMillimeters(16.0)
                    )
                )
            ),
            summary
        )
    }

    @Test
    fun `when no past moments, summary is outdated`() = runTest {
        val startTime = unixEpochStart
        val period = PrecipitationPeriod(dayOfPrecipitation(startTime, 1.0))
        val start = startTime.plus(10, ChronoUnit.MINUTES)
        val useCase = GetPrecipitationSummary(FakePrecipitationRepository(period))
        val summary = useCase(coords, units, start)
        assertEquals(ForecastResult.Outdated, summary)
    }

    @Test
    fun `when no future moments, summary is outdated`() = runTest {
        val startTime = unixEpochStart
        val period = PrecipitationPeriod(dayOfPrecipitation(startTime, 1.0))
        val end = startTime.plus(24, ChronoUnit.HOURS).plus(10, ChronoUnit.MINUTES)
        val useCase = GetPrecipitationSummary(FakePrecipitationRepository(period))
        val summary = useCase(coords, units, end)
        assertEquals(ForecastResult.Outdated, summary)
    }

    @Test
    fun `when no past or future moments, summary is outdated`() = runTest {
        val startTime = unixEpochStart
        val period = PrecipitationPeriod(dayOfPrecipitation(startTime, 1.0))
        val afterEnd = startTime.plus(3, ChronoUnit.DAYS).plus(10, ChronoUnit.MINUTES)
        val useCase = GetPrecipitationSummary(FakePrecipitationRepository(period))
        val summary = useCase(coords, units, afterEnd)
        assertEquals(ForecastResult.Outdated, summary)
    }

    @Test
    fun `when no precipitation in next 24 hours but on some future day, future describes that day`() =
        runTest {
            val startTime = unixEpochStart
            val period = PrecipitationPeriod(
                moments = buildList {
                    addAll(dayOfPrecipitation(startTime, 0.0))
                    addAll(
                        dayOfPrecipitation(
                            startTime.plus(1, ChronoUnit.DAYS),
                            0.0
                        )
                    )
                    addAll(
                        dayOfPrecipitation(
                            startTime.plus(2, ChronoUnit.DAYS),
                            1.0
                        )
                    )
                }
            )
            val now = startTime.plus(1, ChronoUnit.DAYS).plus(10, ChronoUnit.MINUTES)
            val useCase = GetPrecipitationSummary(FakePrecipitationRepository(period))
            val summary = useCase(coords, units, now)
            assertEquals(
                FuturePrecipitation.OnDay(
                    onDay = Instant.ofEpochSecond(0).plus(2, ChronoUnit.DAYS)
                        .atZone(ZoneId.of("GMT")).toLocalDate(),
                    total = Rain.fromMillimeters(23.0)
                ),
                (summary as ForecastResult.Success).data.future
            )
        }

    @Test
    fun `when no precipitation in sight, future is none expected`() = runTest {
        val startTime = unixEpochStart
        val period = PrecipitationPeriod(
            moments = buildList {
                addAll(dayOfPrecipitation(startTime, 0.0))
                addAll(dayOfPrecipitation(startTime.plus(1, ChronoUnit.DAYS), 0.0))
                addAll(dayOfPrecipitation(startTime.plus(2, ChronoUnit.DAYS), 0.0))
            }
        )
        val now = startTime.plus(1, ChronoUnit.DAYS).plus(10, ChronoUnit.MINUTES)
        val useCase = GetPrecipitationSummary(FakePrecipitationRepository(period))
        val summary = useCase(coords, units, now)
        assertEquals(
            FuturePrecipitation.None(inDays = 1),
            (summary as ForecastResult.Success).data.future
        )
    }

    @Test
    fun `when no precipitation in next 24 hours and no days after, future has 0mm total`() =
        runTest {
            val startTime = unixEpochStart
            val period = PrecipitationPeriod(
                moments = buildList {
                    addAll(dayOfPrecipitation(startTime, 0.0))
                    addAll(
                        dayOfPrecipitation(
                            startTime.plus(1, ChronoUnit.DAYS),
                            0.0
                        )
                    )
                }
            )
            val now = startTime.plus(1, ChronoUnit.DAYS).plus(10, ChronoUnit.MINUTES)
            val useCase = GetPrecipitationSummary(FakePrecipitationRepository(period))
            val summary = useCase(coords, units, now)
            assertEquals(
                FuturePrecipitation.InHours(
                    inHours = 24,
                    total = MixedPrecipitation.fromMillimeters(Rain.Zero, Showers.Zero, Snow.Zero)
                ),
                (summary as ForecastResult.Success).data.future
            )
        }

    @Test
    fun `when precipitation in next 24 hours and after, future prioritizes 24 hours`() = runTest {
        val startTime = unixEpochStart
        val period = PrecipitationPeriod(
            moments = buildList {
                addAll(dayOfPrecipitation(startTime, 0.0))
                addAll(dayOfPrecipitation(startTime.plus(1, ChronoUnit.DAYS), 1.0))
                addAll(dayOfPrecipitation(startTime.plus(2, ChronoUnit.DAYS), 2.0))
            }
        )
        val now = startTime.plus(1, ChronoUnit.DAYS).plus(10, ChronoUnit.MINUTES)
        val useCase = GetPrecipitationSummary(FakePrecipitationRepository(period))
        val summary = useCase(coords, units, now)
        assertEquals(
            FuturePrecipitation.InHours(
                inHours = 24,
                total = Rain.fromMillimeters(24.0)
            ),
            (summary as ForecastResult.Success).data.future
        )
    }
}