package com.boatit.boatsharing.ui.navigation.destinations

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.boatit.boatsharing.features.userroles.SelectRole
import com.boatit.boatsharing.features.voyager.dashboard.view.FindDestinationLocationScreen
import com.boatit.boatsharing.features.voyager.dashboard.view.LiveTrackingMapScreen
import com.boatit.boatsharing.ui.components.MapPickerScreen
import com.boatit.boatsharing.ui.components.OnboardingPager
import com.boatit.boatsharing.ui.navigation.NavigationManager
import com.boatit.boatsharing.ui.screens.menu.BusinessMenuOptions
import com.boatit.boatsharing.ui.screens.menu.CaptainMenuOptions
import com.boatit.boatsharing.ui.screens.menu.MenuOptions
import com.boatit.boatsharing.ui.screens.onboardingscreens.BusinessOnboarding
import com.boatit.boatsharing.ui.screens.onboardingscreens.CaptainOnboarding
import com.boatit.boatsharing.ui.screens.onboardingscreens.VoyagerOnboarding
import com.boatit.boatsharing.ui.screens.splash.SplashComposable
import com.google.accompanist.pager.ExperimentalPagerApi

@OptIn(ExperimentalPagerApi::class)
fun NavGraphBuilder.registerCoreFlowDestinations(navController: NavHostController) {
    composable(NavigationManager.SPLASH_SCREEN) {
        SplashComposable(navController)
    }

    composable(NavigationManager.VOYAGER_ONBOARDING_SCREEN) {
        VoyagerOnboarding(
            navController,
            pagerState = null,
            scope = null,
        )
    }
    composable(NavigationManager.CAPTAIN_ONBOARDING_SCREEN) {
        CaptainOnboarding(
            navController,
            pagerState = null,
            scope = null,
        )
    }

    composable(NavigationManager.BUSINESS_ONBOARDING_SCREEN) {
        BusinessOnboarding(navController)
    }

    composable(NavigationManager.SELECT_ROLE_SCREEN) {
        SelectRole(navController)
    }

    composable(NavigationManager.TRACKING_SCREEN) {
        LiveTrackingMapScreen()
    }

    composable(NavigationManager.FIND_LOCATION_SCREEN) {
        FindDestinationLocationScreen(navController, onLocationSelected = { location ->
        })
    }

    composable(NavigationManager.MENU_OPTIONS_SCREEN) {
        MenuOptions(navController)
    }

    composable(NavigationManager.CAPTAIN_MENU_OPTIONS_SCREEN) {
        CaptainMenuOptions(navController)
    }

    composable(NavigationManager.BUSINESS_MENU_OPTIONS_SCREEN) {
        BusinessMenuOptions(navController)
    }

    composable(NavigationManager.MAP_PICKER_SCREEN) {
        MapPickerScreen(navController)
    }

    composable(NavigationManager.ONBOARDING_SWIPE) {
        OnboardingPager(navController)
    }
}
