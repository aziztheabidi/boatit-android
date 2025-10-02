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
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import android.util.Log

/**
 * Create basic HttpClient without session management for TokenRefreshService
 * 
 * Implements LLR-3.10.9: Basic HttpClient Implementation
 * Provides basic HttpClient for services that don't require session management to avoid circular dependencies
 * 
 * @param tokenProvider TokenProvider for accessing stored tokens
 * @return HttpClient configured with basic plugins (ContentNegotiation, Logging, Auth, defaultRequest)
 */
fun createKtorClient(
    tokenProvider: TokenProvider
): HttpClient {
    return HttpClient(CIO) {
        // Implements LLR-3.10.6: ContentNegotiation Plugin Implementation
        // Configure ContentNegotiation plugin with Json serializer using ignoreUnknownKeys=true
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
        
        // Implements LLR-3.10.5: Logging Plugin Implementation
        // Configure Logging plugin with level=LogLevel.BODY for comprehensive request/response logging
        install(Logging) {
            level = LogLevel.BODY
        }
        
        // Implements LLR-3.10.3: Auth Plugin Bearer Implementation (basic version)
        // Configure Auth plugin with bearer provider that loads tokens from TokenProvider
        install(Auth) {
            bearer {
                loadTokens {
                    tokenProvider.getAccessToken()?.let { token ->
                        BearerTokens(token, tokenProvider.getRefreshToken() ?: "")
                    }
                }
            }
        }
        
        // Implements LLR-3.10.8: Default Request Headers Implementation (basic version)
        // Implement defaultRequest block that sets Authorization header with bearer token from TokenProvider or AppConstants.JWT_TOKEN
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
 * Implements LLR-3.10.1: Native Ktor HttpClient Implementation
 * Provides the foundation for all network operations using Ktor's native capabilities
 * 
 * This implementation replaces the deprecated NetworkInterceptor with Ktor's built-in capabilities:
 * - HttpRequestRetry: Native retry logic with exponential backoff
 * - Auth plugin: Automatic token refresh and bearer token management
 * - HttpTimeout: Comprehensive timeout configuration
 * - Logging: Request/response logging for debugging
 * 
 * Migration from NetworkInterceptor:
 * - Server Error Retry (5xx) - Native Ktor HttpRequestRetry
 * - Timeout Error Retry - Native Ktor HttpRequestRetry
 * - Client Error Handling (4xx) - Native Ktor Auth plugin
 * - Exception-based Retry - Native Ktor HttpRequestRetry
 * - Session Management - Native Ktor Auth plugin + SessionManager
 * - Token Refresh - Automatic via Auth plugin
 * - Logging - Native Ktor Logging plugin
 * - Timeout Configuration - Native Ktor HttpTimeout plugin
 * 
 * @param tokenProvider TokenProvider for accessing stored tokens
 * @param sessionManager SessionManager for handling token refresh and session management
 * @return HttpClient configured with native Ktor plugins for comprehensive network handling
 */
fun createKtorClientWithInterceptor(
    tokenProvider: TokenProvider,
    sessionManager: SessionManager
): HttpClient {
    return HttpClient(CIO) {
        // Implements LLR-3.10.6: ContentNegotiation Plugin Implementation
        // Configure ContentNegotiation plugin with Json serializer using ignoreUnknownKeys=true
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
        
        // Implements LLR-3.10.5: Logging Plugin Implementation
        // Configure Logging plugin with level=LogLevel.BODY for comprehensive request/response logging
        install(Logging) {
            level = LogLevel.BODY
        }
        
        // Implements LLR-3.10.4: HttpTimeout Plugin Implementation
        // Configure HttpTimeout plugin with requestTimeoutMillis=30000, connectTimeoutMillis=10000, socketTimeoutMillis=30000
        install(HttpTimeout) {
            requestTimeoutMillis = 30000
            connectTimeoutMillis = 10000
            socketTimeoutMillis = 30000
        }
        
        // Implements LLR-3.10.3: Auth Plugin Bearer Implementation
        // Configure Auth plugin with bearer provider that loads tokens from TokenProvider and refreshes via SessionManager
        install(Auth) {
            bearer {
                // Load tokens from TokenProvider
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
                
                // Implements LLR-3.10.7: Token Refresh Logic Implementation
                // Implement refreshTokens block that calls sessionManager.handleUnauthorized() and retrieves new tokens from TokenProvider
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
        
        // Implements LLR-3.10.8: Default Request Headers Implementation
        // Implement defaultRequest block that sets Authorization header with bearer token from TokenProvider or AppConstants.JWT_TOKEN
        defaultRequest {
            val token = tokenProvider.getAccessToken()
            Log.d("KtorClient", "Setting authorization header: ${if (token != null) "Bearer token present" else "No token"}")
            
            if (token != null) {
                headers.append(HttpHeaders.Authorization, "Bearer $token")
            } else if (AppConstants.JWT_TOKEN != null) {
                headers.append(HttpHeaders.Authorization, "Bearer ${AppConstants.JWT_TOKEN}")
            }
        }
        
        // Implements LLR-3.10.2: HttpRequestRetry Plugin Implementation
        // Configure HttpRequestRetry plugin with maxRetries=3 and exponentialDelay(base=2.0, maxDelayMs=10000)
        install(HttpRequestRetry) {
            maxRetries = 3
            exponentialDelay(
                base = 2.0,
                maxDelayMs = 10000
            )
        }
    }
}
