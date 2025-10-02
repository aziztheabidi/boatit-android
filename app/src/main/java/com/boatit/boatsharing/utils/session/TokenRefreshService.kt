package com.boatit.boatsharing.utils.session

import android.util.Log
import com.boatit.boatsharing.network.networkresponse.RefreshRequest
import com.boatit.boatsharing.network.networkresponse.RefreshResponse
import com.boatit.boatsharing.utils.prefmanager.TokenProvider
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.delay
import java.util.Base64
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json

/**
 * Service responsible for handling token refresh operations
 * 
 * Implements LLR-2.1.1: Token Format Validation Implementation
 * Implements LLR-2.1.2: Token Expiry Detection Implementation
 * Implements LLR-2.2.1: Refresh Token Usage Implementation
 * Implements LLR-2.2.2: New Token Storage Implementation
 * Implements LLR-2.3.1: Malformed Response Detection Implementation
 * Implements LLR-2.3.2: Malformed Response Handling Implementation
 */
class TokenRefreshService(
    private val httpClient: HttpClient,
    private val tokenProvider: TokenProvider
) {

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _refreshAttempts = MutableStateFlow(0)
    val refreshAttempts: StateFlow<Int> = _refreshAttempts.asStateFlow()

    companion object {
        private const val MAX_REFRESH_ATTEMPTS = 3
        private const val REFRESH_DELAY_MS = 1000L
        private const val REFRESH_ENDPOINT = "/auth/refresh"

        // LLR-2.1.1: Token Format Validation Implementation
        private const val MIN_TOKEN_LENGTH = 10
        private const val MAX_TOKEN_LENGTH = 2048
        private const val TOKEN_PATTERN = "^[A-Za-z0-9._-]+$"
    }

    /**
     * LLR-2.1.1: Token Format Validation Implementation
     * LLR-2.1.2: Token Expiry Detection Implementation
     *
     * Validates token format and detects expiry
     */
    fun validateTokenFormat(token: String): Boolean {
        return try {
            Log.d("TokenRefreshService", "Validating token format")

            // LLR-2.1.1: Token Format Validation Implementation
            val isValidFormat = when {
                token.isBlank() -> {
                    Log.w("TokenRefreshService", "Token is blank")
                    false
                }

                token.length < MIN_TOKEN_LENGTH -> {
                    Log.w(
                        "TokenRefreshService",
                        "Token too short: ${token.length} < $MIN_TOKEN_LENGTH"
                    )
                    false
                }

                token.length > MAX_TOKEN_LENGTH -> {
                    Log.w(
                        "TokenRefreshService",
                        "Token too long: ${token.length} > $MAX_TOKEN_LENGTH"
                    )
                    false
                }

                !token.matches(TOKEN_PATTERN.toRegex()) -> {
                    Log.w("TokenRefreshService", "Token contains invalid characters")
                    false
                }

                else -> {
                    Log.d("TokenRefreshService", "Token format validation passed")
                    true
                }
            }

            // LLR-2.1.2: Token Expiry Detection Implementation
            val isNotExpired = if (isValidFormat) {
                isTokenNotExpired(token)
            } else {
                false
            }

            Log.i(
                "TokenRefreshService",
                "Token validation result: format=$isValidFormat, expired=${!isNotExpired}"
            )
            isValidFormat && isNotExpired

        } catch (e: Exception) {
            Log.e("TokenRefreshService", "Token validation failed: ${e.message}")
            false
        }
    }

    /**
     * LLR-2.2.1: Refresh Token Usage Implementation
     *
     * Makes refresh request using stored refresh token
     */
    suspend fun makeRefreshRequest(): RefreshResponse? {
        return try {
            Log.i("TokenRefreshService", "Making refresh token request")

            val refreshToken = tokenProvider.getRefreshToken()
            val accessToken = tokenProvider.getAccessToken()

            if (refreshToken.isNullOrBlank()) {
                Log.w("TokenRefreshService", "No refresh token available")
                return null
            }

            if (!validateTokenFormat(refreshToken)) {
                Log.w("TokenRefreshService", "Refresh token format validation failed")
                return null
            }

            val response: HttpResponse = httpClient.post(REFRESH_ENDPOINT) {
                contentType(ContentType.Application.Json)
                setBody(
                    RefreshRequest(
                        accessToken = accessToken ?: "",
                        refreshToken = refreshToken
                    )
                )
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    Log.i("TokenRefreshService", "Refresh request successful")
                    response.body<RefreshResponse>()
                }

                HttpStatusCode.Unauthorized -> {
                    Log.w("TokenRefreshService", "Refresh token unauthorized (401)")
                    null
                }

                else -> {
                    Log.w(
                        "TokenRefreshService",
                        "Refresh request failed with status: ${response.status}"
                    )
                    null
                }
            }

        } catch (e: Exception) {
            Log.e("TokenRefreshService", "Refresh request exception: ${e.message}")
            null
        }
    }

    /**
     * LLR-2.3.1: Malformed Response Detection Implementation
     *
     * Detects malformed responses from refresh endpoint
     */
    fun detectMalformedResponse(response: RefreshResponse): Boolean {
        return try {
            Log.d("TokenRefreshService", "Detecting malformed response")

            val isMalformed = when {
                response.obj == null -> {
                    Log.w("TokenRefreshService", "Response object is null")
                    true
                }

                response.obj.accessToken.isNullOrBlank() -> {
                    Log.w("TokenRefreshService", "Access token is null or blank")
                    true
                }

                response.obj.refreshToken.isNullOrBlank() -> {
                    Log.w("TokenRefreshService", "Refresh token is null or blank")
                    true
                }

                !validateTokenFormat(response.obj.accessToken) -> {
                    Log.w("TokenRefreshService", "New access token format validation failed")
                    true
                }

                !validateTokenFormat(response.obj.refreshToken) -> {
                    Log.w("TokenRefreshService", "New refresh token format validation failed")
                    true
                }

                else -> {
                    Log.d("TokenRefreshService", "Response validation passed")
                    false
                }
            }

            Log.i("TokenRefreshService", "Malformed response detection result: $isMalformed")
            isMalformed

        } catch (e: Exception) {
            Log.e("TokenRefreshService", "Malformed response detection failed: ${e.message}")
            true
        }
    }

    /**
     * LLR-2.3.2: Malformed Response Handling Implementation
     *
     * Handles malformed responses gracefully
     */
    fun handleMalformedResponse(response: RefreshResponse): Boolean {
        return try {
            Log.w("TokenRefreshService", "Handling malformed response")

            // Clear potentially corrupted tokens
            tokenProvider.clearTokens()

            // Log the malformed response for debugging
            Log.e(
                "TokenRefreshService", "Malformed response details: " +
                        "success=${response.isSuccess}, " +
                        "message=${response.message}, " +
                        "obj=${response.obj}"
            )

            false

        } catch (e: Exception) {
            Log.e("TokenRefreshService", "Failed to handle malformed response: ${e.message}")
            false
        }
    }

    /**
     * LLR-2.2.2: New Token Storage Implementation
     *
     * Processes refresh response and stores new tokens
     */
    fun processRefreshResponse(response: RefreshResponse): Boolean {
        return try {
            Log.i("TokenRefreshService", "Processing refresh response")

            // LLR-2.3.1: Malformed Response Detection Implementation
            if (detectMalformedResponse(response)) {
                Log.w("TokenRefreshService", "Response is malformed, handling gracefully")
                return handleMalformedResponse(response)
            }

            val newAccessToken = response.obj?.accessToken
            val newRefreshToken = response.obj?.refreshToken

            if (newAccessToken != null && newRefreshToken != null) {
                // LLR-2.2.2: New Token Storage Implementation
                tokenProvider.saveTokens(newAccessToken, newRefreshToken)

                Log.i("TokenRefreshService", "New tokens stored successfully")
                true
            } else {
                Log.w("TokenRefreshService", "Failed to extract tokens from response")
                false
            }

        } catch (e: Exception) {
            Log.e("TokenRefreshService", "Failed to process refresh response: ${e.message}")
            false
        }
    }

    /**
     * Main token refresh function that orchestrates all the smaller functions
     * This replaces the monolithic refreshToken() function
     */
    suspend fun refreshAccessToken(): Boolean {
        if (_isRefreshing.value) {
            Log.w("TokenRefreshService", "Token refresh already in progress")
            return false
        }

        return try {
            _isRefreshing.value = true
            _refreshAttempts.value++

            Log.i("TokenRefreshService", "Starting token refresh attempt ${_refreshAttempts.value}")

            // LLR-2.2.1: Refresh Token Usage Implementation
            val response = makeRefreshRequest()

            if (response != null) {
                // LLR-2.2.2: New Token Storage Implementation
                val success = processRefreshResponse(response)

                if (success) {
                    _refreshAttempts.value = 0 // Reset attempts on success
                    Log.i("TokenRefreshService", "Token refresh completed successfully")
                } else {
                    Log.w("TokenRefreshService", "Token refresh failed during processing")
                }

                success
            } else {
                Log.w("TokenRefreshService", "Token refresh request failed")
                false
            }

        } catch (e: Exception) {
            Log.e("TokenRefreshService", "Token refresh exception: ${e.message}")
            false
        } finally {
            _isRefreshing.value = false
        }
    }

    /**
     * Reset refresh attempts counter
     */
    fun resetRefreshAttempts() {
        _refreshAttempts.value = 0
        Log.d("TokenRefreshService", "Refresh attempts reset to 0")
    }

    /**
     * Check if we can attempt another refresh
     */
    fun canAttemptRefresh(): Boolean {
        val canAttempt = _refreshAttempts.value < MAX_REFRESH_ATTEMPTS
        Log.d(
            "TokenRefreshService",
            "Can attempt refresh: $canAttempt (attempts: ${_refreshAttempts.value}/$MAX_REFRESH_ATTEMPTS)"
        )
        return canAttempt
    }

    /**
     * Get the current refresh attempts count
     */
    fun getRefreshAttempts(): Int {
        return _refreshAttempts.value
    }
    
    /**
     * LLR-2.1.2: Token Expiry Detection Implementation
     * 
     * Parses JWT token to check if it's expired
     */
    private fun isTokenNotExpired(token: String): Boolean {
        return try {
            Log.d("TokenRefreshService", "Parsing JWT token for expiry")
            
            // JWT tokens have 3 parts separated by dots: header.payload.signature
            val parts = token.split(".")
            if (parts.size != 3) {
                Log.w("TokenRefreshService", "Invalid JWT format: expected 3 parts, got ${parts.size}")
                return false
            }
            
            // Decode the payload (second part)
            val payload = parts[1]
            val decodedPayload = try {
                // Add padding if needed for Base64 decoding
                val paddedPayload = payload + "=".repeat((4 - payload.length % 4) % 4)
                String(Base64.getDecoder().decode(paddedPayload))
            } catch (e: Exception) {
                Log.w("TokenRefreshService", "Failed to decode JWT payload: ${e.message}")
                return false
            }
            
            // Parse the payload JSON to get the expiry time
            val json = Json { ignoreUnknownKeys = true }
            val payloadData = json.decodeFromString<Map<String, Any>>(decodedPayload)
            
            // Get the 'exp' (expiry) claim
            val exp = payloadData["exp"]
            if (exp == null) {
                Log.w("TokenRefreshService", "JWT token missing 'exp' claim")
                return false
            }
            
            // Convert to timestamp and check if expired
            val expiryTime = when (exp) {
                is Number -> exp.toLong()
                is String -> exp.toLongOrNull() ?: 0L
                else -> {
                    Log.w("TokenRefreshService", "Invalid 'exp' claim type: ${exp::class.simpleName}")
                    return false
                }
            }
            
            val currentTime = System.currentTimeMillis() / 1000 // Convert to seconds
            val isNotExpired = expiryTime > currentTime
            
            Log.i("TokenRefreshService", "JWT expiry check: current=$currentTime, expiry=$expiryTime, valid=$isNotExpired")
            isNotExpired
            
        } catch (e: Exception) {
            Log.e("TokenRefreshService", "JWT expiry parsing failed: ${e.message}")
            false
        }
    }
}