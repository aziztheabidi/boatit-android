package com.boatit.boatsharing.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.boatit.boatsharing.ui.navigation.destinations.registerAuthDestinations
import com.boatit.boatsharing.ui.navigation.destinations.registerBusinessDashboardDestinations
import com.boatit.boatsharing.ui.navigation.destinations.registerBusinessSignupDestinations
import com.boatit.boatsharing.ui.navigation.destinations.registerCaptainFlowDestinations
import com.boatit.boatsharing.ui.navigation.destinations.registerCaptainSignupDestinations
import com.boatit.boatsharing.ui.navigation.destinations.registerChatDestinations
import com.boatit.boatsharing.ui.navigation.destinations.registerCoreFlowDestinations
import com.boatit.boatsharing.ui.navigation.destinations.registerVoyagerFlowDestinations
import com.google.accompanist.pager.ExperimentalPagerApi

/**
 * Root navigation graph. Destination registrars live in
 * `com.boatit.boatsharing.ui.navigation.destinations` as [androidx.navigation.NavGraphBuilder] extensions.
 *
 * Registration order follows a rough product flow (readability only; route matching is unordered):
 * entry and shared chrome, then auth, sign-up flows, role dashboards, voyager and captain flows, shared chat.
 */
@OptIn(ExperimentalPagerApi::class)
@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = NavigationManager.SPLASH_SCREEN) {
        registerCoreFlowDestinations(navController)
        registerAuthDestinations(navController)
        registerCaptainSignupDestinations(navController)
        registerBusinessSignupDestinations(navController)
        registerBusinessDashboardDestinations(navController)
        registerVoyagerFlowDestinations(navController)
        registerCaptainFlowDestinations(navController)
        registerChatDestinations(navController)
    }
}
