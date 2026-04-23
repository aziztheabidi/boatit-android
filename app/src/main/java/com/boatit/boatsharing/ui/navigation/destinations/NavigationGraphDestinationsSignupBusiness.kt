package com.boatit.boatsharing.ui.navigation.destinations

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.boatit.boatsharing.features.signup.business.AddBusinessDescriptions
import com.boatit.boatsharing.features.signup.business.AddBusinessLogo
import com.boatit.boatsharing.features.signup.business.AddGeneralBusinessInfo
import com.boatit.boatsharing.features.signup.business.view.BusinessAccountInfoScreen
import com.boatit.boatsharing.ui.navigation.NavigationManager

fun NavGraphBuilder.registerBusinessSignupDestinations(navController: NavHostController) {
    composable(NavigationManager.BUSINESS_GENERAL_INFO_SCREEN) {
        AddGeneralBusinessInfo(navController)
    }

    composable(NavigationManager.BUSINESS_DESCRIPTIONS_SCREEN) {
        AddBusinessDescriptions(navController)
    }

    composable(NavigationManager.BUSINESS_LOGO_SCREEN) {
        AddBusinessLogo(navController)
    }

    composable(NavigationManager.BUSINESS_ACCT_INFO_SCREEN) {
        BusinessAccountInfoScreen(navController)
    }
}
