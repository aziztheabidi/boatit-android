package com.boatit.boatsharing.application

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.boatit.boatsharing.application.viewmodel.MainActivityUiEvent
import com.boatit.boatsharing.application.viewmodel.MainActivityViewModel
import com.boatit.boatsharing.ui.navigation.AppNavGraph
import com.boatit.boatsharing.features.login.viewmodel.NotificationViewModel
import com.boatit.boatsharing.ui.theme.BoatSharingAppTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    private val notificationViewModel: NotificationViewModel by inject()
    private val activityViewModel: MainActivityViewModel by viewModels()
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            activityViewModel.onEvent(MainActivityUiEvent.PermissionResult(isGranted))
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (activityViewModel.shouldRequestNotificationPermission()) {
            requestNotificationPermission()
        }

        enableEdgeToEdge()
        setContent {
            BoatSharingAppTheme {
                val navController = rememberNavController()
                Box(
                    modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.systemBars), // Handle system bars padding
                ) {
                    AppNavGraph(navController = navController)
                }
            }
        }
    }

    @SuppressLint("InlinedApi")
    private fun requestNotificationPermission() {
        requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
    }
}
