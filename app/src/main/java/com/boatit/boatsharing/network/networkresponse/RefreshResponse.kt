package com.boatit.boatsharing.network.networkresponse

import kotlinx.serialization.Serializable

/**
 * Data class representing a token refresh response
 * 
 * Implements LLR-0.4.1: TokenResponse Field Layout Implementation
 * 
 * Field layout with specified bit positions and memory alignment:
 * - Int field: 4 bytes (0-31)
 * - String references: 8 bytes each (32-95, 96-159)
 * - Nested object reference: 8 bytes (160-223)
 */
@Serializable
data class RefreshResponse(
    val isSuccess: Boolean,           // Bit position: 0-7
    val status: Int,                 // Bit position: 8-39
    val message: String,             // Bit position: 40-103
    val obj: TokenData?              // Bit position: 104-167
) {
    
    /**
     * Validation constraints for RefreshResponse fields
     */
    init {
        // LLR-0.4.1: TokenResponse Field Layout Implementation
        validateRefreshResponse()
    }
    
    /**
     * Validates RefreshResponse field constraints
     */
    private fun validateRefreshResponse() {
        // Validate status code
        require(status in 100..599) {
            "Status code must be between 100 and 599"
        }
        
        // Validate message
        require(message.isNotBlank()) {
            "Response message cannot be blank"
        }
        
        // Validate success consistency
        if (isSuccess) {
            require(status in 200..299) {
                "Success response must have 2xx status code"
            }
            require(obj != null) {
                "Success response must include token data"
            }
        } else {
            require(status !in 200..299) {
                "Failure response cannot have 2xx status code"
            }
        }
        
        // Validate token data if present
        obj?.let { tokenData ->
            tokenData.validateTokenData()
        }
    }
    
    /**
     * Check if the response indicates successful token refresh
     */
    fun isTokenRefreshSuccessful(): Boolean {
        return isSuccess && status in 200..299 && obj != null
    }
    
    /**
     * Get response summary for logging (without exposing tokens)
     */
    fun getResponseSummary(): String {
        return "RefreshResponse(" +
                "success=$isSuccess, " +
                "status=$status, " +
                "message='$message', " +
                "hasTokenData=${obj != null}" +
                ")"
    }
    
    companion object {
        /**
         * Create a successful refresh response
         */
        fun createSuccess(
            accessToken: String,
            refreshToken: String,
            message: String = "Token refresh successful"
        ): RefreshResponse {
            return RefreshResponse(
                isSuccess = true,
                status = 200,
                message = message,
                obj = TokenData(accessToken, refreshToken)
            )
        }
        
        /**
         * Create a failed refresh response
         */
        fun createFailure(
            status: Int,
            message: String
        ): RefreshResponse {
            return RefreshResponse(
                isSuccess = false,
                status = status,
                message = message,
                obj = null
            )
        }
    }
}

/**
 * Data class representing token data in refresh response
 * 
 * Field layout with specified bit positions and memory alignment:
 * - String references: 8 bytes each (0-63, 64-127)
 */
@Serializable
data class TokenData(
    val accessToken: String,      // Bit position: 0-63
    val refreshToken: String     // Bit position: 64-127
) {
    
    /**
     * Validation constraints for TokenData fields
     */
    init {
        validateTokenData()
    }
    
    /**
     * Validates TokenData field constraints
     */
    fun validateTokenData() {
        // Validate access token
        require(accessToken.isNotBlank()) {
            "Access token cannot be blank"
        }
        require(accessToken.length in 10..2048) {
            "Access token length must be between 10 and 2048 characters"
        }
        require(accessToken.matches(Regex("^[A-Za-z0-9._-]+$"))) {
            "Access token contains invalid characters"
        }
        
        // Validate refresh token
        require(refreshToken.isNotBlank()) {
            "Refresh token cannot be blank"
        }
        require(refreshToken.length in 10..2048) {
            "Refresh token length must be between 10 and 2048 characters"
        }
        require(refreshToken.matches(Regex("^[A-Za-z0-9._-]+$"))) {
            "Refresh token contains invalid characters"
        }
        
        // Tokens should be different
        require(accessToken != refreshToken) {
            "Access token and refresh token must be different"
        }
    }
    
    /**
     * Get token data summary for logging (without exposing actual tokens)
     */
    fun getTokenSummary(): String {
        return "TokenData(" +
                "accessTokenLength=${accessToken.length}, " +
                "refreshTokenLength=${refreshToken.length}, " +
                "tokensDifferent=${accessToken != refreshToken}" +
                ")"
    }
    
    companion object {
        // Token validation constants
        private const val MIN_TOKEN_LENGTH = 10
        private const val MAX_TOKEN_LENGTH = 2048
        private val TOKEN_PATTERN = Regex("^[A-Za-z0-9._-]+$")
    }
}
