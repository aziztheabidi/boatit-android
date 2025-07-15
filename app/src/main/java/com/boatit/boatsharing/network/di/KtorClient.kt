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
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType

fun createKtorClient(tokenProvider: TokenProvider): HttpClient {
    return HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
        install(Logging) {
            level = LogLevel.BODY
        }
        install(Auth) {
            bearer {
                loadTokens {
                    tokenProvider.getAccessToken()?.let { token ->
                        BearerTokens(token, tokenProvider.getRefreshToken() ?: "")
                    }
                }
            }
        }
        defaultRequest {
            val token = tokenProvider.getAccessToken()
            println("Hello" + token)
            if (token != null) {
                headers.append(HttpHeaders.Authorization, "Bearer $token")
            }else if (AppConstants.JWT_TOKEN != null){
                headers.append(HttpHeaders.Authorization, "Bearer " + AppConstants.JWT_TOKEN)
            }
        }
    }
}