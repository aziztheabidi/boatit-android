package com.boatit.boatsharing.ui.navigation.destinations

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.boatit.boatsharing.features.captain.availabilitystatus.CaptainFeedbackScreen
import com.boatit.boatsharing.features.captain.availabilitystatus.CustomStatusScreen
import com.boatit.boatsharing.features.captain.availabilitystatus.VoyageStartedScreen
import com.boatit.boatsharing.features.captain.dashboard.view.CaptainDashboard
import com.boatit.boatsharing.features.captain.voyages.view.CaptainVoyages
import com.boatit.boatsharing.features.chat.view.CaptainCurrentVoyages
import com.boatit.boatsharing.ui.navigation.InteractionRoutes
import com.boatit.boatsharing.ui.navigation.NavigationManager
import com.boatit.boatsharing.ui.navigation.optDecodedStringArg

fun NavGraphBuilder.registerCaptainFlowDestinations(navController: NavHostController) {
    composable(NavigationManager.CAPTAIN_DASHBOARD_SCREEN) {
        CaptainDashboard(navController)
    }

    composable(NavigationManager.CAPTAIN_VOYAGES_SCREEN) {
        CaptainVoyages(navController)
    }

    composable(NavigationManager.VOYAGE_STARTED_SCREEN) {
        VoyageStartedScreen(navController)
    }

    composable(NavigationManager.CAPTAIN_CURRENT_VOYAGES_SCREEN) {
        CaptainCurrentVoyages(navController)
    }

    composable(NavigationManager.CAPTAIN_OFFLINE_SCREEN) {
        CustomStatusScreen(navController)
    }

    composable(InteractionRoutes.captainFeedbackPattern) { backStackEntry ->
        val data = backStackEntry.optDecodedStringArg(InteractionRoutes.FEEDBACK_VOYAGE_ID_ARG).orEmpty()
        CaptainFeedbackScreen(navController, data)
    }
}
