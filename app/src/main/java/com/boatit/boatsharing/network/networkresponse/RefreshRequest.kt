package com.boatit.boatsharing.network.networkresponse

import kotlinx.serialization.Serializable

/**
 * Data class representing a token refresh request
 * 
 * Implements LLR-0.3.1: RefreshRequest Field Layout Implementation
 * 
 * Field layout with specified bit positions and memory alignment:
 * - String references: 8 bytes each (0-63, 64-127)
 * - Validation constraints for token format and length
 */
@Serializable
data class RefreshRequest(
    val accessToken: String?,    // Bit position: 0-63
    val refreshToken: String?    // Bit position: 64-127
) {
    
    /**
     * Validation constraints for RefreshRequest fields
     */
    init {
        // LLR-0.3.1: RefreshRequest Field Layout Implementation
        validateRefreshRequest()
    }
    
    /**
     * Validates RefreshRequest field constraints
     */
    private fun validateRefreshRequest() {
        // Validate access token if present
        accessToken?.let { token ->
            require(token.isNotBlank()) {
                "Access token cannot be blank"
            }
            require(token.length in 10..2048) {
                "Access token length must be between 10 and 2048 characters"
            }
            require(token.matches(Regex("^[A-Za-z0-9._-]+$"))) {
                "Access token contains invalid characters"
            }
        }
        
        // Validate refresh token if present
        refreshToken?.let { token ->
            require(token.isNotBlank()) {
                "Refresh token cannot be blank"
            }
            require(token.length in 10..2048) {
                "Refresh token length must be between 10 and 2048 characters"
            }
            require(token.matches(Regex("^[A-Za-z0-9._-]+$"))) {
                "Refresh token contains invalid characters"
            }
        }
        
        // At least one token must be present
        require(accessToken != null || refreshToken != null) {
            "At least one token (access or refresh) must be provided"
        }
    }
    
    /**
     * Check if the request has valid tokens
     */
    fun hasValidTokens(): Boolean {
        return try {
            validateRefreshRequest()
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }
    
    /**
     * Get token summary for logging (without exposing actual tokens)
     */
    fun getTokenSummary(): String {
        return "RefreshRequest(" +
                "accessTokenPresent=${accessToken != null}, " +
                "refreshTokenPresent=${refreshToken != null}, " +
                "accessTokenLength=${accessToken?.length ?: 0}, " +
                "refreshTokenLength=${refreshToken?.length ?: 0}" +
                ")"
    }
    
    /**
     * Create a RefreshRequest with both tokens
     */
    companion object {
        /**
         * Create a RefreshRequest with both access and refresh tokens
         */
        fun create(
            accessToken: String,
            refreshToken: String
        ): RefreshRequest {
            return RefreshRequest(accessToken, refreshToken)
        }
        
        /**
         * Create a RefreshRequest with only access token
         */
        fun createWithAccessToken(accessToken: String): RefreshRequest {
            return RefreshRequest(accessToken, null)
        }
        
        /**
         * Create a RefreshRequest with only refresh token
         */
        fun createWithRefreshToken(refreshToken: String): RefreshRequest {
            return RefreshRequest(null, refreshToken)
        }
        
        // Token validation constants
        private const val MIN_TOKEN_LENGTH = 10
        private const val MAX_TOKEN_LENGTH = 2048
        private val TOKEN_PATTERN = Regex("^[A-Za-z0-9._-]+$")
    }
}
