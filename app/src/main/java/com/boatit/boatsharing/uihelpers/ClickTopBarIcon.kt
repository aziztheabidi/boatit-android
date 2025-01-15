package com.boatit.boatsharing.uihelpers

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun ClickTopBarIcon(imageResId: Int, onClick: () -> Unit) {
    Image(
        painter = painterResource(id = imageResId),
        contentDescription = "Icon Image",
        modifier = Modifier
            .size(width = 24.dp, height = 18.dp)
            .clickable { onClick() }
    )
}
