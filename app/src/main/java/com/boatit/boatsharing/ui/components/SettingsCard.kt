@file:Suppress("ktlint:standard:function-naming")

package com.boatit.boatsharing.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.boatit.boatsharing.R

@Composable
fun SettingsCard(
    label: String,
    modifier: Modifier = Modifier,
    onArrowClick: () -> Unit,
) {
    Card(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(0.5.dp, color = colorResource(id = R.color.button_normal)),
        shape = RoundedCornerShape(16.dp),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = label,
                style =
                    TextStyle(
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                    ),
            )

            IconButton(onClick = onArrowClick) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_right),
                    contentDescription = "Navigate",
                    tint = colorResource(id = R.color.button_normal),
                )
            }
        }
    }
}
