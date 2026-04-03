package com.boatit.boatsharing.data.remote

import com.boatit.boatsharing.utils.AppConstants
import com.boatit.boatsharing.utils.prefmanager.TokenProvider
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Attaches Bearer token from [TokenProvider], with optional registration-time token from [AppConstants].
 */
class AuthInterceptor(
    private val tokenProvider: TokenProvider,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = tokenProvider.getAccessToken() ?: AppConstants.JWT_TOKEN
        val request = if (!token.isNullOrBlank()) {
            chain.request().newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        } else {
            chain.request()
        }
        return chain.proceed(request)
    }
}
