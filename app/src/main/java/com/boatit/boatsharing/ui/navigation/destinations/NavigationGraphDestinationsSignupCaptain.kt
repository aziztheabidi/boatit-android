package com.boatit.boatsharing.ui.navigation.destinations

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.boatit.boatsharing.features.signup.captain.AddCaptainBoatInfoScreen
import com.boatit.boatsharing.features.signup.captain.AddCaptainDocumentInfoScreen
import com.boatit.boatsharing.features.signup.captain.view.CaptainAccountInfoScreen
import com.boatit.boatsharing.ui.navigation.NavigationManager

fun NavGraphBuilder.registerCaptainSignupDestinations(navController: NavHostController) {
    composable(NavigationManager.CAPTAIN_INFO_SCREEN) {
        CaptainAccountInfoScreen(navController)
    }
    composable(NavigationManager.CAPTAIN_DOCUMENT_INFO_SCREEN) {
        AddCaptainDocumentInfoScreen(navController)
    }
    composable(NavigationManager.CAPTAIN_BOAT_INFO_SCREEN) {
        AddCaptainBoatInfoScreen(navController)
    }
}
