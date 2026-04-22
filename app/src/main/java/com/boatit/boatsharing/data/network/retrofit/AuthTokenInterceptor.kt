package com.boatit.boatsharing.data.network.retrofit

import com.boatit.boatsharing.data.local.prefmanager.ITokenProvider
import okhttp3.Interceptor
import okhttp3.Response

class AuthTokenInterceptor(
    private val tokenProvider: ITokenProvider,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        if (originalRequest.header("Authorization") != null) {
            return chain.proceed(originalRequest)
        }

        val accessToken = tokenProvider.getAccessToken()
        val requestWithAuth =
            if (accessToken.isNullOrBlank()) {
                originalRequest
            } else {
                originalRequest
                    .newBuilder()
                    .addHeader("Authorization", "Bearer $accessToken")
                    .build()
            }

        return chain.proceed(requestWithAuth)
    }
}
