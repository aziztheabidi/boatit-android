package com.boatit.boatsharing.data.local.prefmanager

interface ITokenProvider {
    fun getAccessToken(): String?

    fun getRefreshToken(): String?

    fun saveTokens(
        accessToken: String?,
        refreshToken: String?,
    )

    fun clearTokens()

    fun clearAll()
}
