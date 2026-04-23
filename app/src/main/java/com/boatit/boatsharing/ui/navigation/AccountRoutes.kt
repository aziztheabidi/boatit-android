package com.boatit.boatsharing.ui.navigation

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

object AccountRoutes {
    const val ACCOUNT_CONTEXT_ARG = "value"
    const val EMAIL_ARG = "email"
    const val TOKEN_ARG = "tokenValue"

    val settingsPattern: String = "${AppRoutes.Auth.SETTINGS}/{$ACCOUNT_CONTEXT_ARG}"
    val userAccountInfoPattern: String = "${AppRoutes.Auth.USER_ACCOUNT_INFO}/{$ACCOUNT_CONTEXT_ARG}"
    val createAccountStepTwoPattern: String = "${AppRoutes.Auth.CREATE_ACCOUNT_STEP_TWO}/{$EMAIL_ARG}"
    val createAccountStepThreePattern: String = "${AppRoutes.Auth.CREATE_ACCOUNT_STEP_THREE}/{$TOKEN_ARG}"

    fun settings(contextValue: String): String {
        return "${AppRoutes.Auth.SETTINGS}/${encode(contextValue)}"
    }

    fun userAccountInfo(contextValue: String): String {
        return "${AppRoutes.Auth.USER_ACCOUNT_INFO}/${encode(contextValue)}"
    }

    fun createAccountStepTwo(email: String): String {
        return "${AppRoutes.Auth.CREATE_ACCOUNT_STEP_TWO}/${encode(email)}"
    }

    fun createAccountStepThree(token: String): String {
        return "${AppRoutes.Auth.CREATE_ACCOUNT_STEP_THREE}/${encode(token)}"
    }

    private fun encode(value: String): String {
        return URLEncoder.encode(value, StandardCharsets.UTF_8.toString()).replace("+", "%20")
    }
}
