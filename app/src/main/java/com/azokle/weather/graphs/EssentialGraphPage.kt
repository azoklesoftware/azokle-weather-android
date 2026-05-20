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

package com.azokle.weather.graphs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.azokle.weather.R
import com.azokle.weather.common.TextSkeleton
import com.azokle.weather.graphs.common.GraphArgs
import com.azokle.weather.graphs.common.GraphScreenSectionLabel
import com.azokle.weather.graphs.pop.PopGraph
import com.azokle.weather.graphs.precipitation.PrecipitationBullets
import com.azokle.weather.graphs.precipitation.PrecipitationGraph
import com.azokle.weather.graphs.precipitation.TodayPrecipitationBullets
import com.azokle.weather.graphs.precipitation.PrecipitationTotal
import com.azokle.weather.graphs.temperature.TemperatureGraph
import com.azokle.weather.graphs.temperature.TemperatureGraphSummary
import com.azokle.weather.precipitation.MixedPrecipitation
import com.azokle.weather.summary.now.NowSummarySkeleton
import com.azokle.weather.temperature.Temperature

private const val graphAspectRatio = 4f / 3f
private val contentPadding = PaddingValues(horizontal = 16.dp, vertical = 24.dp)
private val verticalSpacing = 24.dp
private val graphLabelSpacing = 8.dp

@Composable
fun EssentialGraphPage(
    summary: TemperatureGraphSummary,
    temperatureGraph: TemperatureGraph,
    minTemp: Temperature,
    maxTemp: Temperature,
    temperatureArgs: GraphArgs,
    popGraph: PopGraph,
    popArgs: GraphArgs,
    precipGraph: PrecipitationGraph,
    precipArgs: GraphArgs,
    precipMax: MixedPrecipitation,
    precipitationTotal: PrecipitationTotal
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(verticalSpacing),
        contentPadding = contentPadding,
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            TemperatureGraphSummary(
                state = summary,
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            TemperatureGraph(
                state = temperatureGraph,
                absMinTemp = minTemp,
                absMaxTemp = maxTemp,
                args = temperatureArgs,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(graphAspectRatio)
                    .border(
                        width = Dp.Hairline,
                        shape = MaterialTheme.shapes.large,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    .clip(MaterialTheme.shapes.large)
            )
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(graphLabelSpacing)) {
                GraphScreenSectionLabel(stringResource(R.string.cond_screen_pop))
                PopGraph(
                    state = popGraph,
                    args = popArgs,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(graphAspectRatio)
                        .border(
                            width = Dp.Hairline,
                            shape = MaterialTheme.shapes.large,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        .clip(MaterialTheme.shapes.large)
                )
            }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(graphLabelSpacing)) {
                GraphScreenSectionLabel(stringResource(R.string.cond_screen_precip))
                PrecipitationGraph(
                    state = precipGraph,
                    max = precipMax,
                    args = precipArgs,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(graphAspectRatio)
                        .border(
                            width = Dp.Hairline,
                            shape = MaterialTheme.shapes.large,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        .clip(MaterialTheme.shapes.large)
                )
            }
        }
        item {
            when (precipitationTotal) {
                is PrecipitationTotal.OtherDay -> PrecipitationBullets(
                    state = precipitationTotal.total,
                    modifier = Modifier.fillMaxWidth()
                )

                is PrecipitationTotal.Today -> TodayPrecipitationBullets(
                    state = precipitationTotal,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        item {
            Text(
                text = stringResource(id = R.string.credit_weather),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun EssentialGraphPageLoadingIndicator(shimmerColor: State<Color>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(verticalSpacing),
        userScrollEnabled = false,
        contentPadding = contentPadding
    ) {
        item {
            NowSummarySkeleton(
                color = shimmerColor,
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(graphAspectRatio)
                    .background(shape = MaterialTheme.shapes.large, color = shimmerColor.value),
            )
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(graphLabelSpacing)) {
                TextSkeleton(
                    color = shimmerColor,
                    shape = MaterialTheme.shapes.small,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.width(160.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(graphAspectRatio)
                        .background(shape = MaterialTheme.shapes.large, color = shimmerColor.value),
                )
            }
        }
    }
}