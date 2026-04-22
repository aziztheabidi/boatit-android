package com.boatit.boatsharing.ui.navigation
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.boatit.boatsharing.features.business.view.BusinessDashboard
import com.boatit.boatsharing.features.captain.availabilitystatus.CaptainFeedbackScreen
import com.boatit.boatsharing.features.captain.availabilitystatus.CustomStatusScreen
import com.boatit.boatsharing.features.captain.availabilitystatus.VoyageBookedScreenVoyager
import com.boatit.boatsharing.features.captain.availabilitystatus.VoyageStartedScreen
import com.boatit.boatsharing.features.captain.availabilitystatus.VoyageStartedScreenVoyager
import com.boatit.boatsharing.features.captain.availabilitystatus.VoyagerFeedbackScreen
import com.boatit.boatsharing.features.captain.dashboard.view.CaptainDashboard
import com.boatit.boatsharing.features.captain.voyages.view.CaptainVoyages
import com.boatit.boatsharing.features.chat.view.CaptainCurrentVoyages
import com.boatit.boatsharing.features.chat.view.ChatScreen
import com.boatit.boatsharing.features.chat.view.VoyagersListScreen
import com.boatit.boatsharing.features.forgotpassword.view.ForgotPasswordScreen
import com.boatit.boatsharing.features.login.view.LoginScreen
import com.boatit.boatsharing.ui.screens.menu.BusinessMenuOptions
import com.boatit.boatsharing.ui.screens.menu.CaptainMenuOptions
import com.boatit.boatsharing.ui.screens.menu.MenuOptions
import com.boatit.boatsharing.ui.screens.onboardingscreens.BusinessOnboarding
import com.boatit.boatsharing.ui.screens.onboardingscreens.CaptainOnboarding
import com.boatit.boatsharing.ui.screens.onboardingscreens.VoyagerOnboarding
import com.boatit.boatsharing.ui.screens.settings.SettingsScreen
import com.boatit.boatsharing.features.signup.business.AddBusinessDescriptions
import com.boatit.boatsharing.features.signup.business.AddBusinessLogo
import com.boatit.boatsharing.features.signup.business.AddGeneralBusinessInfo
import com.boatit.boatsharing.features.signup.business.view.BusinessAccountInfoScreen
import com.boatit.boatsharing.features.signup.captain.AddCaptainBoatInfoScreen
import com.boatit.boatsharing.features.signup.captain.AddCaptainDocumentInfoScreen
import com.boatit.boatsharing.features.signup.captain.view.CaptainAccountInfoScreen
import com.boatit.boatsharing.features.signup.general.view.CreatePassword
import com.boatit.boatsharing.features.signup.general.view.UserAccountInfoScreen
import com.boatit.boatsharing.features.signup.general.view.UserBasicInfoScreen
import com.boatit.boatsharing.features.signup.general.view.VerifyUserEmail
import com.boatit.boatsharing.ui.screens.splash.SplashComposable
import com.boatit.boatsharing.features.userroles.SelectRole
import com.boatit.boatsharing.features.voyager.dashboard.view.BusinessDetail
import com.boatit.boatsharing.features.voyager.dashboard.view.BusinessListScreen
import com.boatit.boatsharing.features.voyager.dashboard.view.ConfirmVoyageScreen
import com.boatit.boatsharing.features.voyager.dashboard.view.CreateVoyageRateCalcScreen
import com.boatit.boatsharing.features.voyager.dashboard.view.CreateVoyageScreen
import com.boatit.boatsharing.features.voyager.dashboard.view.CreateVoyageSponsorScreen
import com.boatit.boatsharing.features.voyager.dashboard.view.DashboardScreen
import com.boatit.boatsharing.features.voyager.dashboard.view.FindDestinationLocationScreen
import com.boatit.boatsharing.features.voyager.dashboard.view.FutureVoyages
import com.boatit.boatsharing.features.voyager.dashboard.view.LiveTrackingMapScreen
import com.boatit.boatsharing.features.voyager.dashboard.view.SponsorList
import com.boatit.boatsharing.features.voyager.dashboard.view.SponsorScreen
import com.boatit.boatsharing.features.voyager.dashboard.view.TravelNow
import com.boatit.boatsharing.features.voyager.dashboard.view.VoyagerVoyages
import com.boatit.boatsharing.ui.components.MapPickerScreen
import com.boatit.boatsharing.ui.components.OnboardingPager
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
    const val VOYAGE_STARTED_SCREEN_VOYAGER = "VoyageStartedScreenVoyager"
    @Deprecated("Use VOYAGE_STARTED_SCREEN_VOYAGER")
    const val VOYAGE_STARTED_SCREEN_Voyager = VOYAGE_STARTED_SCREEN_VOYAGER
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
    const val TRAVEL_NOW_SCREEN = "TravelScreen"
    @Deprecated("Use TRAVEL_NOW_SCREEN")
    const val TRAVER_NOW_SCREEN = TRAVEL_NOW_SCREEN
    const val SETTINGS_SCREEN = "settingsScreen"
    const val ONBOARDING_SWIPE = "onboardingSwipeScreens"

    /** Shared map address picker (was hard-coded as `"map_picker"`). */
    const val MAP_PICKER_SCREEN = "map_picker"
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun AppNavGraph(navController: NavHostController) {
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

        composable(NavigationManager.TRAVEL_NOW_SCREEN) {
            TravelNow(navController)
        }

        composable(AccountRoutes.settingsPattern) { backStackEntry ->
            val comingFrom = backStackEntry.arguments?.getString(AccountRoutes.ACCOUNT_CONTEXT_ARG)
            SettingsScreen(navController, comingFrom)
        }

        //  onboarding screens
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

        composable(NavigationManager.VOYAGE_PAST_SCREEN) {
            VoyagerVoyages(navController)
        }

        // roles screen////
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

        // /forgot password///
        composable(NavigationManager.FORGOT_PASSWORD_SCREEN) {
            ForgotPasswordScreen(navController)
        }

        // //create account///
        composable(NavigationManager.CREATE_ACCOUNT_STEP_ONE_SCREEN) {
            UserBasicInfoScreen(navController)
        }

        // //create account///
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
            val comingFrom = backStackEntry.arguments?.getBoolean(VoyagerFlowRoutes.CREATE_SPONSOR_SPLIT_ARG) ?: false
            CreateVoyageSponsorScreen(navController, comingFrom)
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
            SponsorList(navController)
        }

        composable(NavigationManager.FUTURE_VOYAGES_SCREEN) {
            FutureVoyages(navController)
        }

        composable(AccountRoutes.createAccountStepTwoPattern) { backStackEntry ->
            val userEmail = backStackEntry.arguments?.getString(AccountRoutes.EMAIL_ARG)?.let(Uri::decode)
            if (userEmail != null) {
                VerifyUserEmail(navController, userEmail)
            }
        }

        composable(AccountRoutes.createAccountStepThreePattern) { backStackEntry ->
            val tokenValue = backStackEntry.arguments?.getString(AccountRoutes.TOKEN_ARG)?.let(Uri::decode)
            if (tokenValue != null) {
                CreatePassword(navController, tokenValue)
            }
        }

        composable(AccountRoutes.userAccountInfoPattern) { backStackEntry ->
            val comingFrom = backStackEntry.arguments?.getString(AccountRoutes.ACCOUNT_CONTEXT_ARG)
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

        composable(NavigationManager.VOYAGE_STARTED_SCREEN_VOYAGER) {
            VoyageStartedScreenVoyager(navController)
        }

        composable(NavigationManager.CAPTAIN_CURRENT_VOYAGES_SCREEN) {
            CaptainCurrentVoyages(navController)
        }

        composable(NavigationManager.CAPTAIN_OFFLINE_SCREEN) {
            CustomStatusScreen(navController)
        }

        composable(InteractionRoutes.captainFeedbackPattern) { backStackEntry ->
            val data = backStackEntry.arguments?.getString(InteractionRoutes.FEEDBACK_VOYAGE_ID_ARG)
            CaptainFeedbackScreen(navController, data.orEmpty())
        }

        composable(VoyagerFlowRoutes.dashboardPattern) { backStackEntry ->
            val data = backStackEntry.arguments?.getString(VoyagerFlowRoutes.DASHBOARD_VALUE_ARG)
            DashboardScreen(navController, data)
        }

        composable(InteractionRoutes.chatPattern) { backStackEntry ->
            val chatId = backStackEntry.arguments?.getString(InteractionRoutes.CHAT_ID_ARG)
            val currentUserId = backStackEntry.arguments?.getString(InteractionRoutes.CURRENT_USER_ID_ARG)
            val name = backStackEntry.arguments?.getString(InteractionRoutes.NAME_ARG)
            val senderId = backStackEntry.arguments?.getString(InteractionRoutes.SENDER_ID_ARG)
            ChatScreen(navController, chatId.orEmpty(), currentUserId.orEmpty(), name.orEmpty(), senderId.orEmpty())
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

        composable(InteractionRoutes.voyagerFeedbackPattern) { backStackEntry ->
            val data = backStackEntry.arguments?.getString(InteractionRoutes.FEEDBACK_VOYAGE_ID_ARG)
            VoyagerFeedbackScreen(navController, data.orEmpty())
        }

        composable(NavigationManager.MAP_PICKER_SCREEN) {
            MapPickerScreen(navController)
        }

        composable(NavigationManager.ONBOARDING_SWIPE) {
            OnboardingPager(navController)
        }
    }
}
