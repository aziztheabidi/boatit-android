package com.boatit.boatsharing.data.local.prefmanager

import android.content.Context

class TokenProvider(context: Context) : ITokenProvider {
    private val sharedPrefManager = SharedPrefManager(context)

    override fun getAccessToken(): String? = sharedPrefManager.getAccessToken()

    override fun getRefreshToken(): String? = sharedPrefManager.getRefreshToken()

    override fun saveTokens(
        accessToken: String?,
        refreshToken: String?,
    ) {
        val access = accessToken?.trim().orEmpty()
        val refresh = refreshToken?.trim().orEmpty()

        if (access.isBlank() && refresh.isBlank()) return

        sharedPrefManager.saveTokens(access, refresh)
    }

    override fun clearTokens() {
        sharedPrefManager.clearTokens()
    }

    override fun clearAll() {
        sharedPrefManager.clearUserData()
    }
}
