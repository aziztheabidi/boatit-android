package com.boatit.boatsharing.network.di

import android.content.Context
import com.boatit.boatsharing.network.networkreposne.RefreshRequest
import com.boatit.boatsharing.network.networkreposne.TokenResponse
import com.boatit.boatsharing.ui.splash.SplashComposable
import com.boatit.boatsharing.utils.AppConstants
import com.boatit.boatsharing.utils.prefmanager.TokenProvider
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.client.plugins.*
import java.io.IOException

fun createKtorClient(tokenProvider: TokenProvider): HttpClient {
    return HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
        install(Logging) {
            level = LogLevel.BODY
        }

        install(HttpRequestRetry) {
            maxRetries = 3
            retryOnExceptionIf { request, cause ->
                val isNetworkIssue = cause is IOException || cause is HttpRequestTimeoutException
                val path = request.url.encodedPath
                val isRefresh = path.endsWith(ApiConstants.Endpoints.REFRESH)
                isNetworkIssue && !isRefresh
            }
            retryIf { request, response ->
                val path = request.url.encodedPath
                val isRefresh = path.endsWith(ApiConstants.Endpoints.REFRESH)
                (response.status.value in 500..599) && !isRefresh
            }
            delayMillis { attempt ->
                (500L shl (attempt - 1)).coerceAtMost(2_000L).coerceAtLeast(0L)
            }
        }


        install(Auth) {
            bearer {
                loadTokens {
                    val access = tokenProvider.getAccessToken() ?: AppConstants.JWT_TOKEN
                    val refresh = tokenProvider.getRefreshToken()
                    if (access != null) {
                        BearerTokens(access, refresh.orEmpty())
                    } else null
                }
                refreshTokens {
                    val accessToken = tokenProvider.getAccessToken()
                    val refreshToken = tokenProvider.getRefreshToken()
                    if (!refreshToken.isNullOrEmpty()) {
                        try {
                            val responses: HttpResponse = client.post("${ApiConstants.BASE_URL}${ApiConstants.Endpoints.REFRESH}") {
                                markAsRefreshTokenRequest()
                                contentType(ContentType.Application.Json)
                                setBody(RefreshRequest(accessToken,refreshToken))
                            }
                            val result = responses.body<TokenResponse>()
                            tokenProvider.saveTokens(result.obj.Accesstoken, result.obj.Refreshtoken)
                            BearerTokens(result.obj.Accesstoken, result.obj.Refreshtoken)
                        } catch (e: Exception) {
                            null
                        }
                    } else {
                        null
                    }
                }

                sendWithoutRequest { request ->
                    val path = request.url.encodedPath
                    !path.endsWith(ApiConstants.Endpoints.REFRESH)

                }
            }
        }
        defaultRequest {
            url(ApiConstants.BASE_URL)
        }
    }
}