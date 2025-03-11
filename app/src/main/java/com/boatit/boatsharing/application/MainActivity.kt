package com.boatit.boatsharing.application

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.boatit.boatsharing.routes.AppNavGraph
import com.boatit.boatsharing.utils.theme.BoatSharingAppTheme
import android.Manifest
import android.annotation.SuppressLint
import androidx.lifecycle.lifecycleScope
import com.boatit.boatsharing.ui.login.viewmodel.NotificationViewModel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    private val viewModel: NotificationViewModel by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestNotificationPermission()
        }

         enableEdgeToEdge()
            setContent {
                BoatSharingAppTheme {
                    val navController = rememberNavController()
                    Box(
                        modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.systemBars) // Handle system bars padding
                    ) {
                        AppNavGraph(navController = navController)
                    }
                }
        }
    }

    @SuppressLint("InlinedApi")
    private fun requestNotificationPermission() {
        val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (!isGranted) {
                    // Handle case when permission is denied (Show explanation UI if needed)
                }
            }
        requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
    }
}
