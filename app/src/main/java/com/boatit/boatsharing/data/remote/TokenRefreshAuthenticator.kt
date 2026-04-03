package com.boatit.boatsharing.data.remote

import com.boatit.boatsharing.network.di.ApiConstants
import com.boatit.boatsharing.network.networkresponse.RefreshRequest
import com.boatit.boatsharing.network.networkresponse.TokenResponse
import com.boatit.boatsharing.utils.prefmanager.TokenProvider
import com.google.gson.Gson
import okhttp3.Authenticator
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * On 401, refreshes tokens using a plain OkHttp call (no auth interceptors) and retries once.
 */
class TokenRefreshAuthenticator(
    private val tokenProvider: TokenProvider,
    private val gson: Gson,
) : Authenticator {

    private val refreshClient = OkHttpClient()

    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.request.url.encodedPath.contains("RefreshToken", ignoreCase = true)) {
            return null
        }
        if (responseCount(response) >= 2) return null

        val refreshToken = tokenProvider.getRefreshToken() ?: return null
        val accessToken = tokenProvider.getAccessToken()

        synchronized(this) {
            val newAccess = tokenProvider.getAccessToken()
            if (newAccess != accessToken && !newAccess.isNullOrBlank()) {
                return response.request.newBuilder()
                    .header("Authorization", "Bearer $newAccess")
                    .build()
            }

            val json = gson.toJson(RefreshRequest(accessToken, refreshToken))
            val body = json.toRequestBody("application/json; charset=utf-8".toMediaType())
            val url = "${ApiConstants.BASE_URL.removeSuffix("/")}${ApiConstants.Endpoints.REFRESH}"
            val refreshRequest = Request.Builder()
                .url(url)
                .post(body)
                .build()

            return try {
                refreshClient.newCall(refreshRequest).execute().use { refreshResponse ->
                    if (!refreshResponse.isSuccessful) return null
                    val text = refreshResponse.body?.string() ?: return null
                    val tokenResponse = gson.fromJson(text, TokenResponse::class.java)
                    tokenProvider.saveTokens(
                        tokenResponse.obj.Accesstoken,
                        tokenResponse.obj.Refreshtoken,
                    )
                    response.request.newBuilder()
                        .header("Authorization", "Bearer ${tokenResponse.obj.Accesstoken}")
                        .build()
                }
            } catch (_: Exception) {
                null
            }
        }
    }

    private fun responseCount(response: Response): Int {
        var count = 1
        var prior = response.priorResponse
        while (prior != null) {
            count++
            prior = prior.priorResponse
        }
        return count
    }
}
