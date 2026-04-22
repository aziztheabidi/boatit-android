package com.boatit.boatsharing.data.network.retrofit

import com.boatit.boatsharing.data.network.session.UnauthorizedSessionHandler
import okhttp3.Interceptor
import okhttp3.Response

class UnauthorizedResponseInterceptor(
    private val unauthorizedSessionHandler: UnauthorizedSessionHandler,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        if (response.code == 401) {
            unauthorizedSessionHandler.handleUnauthorizedResponse(chain.request().url.encodedPath)
        }
        return response
    }
}
