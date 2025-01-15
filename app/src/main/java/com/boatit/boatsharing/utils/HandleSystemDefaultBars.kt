package com.boatit.boatsharing.utils

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun HandleSystemDefaultBars(
    statusBarColor: Color = MaterialTheme.colorScheme.primary,
    navigationBarColor: Color = MaterialTheme.colorScheme.background,
    isDarkIcons: Boolean = false
) {
    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = isDarkIcons
        )
        systemUiController.setNavigationBarColor(
            color = navigationBarColor,
            darkIcons = isDarkIcons
        )
    }
}

