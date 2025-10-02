package com.boatit.boatsharing.network.di

import com.boatit.boatsharing.utils.AppConstants
import com.boatit.boatsharing.utils.prefmanager.TokenProvider
import com.boatit.boatsharing.utils.session.SessionManager
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import android.util.Log

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
        
        // Disable built-in retry plugin for basic client (no session management needed)
        install(HttpRequestRetry) {
            maxRetries = 0
        }
    }
}

/**
 * Create HttpClient with native Ktor session management and retry logic
 * 
 * This implementation replaces the deprecated NetworkInterceptor with Ktor's built-in capabilities:
 * - HttpRequestRetry: Native retry logic with exponential backoff
 * - Auth plugin: Automatic token refresh and bearer token management
 * - HttpTimeout: Comprehensive timeout configuration
 * - Logging: Request/response logging for debugging
 * 
 * Migration from NetworkInterceptor:
 * - ✅ Server Error Retry (5xx) - Native Ktor HttpRequestRetry
 * - ✅ Timeout Error Retry - Native Ktor HttpRequestRetry
 * - ✅ Client Error Handling (4xx) - Native Ktor Auth plugin
 * - ✅ Exception-based Retry - Native Ktor HttpRequestRetry
 * - ✅ Session Management - Native Ktor Auth plugin + SessionManager
 * - ✅ Token Refresh - Automatic via Auth plugin
 * - ✅ Logging - Native Ktor Logging plugin
 * - ✅ Timeout Configuration - Native Ktor HttpTimeout plugin
 * - ⚠️ Malformed Response Detection - Not implemented (non-critical)
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
        
        // Configure timeouts
        install(HttpTimeout) {
            requestTimeoutMillis = 30000
            connectTimeoutMillis = 10000
            socketTimeoutMillis = 30000
        }
        
        // Configure authentication with automatic token refresh
        install(Auth) {
            bearer {
                loadTokens {
                    val accessToken = tokenProvider.getAccessToken()
                    val refreshToken = tokenProvider.getRefreshToken()
                    
                    if (accessToken != null && refreshToken != null) {
                        Log.d("KtorClient", "Loading tokens for authentication")
                        BearerTokens(accessToken, refreshToken)
                    } else {
                        Log.w("KtorClient", "No tokens available for authentication")
                        null
                    }
                }
                
                refreshTokens {
                    Log.d("KtorClient", "Attempting token refresh")
                    try {
                        // Use SessionManager to handle token refresh
                        val refreshSuccess = sessionManager.handleUnauthorized()
                        if (refreshSuccess) {
                            val newAccessToken = tokenProvider.getAccessToken()
                            val newRefreshToken = tokenProvider.getRefreshToken()
                            if (newAccessToken != null && newRefreshToken != null) {
                                Log.i("KtorClient", "Token refresh successful")
                                BearerTokens(newAccessToken, newRefreshToken)
                            } else {
                                Log.w("KtorClient", "Token refresh failed - no new tokens")
                                null
                            }
                        } else {
                            Log.w("KtorClient", "Token refresh not needed or failed")
                            null
                        }
                    } catch (e: Exception) {
                        Log.e("KtorClient", "Token refresh error: ${e.message}", e)
                        null
                    }
                }
            }
        }
        
        // Configure default request headers
        defaultRequest {
            val token = tokenProvider.getAccessToken()
            Log.d("KtorClient", "Setting authorization header: ${if (token != null) "Bearer token present" else "No token"}")
            
            if (token != null) {
                headers.append(HttpHeaders.Authorization, "Bearer $token")
            } else if (AppConstants.JWT_TOKEN != null) {
                headers.append(HttpHeaders.Authorization, "Bearer ${AppConstants.JWT_TOKEN}")
            }
        }
        
        // Configure retry logic using Ktor's native HttpRequestRetry
        install(HttpRequestRetry) {
            maxRetries = 3
            exponentialDelay(
                base = 2.0,
                maxDelayMs = 10000
            )
        }
    }
}
