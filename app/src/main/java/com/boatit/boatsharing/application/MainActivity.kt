package com.boatit.boatsharing.application

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.boatit.boatsharing.routes.AppNavGraph
import com.boatit.boatsharing.utils.theme.BoatSharingAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

     enableEdgeToEdge()
        setContent {

            BoatSharingAppTheme {
                val navController = rememberNavController()
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(WindowInsets.systemBars) // Handle system bars padding
                ) {
                    AppNavGraph(navController = navController)
                }
            }
        }
    }
}
