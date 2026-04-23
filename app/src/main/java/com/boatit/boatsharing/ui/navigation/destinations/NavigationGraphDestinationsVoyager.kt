package com.boatit.boatsharing.ui.navigation.destinations

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.boatit.boatsharing.features.captain.availabilitystatus.VoyageBookedScreenVoyager
import com.boatit.boatsharing.features.captain.availabilitystatus.VoyageStartedScreenVoyager
import com.boatit.boatsharing.features.captain.availabilitystatus.VoyagerFeedbackScreen
import com.boatit.boatsharing.features.chat.view.VoyagersListScreen
import com.boatit.boatsharing.features.voyager.dashboard.view.BusinessDetail
import com.boatit.boatsharing.features.voyager.dashboard.view.BusinessListScreen
import com.boatit.boatsharing.features.voyager.dashboard.view.ConfirmVoyageScreen
import com.boatit.boatsharing.features.voyager.dashboard.view.CreateVoyageRateCalcScreen
import com.boatit.boatsharing.features.voyager.dashboard.view.CreateVoyageScreen
import com.boatit.boatsharing.features.voyager.dashboard.view.CreateVoyageSponsorScreen
import com.boatit.boatsharing.features.voyager.dashboard.view.DashboardScreen
import com.boatit.boatsharing.features.voyager.dashboard.view.FutureVoyages
import com.boatit.boatsharing.features.voyager.dashboard.view.SponsorList
import com.boatit.boatsharing.features.voyager.dashboard.view.SponsorScreen
import com.boatit.boatsharing.features.voyager.dashboard.view.TravelNow
import com.boatit.boatsharing.features.voyager.dashboard.view.VoyagerVoyages
import com.boatit.boatsharing.ui.navigation.InteractionRoutes
import com.boatit.boatsharing.ui.navigation.NavigationManager
import com.boatit.boatsharing.ui.navigation.VoyagerFlowRoutes
import com.boatit.boatsharing.ui.navigation.optBooleanArg
import com.boatit.boatsharing.ui.navigation.optDecodedStringArg
import com.boatit.boatsharing.ui.navigation.optStringArg

fun NavGraphBuilder.registerVoyagerFlowDestinations(navController: NavHostController) {
    composable(NavigationManager.BUSINESS_DETAIL_SCREEN) {
        BusinessDetail(navController)
    }

    composable(NavigationManager.VOYAGER_BUSINESS_SCREEN) {
        BusinessListScreen(navController)
    }

    composable(NavigationManager.TRAVEL_NOW_SCREEN) {
        TravelNow(navController)
    }

    composable(NavigationManager.VOYAGE_PAST_SCREEN) {
        VoyagerVoyages(navController)
    }

    composable(NavigationManager.CREATE_VOYAGE_SCREEN) {
        CreateVoyageScreen(navController)
    }

    composable(NavigationManager.CREATE_VOYAGE_RATE_CALC_SCREEN) {
        CreateVoyageRateCalcScreen(navController)
    }
    composable(NavigationManager.SPONSOR_SCREEN) {
        SponsorScreen(navController)
    }

    composable(VoyagerFlowRoutes.createVoyageSponsorPattern) { backStackEntry ->
        val comingFrom = backStackEntry.optBooleanArg(VoyagerFlowRoutes.CREATE_SPONSOR_SPLIT_ARG)
        CreateVoyageSponsorScreen(navController, comingFrom)
    }

    composable(NavigationManager.CONFIRM_VOYAGE_SCREEN) {
        ConfirmVoyageScreen(navController)
    }

    composable(NavigationManager.VOYAGE_BOOKED_SCREEN) {
        VoyageBookedScreenVoyager(navController)
    }

    composable(NavigationManager.SPONSOR_LIST_SCREEN) {
        SponsorList(navController)
    }

    composable(NavigationManager.FUTURE_VOYAGES_SCREEN) {
        FutureVoyages(navController)
    }

    composable(NavigationManager.VOYAGER_CHAT_SCREEN) {
        VoyagersListScreen(navController)
    }

    composable(VoyagerFlowRoutes.dashboardPattern) { backStackEntry ->
        val data = backStackEntry.optStringArg(VoyagerFlowRoutes.DASHBOARD_VALUE_ARG)
        DashboardScreen(navController, data)
    }

    composable(InteractionRoutes.voyagerFeedbackPattern) { backStackEntry ->
        val data = backStackEntry.optDecodedStringArg(InteractionRoutes.FEEDBACK_VOYAGE_ID_ARG).orEmpty()
        VoyagerFeedbackScreen(navController, data)
    }

    composable(NavigationManager.VOYAGE_STARTED_SCREEN_VOYAGER) {
        VoyageStartedScreenVoyager(navController)
    }
}
