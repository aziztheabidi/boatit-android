@file:Suppress("ktlint:standard:function-naming")

package com.boatit.boatsharing.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.boatit.boatsharing.R

@Composable
fun CustomTopBar(
    text: String,
    onImageClick: () -> Unit,
) {
    Box(
        modifier =
            Modifier
                .fillMaxWidth().wrapContentHeight()
                .padding(0.dp),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth().height(60.dp).padding(start = 20.dp, end = 20.dp)
                    .align(Alignment.BottomStart),
            verticalAlignment = Alignment.Bottom,
        ) {
            ClickTopBarIcon(
                imageResId = R.drawable.arrow_back,
                onClick = {
                    onImageClick()
                },
            )

            Text(
                style =
                    TextStyle(
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                    ),
                text = text,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.width(48.dp))
        }
    }
}
