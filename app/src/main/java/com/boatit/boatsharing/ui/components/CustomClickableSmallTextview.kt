@file:Suppress("ktlint:standard:function-naming")

package com.boatit.boatsharing.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomClickableSmallTextview(
    text: String,
    onTextClick: (String) -> Unit,
) {
    Text(
        style =
            TextStyle(
                color = Color.Black,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
            ),
        text = text,
        modifier =
            Modifier.padding(8.dp)
                .clickable { onTextClick(text) },
    )
}
