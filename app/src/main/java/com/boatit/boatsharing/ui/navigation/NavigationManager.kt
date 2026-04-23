package com.boatit.boatsharing.ui.navigation

/**
 * Legacy route constants — values are delegated to [AppRoutes] so strings are not duplicated.
 * Prefer [AppRoutes] in new code; this object remains for existing navigation call sites.
 */
object NavigationManager {
    const val SPLASH_SCREEN = AppRoutes.Core.SPLASH
    const val VOYAGER_ONBOARDING_SCREEN = AppRoutes.Onboarding.VOYAGER
    const val CAPTAIN_ONBOARDING_SCREEN = AppRoutes.Onboarding.CAPTAIN
    const val CAPTAIN_VOYAGES_SCREEN = AppRoutes.Captain.CAPTAIN_VOYAGES
    const val BUSINESS_ONBOARDING_SCREEN = AppRoutes.Onboarding.BUSINESS
    const val SELECT_ROLE_SCREEN = AppRoutes.Auth.SELECT_ROLE
    const val LOGIN_SCREEN = AppRoutes.Auth.LOGIN
    const val FORGOT_PASSWORD_SCREEN = AppRoutes.Auth.FORGOT_PASSWORD
    const val CREATE_ACCOUNT_STEP_ONE_SCREEN = AppRoutes.Auth.CREATE_ACCOUNT_STEP_ONE
    const val CREATE_ACCOUNT_STEP_TWO_SCREEN = AppRoutes.Auth.CREATE_ACCOUNT_STEP_TWO
    const val CREATE_ACCOUNT_STEP_THREE_SCREEN = AppRoutes.Auth.CREATE_ACCOUNT_STEP_THREE
    const val USER_ACCOUNT_INFO_SCREEN = AppRoutes.Auth.USER_ACCOUNT_INFO
    const val CAPTAIN_INFO_SCREEN = AppRoutes.SignupCaptain.CAPTAIN_INFO
    const val CAPTAIN_DOCUMENT_INFO_SCREEN = AppRoutes.SignupCaptain.CAPTAIN_DOCUMENT_INFO
    const val CAPTAIN_BOAT_INFO_SCREEN = AppRoutes.SignupCaptain.CAPTAIN_BOAT_INFO
    const val BUSINESS_GENERAL_INFO_SCREEN = AppRoutes.SignupBusiness.GENERAL_INFO
    const val BUSINESS_DESCRIPTIONS_SCREEN = AppRoutes.SignupBusiness.DESCRIPTIONS
    const val BUSINESS_LOGO_SCREEN = AppRoutes.SignupBusiness.LOGO
    const val DASHBOARD_SCREEN = AppRoutes.Voyager.DASHBOARD
    const val CHAT_SCREEN = AppRoutes.Chat.CHAT
    const val VOYAGER_CHAT_SCREEN = AppRoutes.Voyager.VOYAGER_CHAT_LIST
    const val CAPTAIN_DASHBOARD_SCREEN = AppRoutes.Captain.DASHBOARD
    const val CAPTAIN_OFFLINE_SCREEN = AppRoutes.Captain.OFFLINE
    const val CAPTAIN_FEEDBACK_SCREEN = AppRoutes.Captain.FEEDBACK
    const val FIND_LOCATION_SCREEN = AppRoutes.Core.FIND_LOCATION
    const val MENU_OPTIONS_SCREEN = AppRoutes.Menu.OPTIONS
    const val CAPTAIN_MENU_OPTIONS_SCREEN = AppRoutes.Menu.CAPTAIN_OPTIONS
    const val BUSINESS_MENU_OPTIONS_SCREEN = AppRoutes.Menu.BUSINESS_OPTIONS

    const val VOYAGE_STARTED_SCREEN = AppRoutes.Captain.VOYAGE_STARTED
    const val VOYAGE_PAST_SCREEN = AppRoutes.Voyager.VOYAGE_PAST
    const val VOYAGE_STARTED_SCREEN_VOYAGER = AppRoutes.Voyager.VOYAGE_STARTED_VOYAGER
    const val TRACKING_SCREEN = AppRoutes.Core.LIVE_TRACKING
    const val CREATE_VOYAGE_SCREEN = AppRoutes.Voyager.CREATE_VOYAGE
    const val CREATE_VOYAGE_RATE_CALC_SCREEN = AppRoutes.Voyager.CREATE_VOYAGE_RATE_CALC
    const val CREATE_VOYAGE_SPONSOR_SCREEN = AppRoutes.Voyager.CREATE_VOYAGE_SPONSOR
    const val SPONSOR_SCREEN = AppRoutes.Voyager.SPONSOR
    const val CONFIRM_VOYAGE_SCREEN = AppRoutes.Voyager.CONFIRM_VOYAGE
    const val VOYAGE_BOOKED_SCREEN = AppRoutes.Voyager.VOYAGE_BOOKED
    const val SPONSOR_LIST_SCREEN = AppRoutes.Voyager.SPONSOR_LIST
    const val FUTURE_VOYAGES_SCREEN = AppRoutes.Voyager.FUTURE_VOYAGES
    const val CAPTAIN_CURRENT_VOYAGES_SCREEN = AppRoutes.Captain.CURRENT_VOYAGES
    const val VOYAGER_FEEDBACK_SCREEN = AppRoutes.Voyager.VOYAGER_FEEDBACK
    const val VOYAGER_BUSINESS_SCREEN = AppRoutes.Voyager.BUSINESS_LIST
    const val BUSINESS_SCREEN = AppRoutes.Business.DASHBOARD
    const val BUSINESS_ACCT_INFO_SCREEN = AppRoutes.SignupBusiness.BUSINESS_ACCOUNT
    const val BUSINESS_DESC_SCREEN = AppRoutes.SignupBusiness.BUSINESS_DESC
    const val BUSINESS_DETAIL_SCREEN = AppRoutes.Voyager.BUSINESS_DETAIL
    const val TRAVEL_NOW_SCREEN = AppRoutes.Voyager.TRAVEL_NOW
    const val SETTINGS_SCREEN = AppRoutes.Auth.SETTINGS
    const val ONBOARDING_SWIPE = AppRoutes.Core.ONBOARDING_SWIPE

    const val MAP_PICKER_SCREEN = AppRoutes.Core.MAP_PICKER
}
