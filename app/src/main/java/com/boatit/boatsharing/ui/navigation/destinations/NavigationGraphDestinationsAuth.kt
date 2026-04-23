package com.boatit.boatsharing.ui.navigation.destinations

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.boatit.boatsharing.features.forgotpassword.view.ForgotPasswordScreen
import com.boatit.boatsharing.features.login.view.LoginScreen
import com.boatit.boatsharing.features.signup.general.view.CreatePassword
import com.boatit.boatsharing.features.signup.general.view.UserAccountInfoScreen
import com.boatit.boatsharing.features.signup.general.view.UserBasicInfoScreen
import com.boatit.boatsharing.features.signup.general.view.VerifyUserEmail
import com.boatit.boatsharing.ui.navigation.AccountRoutes
import com.boatit.boatsharing.ui.navigation.NavigationManager
import com.boatit.boatsharing.ui.navigation.optDecodedStringArg
import com.boatit.boatsharing.ui.navigation.optStringArg
import com.boatit.boatsharing.ui.screens.settings.SettingsScreen

fun NavGraphBuilder.registerAuthDestinations(navController: NavHostController) {
    composable(AccountRoutes.settingsPattern) { backStackEntry ->
        val comingFrom = backStackEntry.optStringArg(AccountRoutes.ACCOUNT_CONTEXT_ARG)
        SettingsScreen(navController, comingFrom)
    }

    composable(NavigationManager.LOGIN_SCREEN) {
        LoginScreen(navController)
    }

    composable(NavigationManager.FORGOT_PASSWORD_SCREEN) {
        ForgotPasswordScreen(navController)
    }

    composable(NavigationManager.CREATE_ACCOUNT_STEP_ONE_SCREEN) {
        UserBasicInfoScreen(navController)
    }

    composable(AccountRoutes.createAccountStepTwoPattern) { backStackEntry ->
        val userEmail = backStackEntry.optDecodedStringArg(AccountRoutes.EMAIL_ARG)
        if (userEmail != null) {
            VerifyUserEmail(navController, userEmail)
        }
    }

    composable(AccountRoutes.createAccountStepThreePattern) { backStackEntry ->
        val tokenValue = backStackEntry.optDecodedStringArg(AccountRoutes.TOKEN_ARG)
        if (tokenValue != null) {
            CreatePassword(navController, tokenValue)
        }
    }

    composable(AccountRoutes.userAccountInfoPattern) { backStackEntry ->
        val comingFrom = backStackEntry.optStringArg(AccountRoutes.ACCOUNT_CONTEXT_ARG)
        UserAccountInfoScreen(navController, comingFrom)
    }
}
