package com.boatit.boatsharing.utils.prefmanager

import android.content.Context

class TokenProvider(context: Context) {

    private val sharedPrefManager = SharedPrefManager(context)

    fun getAccessToken(): String? {
        return sharedPrefManager.getUserData()?.Accesstoken
    }

    fun getRefreshToken(): String? {
        return sharedPrefManager.getUserData()?.Refreshtoken
    }

    fun saveTokens(accessToken: String?, refreshToken: String?) {
        val userData = sharedPrefManager.getUserData()?.apply {
            Accesstoken = accessToken!!
            Refreshtoken = refreshToken!!
        }
        if (userData != null) {
            sharedPrefManager.saveLoginData(userData)
        }
    }

    fun clearTokens() {
        sharedPrefManager.clearUserData()
    }
}