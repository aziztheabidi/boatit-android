@file:Suppress(
    "ktlint:standard:function-naming",
    "ktlint:standard:no-wildcard-imports",
)

package com.boatit.boatsharing.features.userroles

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.boatit.boatsharing.R

@Composable
fun RoleCard(
    imageResId: Int,
    size: Dp,
    firstText: String,
    secondText: String,
    onClick: () -> Unit,
) {
    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable { onClick() },
        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 5.dp,
            ),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, color = colorResource(id = R.color.button_normal)),
        shape = RoundedCornerShape(10.dp),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                style =
                    TextStyle(
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                    ),
                text = firstText,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                style =
                    TextStyle(
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Light,
                    ),
                text = secondText,
            )
            Spacer(modifier = Modifier.height(15.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = "Role Image",
                    modifier =
                        Modifier
                            .size(size)
                            .padding(bottom = 8.dp),
                    contentScale = ContentScale.Fit,
                )
            }
        }
    }
}
