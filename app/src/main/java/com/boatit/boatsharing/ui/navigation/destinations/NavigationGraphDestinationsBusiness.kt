package com.boatit.boatsharing.ui.navigation.destinations

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.boatit.boatsharing.features.business.view.BusinessDashboard
import com.boatit.boatsharing.ui.navigation.NavigationManager

fun NavGraphBuilder.registerBusinessDashboardDestinations(navController: NavHostController) {
    composable(NavigationManager.BUSINESS_SCREEN) {
        BusinessDashboard(navController)
    }
}
