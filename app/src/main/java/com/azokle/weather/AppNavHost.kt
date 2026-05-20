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

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.azokle.weather.common.Theme
import com.azokle.weather.graphs.EssentialGraphsDestination
import com.azokle.weather.settings.SettingsDestination
import com.azokle.weather.summary.SummaryDestination
import java.time.LocalDate

@Composable
fun AppNavHost(theme: Theme, onThemeClick: (Theme) -> Unit) {
    val controller = rememberNavController()
    NavHost(navController = controller, startDestination = "summary") {
        composable("summary") {
            SummaryDestination(
                onHourlySectionClick = {
                    controller.navigate("essential-graphs")
                },
                onDayClick = {
                    controller.navigate("essential-graphs?initialDay=$it")
                },
                onSettingsButtonClick = {
                    controller.navigate("settings")
                },
                onPrecipitationClick = {
                    controller.navigate("essential-graphs")
                }
            )
        }
        composable(
            route = "essential-graphs?initialDay={initialDay}",
            arguments = listOf(
                navArgument("initialDay") {
                    nullable = true
                    defaultValue = null
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            EssentialGraphsDestination(
                initialDay = backStackEntry.arguments?.getString("initialDay")?.let(LocalDate::parse),
                onSelectPlaceClick = controller::popBackStack,
                onBackClick = controller::popBackStack
            )
        }
        composable("settings") {
            SettingsDestination(
                theme = theme,
                onThemeClick = onThemeClick,
                onBackClick = controller::popBackStack
            )
        }
    }
}