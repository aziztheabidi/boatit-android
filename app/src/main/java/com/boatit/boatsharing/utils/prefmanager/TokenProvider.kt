package com.boatit.boatsharing.utils.prefmanager

import android.content.Context

class TokenProvider(context: Context) {

    private val sharedPrefManager = SharedPrefManager(context)

    fun getAccessToken(): String? = sharedPrefManager.getAccessToken()
    fun getRefreshToken(): String? = sharedPrefManager.getRefreshToken()


    fun saveTokens(accessToken: String?, refreshToken: String?) {
        val access = accessToken?.trim().orEmpty()
        val refresh = refreshToken?.trim().orEmpty()

        if (access.isBlank() && refresh.isBlank()) return

        sharedPrefManager.saveTokens(access, refresh)
    }

    fun clearTokens() {
        sharedPrefManager.clearTokens()
    }


    fun clearAll() {
        sharedPrefManager.clearUserData()
    }
}