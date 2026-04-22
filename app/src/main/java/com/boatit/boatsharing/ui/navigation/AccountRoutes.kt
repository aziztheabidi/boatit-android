package com.boatit.boatsharing.ui.navigation

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

object AccountRoutes {
    const val ACCOUNT_CONTEXT_ARG = "value"
    const val EMAIL_ARG = "email"
    const val TOKEN_ARG = "tokenValue"

    val settingsPattern: String = "${NavigationManager.SETTINGS_SCREEN}/{$ACCOUNT_CONTEXT_ARG}"
    val userAccountInfoPattern: String = "${NavigationManager.USER_ACCOUNT_INFO_SCREEN}/{$ACCOUNT_CONTEXT_ARG}"
    val createAccountStepTwoPattern: String = "${NavigationManager.CREATE_ACCOUNT_STEP_TWO_SCREEN}/{$EMAIL_ARG}"
    val createAccountStepThreePattern: String = "${NavigationManager.CREATE_ACCOUNT_STEP_THREE_SCREEN}/{$TOKEN_ARG}"

    fun settings(contextValue: String): String {
        return "${NavigationManager.SETTINGS_SCREEN}/${encode(contextValue)}"
    }

    fun userAccountInfo(contextValue: String): String {
        return "${NavigationManager.USER_ACCOUNT_INFO_SCREEN}/${encode(contextValue)}"
    }

    fun createAccountStepTwo(email: String): String {
        return "${NavigationManager.CREATE_ACCOUNT_STEP_TWO_SCREEN}/${encode(email)}"
    }

    fun createAccountStepThree(token: String): String {
        return "${NavigationManager.CREATE_ACCOUNT_STEP_THREE_SCREEN}/${encode(token)}"
    }

    private fun encode(value: String): String {
        return URLEncoder.encode(value, StandardCharsets.UTF_8.toString()).replace("+", "%20")
    }
}
