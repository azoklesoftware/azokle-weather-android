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

package com.azokle.weather.common

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.azokle.weather.R

@Composable
fun HighLowText(
    high: String,
    low: String,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    color: Color = LocalContentColor.current
) {
    val inlineContentMap = mapOf(
        "high" to InlineTextContent(
            placeholder = Placeholder(
                width = style.fontSize,
                height = style.fontSize,
                placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter
            )
        ) {
            Icon(
                painter = painterResource(id = R.drawable.arrow_up),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                tint = color
            )
        },
        "low" to InlineTextContent(
            placeholder = Placeholder(
                width = style.fontSize,
                height = style.fontSize,
                placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter
            )
        ) {
            Icon(
                painter = painterResource(id = R.drawable.arrow_up),
                contentDescription = null,
                modifier = Modifier.fillMaxSize().rotate(180f),
                tint = color
            )
        },
    )
    val annotatedString = buildAnnotatedString {
        withStyle(style.toSpanStyle()) {
            appendInlineContent(id = "high")
            append(high)
            append(" ")
            appendInlineContent(id = "low")
            append(low)
        }
    }
    Text(
        text = annotatedString,
        inlineContent = inlineContentMap,
        color = color,
        modifier = modifier
    )
}
