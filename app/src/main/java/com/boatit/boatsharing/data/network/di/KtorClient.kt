package com.boatit.boatsharing.data.network.di

import android.util.Log
import com.boatit.boatsharing.data.network.networkresponse.RefreshRequest
import com.boatit.boatsharing.data.network.networkresponse.TokenResponse
import com.boatit.boatsharing.data.local.prefmanager.ITokenProvider
import com.boatit.boatsharing.data.network.session.UnauthorizedSessionHandler
import io.ktor.client.*
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.*
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import java.io.IOException

fun createKtorClient(
    tokenProvider: ITokenProvider,
    unauthorizedSessionHandler: UnauthorizedSessionHandler,
    /** True for debuggable builds only; release must not log bodies/headers at verbose levels. */
    enableVerboseNetworkLogging: Boolean,
): HttpClient {
    return HttpClient(CIO) {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    prettyPrint = enableVerboseNetworkLogging
                },
            )
        }

        if (enableVerboseNetworkLogging) {
            install(Logging) {
                logger =
                    object : Logger {
                        override fun log(message: String) {
                            Log.v("KTOR_HTTP", message)
                        }
                    }

                level = LogLevel.ALL
            }
        }

        HttpResponseValidator {
            validateResponse { response ->
                if (response.status == HttpStatusCode.Unauthorized) {
                    unauthorizedSessionHandler.handleUnauthorizedResponse(
                        response.call.request.url.encodedPath,
                    )
                }
            }
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
                    val access = tokenProvider.getAccessToken()
                    val refresh = tokenProvider.getRefreshToken()

                    if (!access.isNullOrBlank()) BearerTokens(access, refresh.orEmpty()) else null
                }

                refreshTokens {
                    val accessToken = tokenProvider.getAccessToken()
                    val refreshToken = tokenProvider.getRefreshToken()
                    if (!refreshToken.isNullOrEmpty()) {
                        try {
                            val responses: HttpResponse =
                                client.post("${ApiConstants.BASE_URL}${ApiConstants.Endpoints.REFRESH}") {
                                    markAsRefreshTokenRequest()
                                    contentType(ContentType.Application.Json)
                                    setBody(RefreshRequest(accessToken, refreshToken))
                                }
                            val result = responses.body<TokenResponse>()
                            tokenProvider.saveTokens(result.obj.accessToken, result.obj.refreshToken)
                            BearerTokens(result.obj.accessToken, result.obj.refreshToken)
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

fun HttpClient.invalidateTokens() {
    val auth = this.plugin(Auth)
    val bearerProvider = auth.providers.filterIsInstance<BearerAuthProvider>().firstOrNull()
    bearerProvider?.clearToken()
}
