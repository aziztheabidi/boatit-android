package com.boatit.boatsharing.uihelpers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FormStepsViews(
    numberOfViews: Int,
    activeColor: Color,
    inactiveColor: Color,
    activeViewsCount: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(numberOfViews) { index ->
            Box(
                modifier = Modifier.background(color = if (index < activeViewsCount) activeColor else inactiveColor)
                    .width(20.dp)
                    .height(3.dp)
                    .clip(RoundedCornerShape(70))
            )
            if (index < numberOfViews - 1) {
                Spacer(modifier = Modifier.width(5.dp))
            }
        }
    }
}
