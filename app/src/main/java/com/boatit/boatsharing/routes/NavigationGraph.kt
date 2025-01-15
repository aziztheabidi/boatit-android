package com.boatit.boatsharing.routes
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.boatit.boatsharing.routes.NavigationManager.USER_ACCOUNT_INFO_SCREEN
import com.boatit.boatsharing.ui.forgotpassword.ForgotPasswordScreen
import com.boatit.boatsharing.ui.home.DashboardScreen
import com.boatit.boatsharing.ui.login.LoginScreen
import com.boatit.boatsharing.ui.onboardingscreens.BusinessOnboarding
import com.boatit.boatsharing.ui.onboardingscreens.CaptainOnboarding
import com.boatit.boatsharing.ui.onboardingscreens.VoyagerOnboarding
import com.boatit.boatsharing.ui.signup.business.AddBusinessDescriptions
import com.boatit.boatsharing.ui.signup.business.AddBusinessLogo
import com.boatit.boatsharing.ui.signup.business.AddGeneralBusinessInfo
import com.boatit.boatsharing.ui.signup.captain.AddCaptainBoatInfoScreen
import com.boatit.boatsharing.ui.signup.captain.AddCaptainDocumentInfoScreen
import com.boatit.boatsharing.ui.signup.general.CreatePassword
import com.boatit.boatsharing.ui.signup.general.VerifyUserEmail
import com.boatit.boatsharing.ui.signup.general.UserAccountInfoScreen
import com.boatit.boatsharing.ui.signup.general.UserBasicInfoScreen
import com.boatit.boatsharing.ui.userroles.SelectRole
import com.boatit.boatsharing.ui.splash.SplashComposable


object NavigationManager {
    const val SPLASH_SCREEN = "splash"
    const val VOYAGER_ONBOARDING_SCREEN = "voyagerOnBoarding"
    const val CAPTAIN_ONBOARDING_SCREEN = "captainOnBoarding"
    const val BUSINESS_ONBOARDING_SCREEN = "businessOnBoarding"
    const val SELECT_ROLE_SCREEN = "selectRole"
    const val LOGIN_SCREEN = "loginScreen"
    const val FORGOT_PASSWORD_SCREEN = "forgotPasswordScreen"
    const val CREATE_ACCOUNT_STEP_ONE_SCREEN = "createAccountStepOneScreen"
    const val CREATE_ACCOUNT_STEP_TWO_SCREEN = "createAccountStepTwoScreen"
    const val CREATE_ACCOUNT_STEP_THREE_SCREEN = "createAccountStepThreeScreen"
    const val USER_ACCOUNT_INFO_SCREEN = "userAccountInfoScreen"
    const val CAPTAIN_DOCUMENT_INFO_SCREEN = "captainDocumentInfoScreen"
    const val CAPTAIN_BOAT_INFO_SCREEN = "captainBoatInfoScreen"
    const val BUSINESS_GENERAL_INFO_SCREEN = "businessGeneralInfoScreen"
    const val BUSINESS_DESCRIPTIONS_SCREEN = "businessDescriptionsScreen"
    const val BUSINESS_LOGO_SCREEN = "businessLogoScreen"
    const val DASHBOARD_SCREEN = "dashboardScreen"

}

@Composable
fun AppNavGraph(navController: NavHostController ) {
       NavHost(navController = navController, startDestination = NavigationManager.SPLASH_SCREEN) {
        // Splash screen
        composable(NavigationManager.SPLASH_SCREEN) {
            SplashComposable(navController)
        }
       //  onboarding screens
        composable(NavigationManager.VOYAGER_ONBOARDING_SCREEN) {
            VoyagerOnboarding(navController)
        }
        composable(NavigationManager.CAPTAIN_ONBOARDING_SCREEN) {
            CaptainOnboarding(navController)
        }

         composable(NavigationManager.BUSINESS_ONBOARDING_SCREEN) {
               BusinessOnboarding(navController)
           }

           //roles screen////

           composable(NavigationManager.SELECT_ROLE_SCREEN) {
               SelectRole(navController)
           }
           // login screen///

           composable(NavigationManager.LOGIN_SCREEN) {
               LoginScreen(navController)
           }

           ///forgot password///
           composable(NavigationManager.FORGOT_PASSWORD_SCREEN) {
               ForgotPasswordScreen(navController)
           }

           ////create account///
           composable(NavigationManager.CREATE_ACCOUNT_STEP_ONE_SCREEN) {
               UserBasicInfoScreen(navController)
           }

           composable(NavigationManager.CREATE_ACCOUNT_STEP_TWO_SCREEN) {
               VerifyUserEmail(navController)
           }

           composable(NavigationManager.CREATE_ACCOUNT_STEP_THREE_SCREEN) {
               CreatePassword(navController)
           }


           composable("$USER_ACCOUNT_INFO_SCREEN/{value}") { backStackEntry ->
               val comingFrom = backStackEntry.arguments?.getString("value")
               UserAccountInfoScreen(navController, comingFrom)
           }

           composable(NavigationManager.CAPTAIN_DOCUMENT_INFO_SCREEN) {
               AddCaptainDocumentInfoScreen(navController)
           }
           composable(NavigationManager.CAPTAIN_BOAT_INFO_SCREEN) {
               AddCaptainBoatInfoScreen(navController)
           }

           composable(NavigationManager.BUSINESS_GENERAL_INFO_SCREEN) {
               AddGeneralBusinessInfo(navController)
           }

           composable(NavigationManager.BUSINESS_DESCRIPTIONS_SCREEN) {
               AddBusinessDescriptions(navController)
           }

           composable(NavigationManager.BUSINESS_LOGO_SCREEN) {
               AddBusinessLogo(navController)
           }
           composable(NavigationManager.DASHBOARD_SCREEN) {
               DashboardScreen(navController)
           }



    }
}



fun NavController.navigateWithClearStack(route: String, clearStack: Boolean) {
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
    this.popBackStack()
}
