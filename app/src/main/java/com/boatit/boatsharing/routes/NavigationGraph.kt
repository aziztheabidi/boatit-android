package com.boatit.boatsharing.routes
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.boatit.boatsharing.routes.NavigationManager.CAPTAIN_FEEDBACK_SCREEN
import com.boatit.boatsharing.routes.NavigationManager.CHAT_SCREEN
import com.boatit.boatsharing.routes.NavigationManager.CREATE_ACCOUNT_STEP_THREE_SCREEN
import com.boatit.boatsharing.routes.NavigationManager.CREATE_ACCOUNT_STEP_TWO_SCREEN
import com.boatit.boatsharing.routes.NavigationManager.CREATE_VOYAGE_SPONSOR_SCREEN
import com.boatit.boatsharing.routes.NavigationManager.DASHBOARD_SCREEN
import com.boatit.boatsharing.routes.NavigationManager.SETTINGS_SCREEN
import com.boatit.boatsharing.routes.NavigationManager.USER_ACCOUNT_INFO_SCREEN
import com.boatit.boatsharing.routes.NavigationManager.VOYAGER_FEEDBACK_SCREEN
import com.boatit.boatsharing.ui.business.view.BusinessDashboard
import com.boatit.boatsharing.ui.captain.availablitystatus.CaptainFeedbackScreen
import com.boatit.boatsharing.ui.captain.availablitystatus.CustomStatusScreen
import com.boatit.boatsharing.ui.captain.availablitystatus.VoyageBookedScreenVoyager
import com.boatit.boatsharing.ui.captain.availablitystatus.VoyageStartedScreen
import com.boatit.boatsharing.ui.captain.availablitystatus.VoyageStartedScreenVoyager
import com.boatit.boatsharing.ui.captain.availablitystatus.VoyagerFeedbackScreen
import com.boatit.boatsharing.ui.captain.dashbaord.view.CaptainDashboard
import com.boatit.boatsharing.ui.captain.voyages.view.CaptainVoyages
import com.boatit.boatsharing.ui.chat.view.CaptainCurrentVoyages
import com.boatit.boatsharing.ui.chat.view.ChatScreen
import com.boatit.boatsharing.ui.chat.view.VoyagersListScreen
import com.boatit.boatsharing.ui.forgotpassword.view.ForgotPasswordScreen
import com.boatit.boatsharing.ui.login.view.LoginScreen
import com.boatit.boatsharing.ui.menu.BusinessMenuOptions
import com.boatit.boatsharing.ui.menu.CaptainMenuOptions
import com.boatit.boatsharing.ui.voyager.dashbaord.view.DashboardScreen
import com.boatit.boatsharing.ui.voyager.dashbaord.view.FindDestinationLocationScreen
import com.boatit.boatsharing.ui.menu.MenuOptions
import com.boatit.boatsharing.ui.onboardingscreens.BusinessOnboarding
import com.boatit.boatsharing.ui.onboardingscreens.CaptainOnboarding
import com.boatit.boatsharing.ui.onboardingscreens.VoyagerOnboarding
import com.boatit.boatsharing.ui.settings.SettingsScreen
import com.boatit.boatsharing.ui.signup.business.AddBusinessDescriptions
import com.boatit.boatsharing.ui.signup.business.AddBusinessLogo
import com.boatit.boatsharing.ui.signup.business.AddGeneralBusinessInfo
import com.boatit.boatsharing.ui.signup.business.view.BusinessAccountInfoScreen
import com.boatit.boatsharing.ui.signup.captain.AddCaptainBoatInfoScreen
import com.boatit.boatsharing.ui.signup.captain.AddCaptainDocumentInfoScreen
import com.boatit.boatsharing.ui.signup.captain.view.CaptainAccountInfoScreen
import com.boatit.boatsharing.ui.signup.general.view.CreatePassword
import com.boatit.boatsharing.ui.signup.general.view.UserAccountInfoScreen
import com.boatit.boatsharing.ui.signup.general.view.UserBasicInfoScreen
import com.boatit.boatsharing.ui.signup.general.view.VerifyUserEmail
import com.boatit.boatsharing.ui.userroles.SelectRole
import com.boatit.boatsharing.ui.splash.SplashComposable
import com.boatit.boatsharing.ui.voyager.dashbaord.view.BusinessDetail
import com.boatit.boatsharing.ui.voyager.dashbaord.view.BusinessListScreen
import com.boatit.boatsharing.ui.voyager.dashbaord.view.ConfirmVoyageScreen
import com.boatit.boatsharing.ui.voyager.dashbaord.view.CreateVoyageRateCalcScreen
import com.boatit.boatsharing.ui.voyager.dashbaord.view.CreateVoyageScreen
import com.boatit.boatsharing.ui.voyager.dashbaord.view.CreateVoyageSponsorScreen
import com.boatit.boatsharing.ui.voyager.dashbaord.view.FutureVoyages
import com.boatit.boatsharing.ui.voyager.dashbaord.view.LiveTrackingMapScreen
import com.boatit.boatsharing.ui.voyager.dashbaord.view.SponcersList
import com.boatit.boatsharing.ui.voyager.dashbaord.view.SponsorScreen
import com.boatit.boatsharing.ui.voyager.dashbaord.view.TravelNow
import com.boatit.boatsharing.ui.voyager.dashbaord.view.VoyagerVoyages
import com.boatit.boatsharing.uihelpers.MapPickerScreen
import com.boatit.boatsharing.uihelpers.OnboardingPager
import com.google.accompanist.pager.ExperimentalPagerApi


object NavigationManager {
    const val SPLASH_SCREEN = "splash"
    const val VOYAGER_ONBOARDING_SCREEN = "voyagerOnBoarding"
    const val CAPTAIN_ONBOARDING_SCREEN = "captainOnBoarding"
    const val CAPTAIN_VOYAGES_SCREEN = "captainVoyages"
    const val BUSINESS_ONBOARDING_SCREEN = "businessOnBoarding"
    const val SELECT_ROLE_SCREEN = "selectRole"
    const val LOGIN_SCREEN = "loginScreen"
    const val FORGOT_PASSWORD_SCREEN = "forgotPasswordScreen"
    const val CREATE_ACCOUNT_STEP_ONE_SCREEN = "createAccountStepOneScreen"
    const val CREATE_ACCOUNT_STEP_TWO_SCREEN = "createAccountStepTwoScreen"
    const val CREATE_ACCOUNT_STEP_THREE_SCREEN = "createAccountStepThreeScreen"
    const val USER_ACCOUNT_INFO_SCREEN = "userAccountInfoScreen"
    const val CAPTAIN_INFO_SCREEN = "captainInfoScreen"
    const val CAPTAIN_DOCUMENT_INFO_SCREEN = "captainDocumentInfoScreen"
    const val CAPTAIN_BOAT_INFO_SCREEN = "captainBoatInfoScreen"
    const val BUSINESS_GENERAL_INFO_SCREEN = "businessGeneralInfoScreen"
    const val BUSINESS_DESCRIPTIONS_SCREEN = "businessDescriptionsScreen"
    const val BUSINESS_LOGO_SCREEN = "businessLogoScreen"
    const val DASHBOARD_SCREEN = "dashboardScreen"
    const val CHAT_SCREEN = "chatScreen"
    const val VOYAGER_CHAT_SCREEN = "VoyagerChatScreen"
    const val CAPTAIN_DASHBOARD_SCREEN = "captaindashboardScreen"
    const val CAPTAIN_OFFLINE_SCREEN = "captainofflineScreen"
    const val CAPTAIN_FEEDBACK_SCREEN = "captainFeedbackScreen"
    const val FIND_LOCATION_SCREEN = "FindLocationScreen"
    const val MENU_OPTIONS_SCREEN = "MenuOptionsScreen"
    const val CAPTAIN_MENU_OPTIONS_SCREEN = "CaptainMenuOptionsScreen"
    const val BUSINESS_MENU_OPTIONS_SCREEN = "BusinessMenuOptionsScreen"

    const val VOYAGE_STARTED_SCREEN = "VoyageStartedScreen"
    const val VOYAGE_PAST_SCREEN = "VoyagePastScreen"
    const val VOYAGE_STARTED_SCREEN_Voyager = "VoyageStartedScreenVoyager"
    const val TRACKING_SCREEN = "LiveTrackingMapScreen"
    const val CREATE_VOYAGE_SCREEN = "CreateVoyage"
    const val CREATE_VOYAGE_RATE_CALC_SCREEN = "CreateVoyageRateCalc"
    const val CREATE_VOYAGE_SPONSOR_SCREEN = "CreateVoyageSponsorScreen"
    const val SPONSOR_SCREEN = "SponsorScreen"
    const val CONFIRM_VOYAGE_SCREEN = "ConfirmVoyageScreen"
    const val VOYAGE_BOOKED_SCREEN = "VoyageBookedScreen"
    const val SPONSOR_LIST_SCREEN = "SponsorListScreen"
    const val FUTURE_VOYAGES_SCREEN = "FutureVoyagesScreen"
    const val CAPTAIN_CURRENT_VOYAGES_SCREEN = "CaptainCurrentVoyages"
    const val VOYAGER_FEEDBACK_SCREEN = "VoyagerFeedbackScreen"
    const val VOYAGER_BUSINESS_SCREEN = "VoyagerBusinessScreen"
    const val BUSINESS_SCREEN = "BusinessScreen"
    const val BUSINESS_ACCT_INFO_SCREEN = "BusinessACCINFOScreen"
    const val BUSINESS_DESC_SCREEN = "BusinessDescScreen"
    const val BUSINESS_DETAIL_SCREEN = "BusinessDetailScreen"
    const val TRAVER_NOW_SCREEN = "TravelScreen"
    const val SETTINGS_SCREEN = "settingsScreen"
    const val ONBOARDING_SWIPE = "onboardingSwipeScreens"



}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun AppNavGraph(navController: NavHostController ) {

       NavHost(navController = navController, startDestination = NavigationManager.SPLASH_SCREEN) {
        // Splash screen
        composable(NavigationManager.SPLASH_SCREEN) {
            SplashComposable(navController)
        }

       composable(NavigationManager.BUSINESS_SCREEN) {
           BusinessDashboard(navController)
       }

       composable(NavigationManager.BUSINESS_DETAIL_SCREEN) {
           BusinessDetail(navController)
       }

       composable(NavigationManager.VOYAGER_BUSINESS_SCREEN) {
           BusinessListScreen(navController)
       }

       composable(NavigationManager.TRAVER_NOW_SCREEN) {
           TravelNow(navController)
       }

           composable("$SETTINGS_SCREEN/{value}") { backStackEntry ->
               val comingFrom = backStackEntry.arguments?.getString("value")
               SettingsScreen(navController, comingFrom)
           }

           //  onboarding screens
        composable(NavigationManager.VOYAGER_ONBOARDING_SCREEN) {
            VoyagerOnboarding(
                navController,
                pagerState = null,
                scope = null
            )
        }
        composable(NavigationManager.CAPTAIN_ONBOARDING_SCREEN) {
            CaptainOnboarding(navController,pagerState = null,
                scope = null)
        }

         composable(NavigationManager.BUSINESS_ONBOARDING_SCREEN) {
               BusinessOnboarding(navController)
           }

           composable(NavigationManager.VOYAGE_PAST_SCREEN) {
               VoyagerVoyages(navController)
           }

           //roles screen////
           composable(NavigationManager.SELECT_ROLE_SCREEN) {
               SelectRole(navController)
           }

           // login screen///
           composable(NavigationManager.LOGIN_SCREEN) {
               LoginScreen(navController)
           }

           // login screen///
           composable(NavigationManager.TRACKING_SCREEN) {
               LiveTrackingMapScreen()
           }

           ///forgot password///
           composable(NavigationManager.FORGOT_PASSWORD_SCREEN) {
               ForgotPasswordScreen(navController)
           }

           ////create account///
           composable(NavigationManager.CREATE_ACCOUNT_STEP_ONE_SCREEN) {
               UserBasicInfoScreen(navController)
           }

           ////create account///
           composable(NavigationManager.CREATE_VOYAGE_SCREEN) {
               CreateVoyageScreen(navController)
           }

           composable(NavigationManager.CREATE_VOYAGE_RATE_CALC_SCREEN) {
               CreateVoyageRateCalcScreen(navController)
           }
           composable(NavigationManager.SPONSOR_SCREEN) {
               SponsorScreen(navController)
           }

           composable("$CREATE_VOYAGE_SPONSOR_SCREEN/{value}") { backStackEntry ->
               val comingFrom = backStackEntry.arguments?.getBoolean("value")
               CreateVoyageSponsorScreen(navController, comingFrom!!)
           }

           composable(NavigationManager.CONFIRM_VOYAGE_SCREEN) {
               ConfirmVoyageScreen(navController)
           }

           composable(NavigationManager.VOYAGE_BOOKED_SCREEN) {
               VoyageBookedScreenVoyager(navController)
           }

           composable(NavigationManager.BUSINESS_ACCT_INFO_SCREEN) {
               BusinessAccountInfoScreen(navController)
           }

           composable(NavigationManager.SPONSOR_LIST_SCREEN) {
               SponcersList(navController)
           }

           composable(NavigationManager.FUTURE_VOYAGES_SCREEN) {
               FutureVoyages(navController)
           }

           composable("$CREATE_ACCOUNT_STEP_TWO_SCREEN/{email}") { backStackEntry ->
               val userEmail = backStackEntry.arguments?.getString("email")
               if (userEmail != null) {
                   VerifyUserEmail(navController, userEmail)
               }
           }

           composable("$CREATE_ACCOUNT_STEP_THREE_SCREEN/{tokenValue}") { backStackEntry ->
               val tokenValue = backStackEntry.arguments?.getString("tokenValue")
               if (tokenValue != null) {
                   CreatePassword(navController, tokenValue)
               }

           }

           composable("$USER_ACCOUNT_INFO_SCREEN/{value}") { backStackEntry ->
               val comingFrom = backStackEntry.arguments?.getString("value")
               UserAccountInfoScreen(navController, comingFrom)
           }

           composable(NavigationManager.VOYAGER_CHAT_SCREEN) {
               VoyagersListScreen(navController)
           }

           composable(NavigationManager.CAPTAIN_INFO_SCREEN) {
               CaptainAccountInfoScreen(navController)
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

           composable(NavigationManager.CAPTAIN_DASHBOARD_SCREEN) {
               CaptainDashboard(navController)
           }

           composable(NavigationManager.CAPTAIN_VOYAGES_SCREEN) {
               CaptainVoyages(navController)
           }

           composable(NavigationManager.VOYAGE_STARTED_SCREEN) {
               VoyageStartedScreen(navController)
           }

           composable(NavigationManager.VOYAGE_STARTED_SCREEN_Voyager) {
               VoyageStartedScreenVoyager(navController)
           }

           composable(NavigationManager.CAPTAIN_CURRENT_VOYAGES_SCREEN) {
               CaptainCurrentVoyages(navController)
           }

           composable(NavigationManager.CAPTAIN_OFFLINE_SCREEN) {
               CustomStatusScreen(navController)
           }

           composable("$CAPTAIN_FEEDBACK_SCREEN/{value}") { backStackEntry ->
               val data = backStackEntry.arguments?.getString("value")
               CaptainFeedbackScreen(navController, data!!)
           }


           composable("$DASHBOARD_SCREEN/{value}") { backStackEntry ->
               val data = backStackEntry.arguments?.getString("value")
               DashboardScreen(navController, data)
           }

           //Remove Value
           composable("${CHAT_SCREEN}/{chatId}/{currentUserId}/{name}/{senderId}") { backStackEntry ->
               val chatId = backStackEntry.arguments?.getString("chatId")
               val currentUserId = backStackEntry.arguments?.getString("currentUserId")
               val name = backStackEntry.arguments?.getString("name")
               val senderId = backStackEntry.arguments?.getString("senderId")
               ChatScreen(navController, chatId!!, currentUserId!!, name!!, senderId!!)
           }

           composable(NavigationManager.FIND_LOCATION_SCREEN ) {
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

           composable("$VOYAGER_FEEDBACK_SCREEN/{value}") { backStackEntry ->
               val data = backStackEntry.arguments?.getString("value")
               VoyagerFeedbackScreen(navController, data!!)
           }

           composable("map_picker") {
               MapPickerScreen(navController)
           }

           composable(NavigationManager.ONBOARDING_SWIPE) {
               OnboardingPager(navController)
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
