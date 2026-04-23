package com.boatit.boatsharing.ui.navigation

/**
 * Single source of truth for nav route **path segments** (Compose NavHost destinations).
 * Grouped by feature ownership; [NavigationManager] exposes the same string values for
 * backward compatibility with existing call sites.
 */
object AppRoutes {
    /** Splash, shared chrome, non-feature flows. */
    object Core {
        const val SPLASH = "splash"
        const val MAP_PICKER = "map_picker"
        const val ONBOARDING_SWIPE = "onboardingSwipeScreens"
        const val LIVE_TRACKING = "LiveTrackingMapScreen"
        const val FIND_LOCATION = "FindLocationScreen"
    }

    object Menu {
        const val OPTIONS = "MenuOptionsScreen"
        const val CAPTAIN_OPTIONS = "CaptainMenuOptionsScreen"
        const val BUSINESS_OPTIONS = "BusinessMenuOptionsScreen"
    }

    object Onboarding {
        const val VOYAGER = "voyagerOnBoarding"
        const val CAPTAIN = "captainOnBoarding"
        const val BUSINESS = "businessOnBoarding"
    }

    /** Login, role selection, registration, settings. */
    object Auth {
        const val SELECT_ROLE = "selectRole"
        const val LOGIN = "loginScreen"
        const val FORGOT_PASSWORD = "forgotPasswordScreen"
        const val CREATE_ACCOUNT_STEP_ONE = "createAccountStepOneScreen"
        const val CREATE_ACCOUNT_STEP_TWO = "createAccountStepTwoScreen"
        const val CREATE_ACCOUNT_STEP_THREE = "createAccountStepThreeScreen"
        const val USER_ACCOUNT_INFO = "userAccountInfoScreen"
        const val SETTINGS = "settingsScreen"
    }

    object SignupCaptain {
        const val CAPTAIN_INFO = "captainInfoScreen"
        const val CAPTAIN_DOCUMENT_INFO = "captainDocumentInfoScreen"
        const val CAPTAIN_BOAT_INFO = "captainBoatInfoScreen"
    }

    object SignupBusiness {
        const val GENERAL_INFO = "businessGeneralInfoScreen"
        const val DESCRIPTIONS = "businessDescriptionsScreen"
        const val LOGO = "businessLogoScreen"
        const val BUSINESS_ACCOUNT = "BusinessACCINFOScreen"
        const val BUSINESS_DESC = "BusinessDescScreen"
    }

    /** Voyager dashboard, booking, travel, feedback. */
    object Voyager {
        const val DASHBOARD = "dashboardScreen"
        const val VOYAGER_CHAT_LIST = "VoyagerChatScreen"
        const val BUSINESS_LIST = "VoyagerBusinessScreen"
        const val TRAVEL_NOW = "TravelScreen"
        const val VOYAGE_PAST = "VoyagePastScreen"
        const val CREATE_VOYAGE = "CreateVoyage"
        const val CREATE_VOYAGE_RATE_CALC = "CreateVoyageRateCalc"
        const val CREATE_VOYAGE_SPONSOR = "CreateVoyageSponsorScreen"
        const val SPONSOR = "SponsorScreen"
        const val CONFIRM_VOYAGE = "ConfirmVoyageScreen"
        const val VOYAGE_BOOKED = "VoyageBookedScreen"
        const val SPONSOR_LIST = "SponsorListScreen"
        const val FUTURE_VOYAGES = "FutureVoyagesScreen"
        const val BUSINESS_DETAIL = "BusinessDetailScreen"
        const val VOYAGE_STARTED_VOYAGER = "VoyageStartedScreenVoyager"
        const val VOYAGER_FEEDBACK = "VoyagerFeedbackScreen"
    }

    object Captain {
        const val CAPTAIN_VOYAGES = "captainVoyages"
        const val DASHBOARD = "captaindashboardScreen"
        const val OFFLINE = "captainofflineScreen"
        const val FEEDBACK = "captainFeedbackScreen"
        const val VOYAGE_STARTED = "VoyageStartedScreen"
        const val CURRENT_VOYAGES = "CaptainCurrentVoyages"
    }

    object Business {
        const val DASHBOARD = "BusinessScreen"
    }

    /** Deep-linkable chat (multi-arg route). */
    object Chat {
        const val CHAT = "chatScreen"
    }
}
