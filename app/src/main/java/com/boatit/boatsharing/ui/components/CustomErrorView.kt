@file:Suppress(
    "ktlint:standard:function-naming",
    "ktlint:standard:no-consecutive-blank-lines",
)

package com.boatit.boatsharing.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.boatit.boatsharing.R

@Composable
fun CustomErrorView(text: String) {
    Spacer(modifier = Modifier.height(10.dp))
    Row(verticalAlignment = Alignment.Top) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.info_error),
            contentDescription = "Error Icon",
            tint = Color.Red,
            modifier = Modifier.size(24.dp),
        )
        Spacer(modifier = Modifier.width(4.dp))

        Text(
            style =
                TextStyle(
                    color = Color.Red,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                ),
            textAlign = TextAlign.Start,
            text = text,
        )
    }
}

