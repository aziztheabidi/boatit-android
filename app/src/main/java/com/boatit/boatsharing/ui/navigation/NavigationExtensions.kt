package com.boatit.boatsharing.ui.navigation

import androidx.navigation.NavController

/**
 * Cross-screen navigation helpers. Use these (and route builders like
 * [AccountRoutes], [VoyagerFlowRoutes], [InteractionRoutes]) instead of raw
 * route strings so behavior stays aligned with [AppNavGraph].
 */
fun NavController.navigateToMapPicker() {
    navigate(AppRoutes.Core.MAP_PICKER)
}

fun NavController.navigateToVoyagerDashboard(navArg: String? = null) {
    navigate(VoyagerFlowRoutes.dashboard(navArg))
}

fun NavController.navigateWithClearStack(
    route: String,
    clearStack: Boolean,
) {
    navigate(route) {
        if (clearStack) {
            popUpTo(graph.startDestinationId) {
                inclusive = true
            }
        }
        launchSingleTop = true
    }
}

fun NavController.popBack() {
    popBackStack()
}
