package com.boatit.boatsharing.network.di

import com.boatit.boatsharing.network.interceptors.NetworkInterceptor
import com.boatit.boatsharing.utils.AppConstants
import com.boatit.boatsharing.utils.prefmanager.TokenProvider
import com.boatit.boatsharing.utils.session.SessionManager
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpClientPlugin
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Simple interceptor wrapper for session management
 * This approach avoids complex pipeline interception issues
 */
class SessionInterceptor(private val sessionManager: SessionManager) {
    
    suspend fun interceptRequest(request: HttpRequestBuilder): HttpRequestBuilder {
        // Add session management logic here if needed
        // For now, just return the request as-is
        return request
    }
    
    suspend fun handleResponse(response: HttpResponse): HttpResponse {
        // Handle response for session management
        // Check for 401/403 and trigger token refresh if needed
        if (response.status == HttpStatusCode.Unauthorized) {
            android.util.Log.w("SessionInterceptor", "Unauthorized response detected")
            // Could trigger session refresh here
        }
        return response
    }
}

fun createKtorClient(
    tokenProvider: TokenProvider
): HttpClient {
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
        
        // Disable built-in retry plugin to use our custom NetworkInterceptor
        install(HttpRequestRetry) {
            maxRetries = 0
        }
    }
}

/**
 * Create HttpClient with SessionInterceptor for repositories that need session management
 * This breaks the circular dependency by creating a separate HttpClient instance
 */
fun createKtorClientWithInterceptor(
    tokenProvider: TokenProvider,
    sessionManager: SessionManager
): HttpClient {
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
        
        // Use built-in retry plugin with simple configuration
        install(HttpRequestRetry) {
            maxRetries = 3
            exponentialDelay(
                base = 2.0,
                maxDelayMs = 1000
            )
        }
    }
}