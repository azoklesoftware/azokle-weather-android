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

package com.azokle.weather.pop

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.azokle.weather.R
import com.azokle.weather.common.rememberNumberFormat
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat

fun Pop.string(context: Context, numberFormat: NumberFormat): String = context.getString(
    R.string.pop_value_percent,
    numberFormat.format(BigDecimal.valueOf(value).setScale(0, RoundingMode.HALF_UP))
)

@Composable
fun Pop.string(): String = string(LocalContext.current, rememberNumberFormat())