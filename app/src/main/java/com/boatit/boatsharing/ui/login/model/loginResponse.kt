package com.boatit.boatsharing.ui.login.model

import kotlinx.serialization.Serializable

/**
 * Data class representing a login response
 * 
 * Implements LLR-0.6.1: LoginResponse Field Layout Implementation
 * 
 * Field layout with specified bit positions and memory alignment:
 * - Boolean field: 1 byte (0-7)
 * - Int field: 4 bytes (8-39)
 * - String references: 8 bytes each (40-103, 104-167)
 * - Nested object reference: 8 bytes (168-231)
 */
@Serializable
data class LoginResponse(
    val isSuccess: Boolean,           // Bit position: 0-7
    val status: Int,                  // Bit position: 8-39
    val message: String,              // Bit position: 40-103
    val obj: UserData? = null         // Bit position: 104-167
) {
    
    /**
     * Validation constraints for LoginResponse fields
     */
    init {
        // LLR-0.6.1: LoginResponse Field Layout Implementation
        validateLoginResponse()
    }
    
    /**
     * Validates LoginResponse field constraints
     */
    private fun validateLoginResponse() {
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
                "Success response must include user data"
            }
        } else {
            require(status !in 200..299) {
                "Failure response cannot have 2xx status code"
            }
        }
        
        // Validate user data if present
        obj?.let { userData ->
            userData.validateUserData()
        }
    }
    
    /**
     * Check if the response indicates successful login
     */
    fun isLoginSuccessful(): Boolean {
        return isSuccess && status in 200..299 && obj != null
    }
    
    /**
     * Get response summary for logging (without exposing sensitive data)
     */
    fun getResponseSummary(): String {
        return "LoginResponse(" +
                "success=$isSuccess, " +
                "status=$status, " +
                "message='$message', " +
                "hasUserData=${obj != null}" +
                ")"
    }
    
    companion object {
        /**
         * Create a successful login response
         */
        fun createSuccess(
            userData: UserData,
            message: String = "Login successful"
        ): LoginResponse {
            return LoginResponse(
                isSuccess = true,
                status = 200,
                message = message,
                obj = userData
            )
        }
        
        /**
         * Create a failed login response
         */
        fun createFailure(
            status: Int,
            message: String
        ): LoginResponse {
            return LoginResponse(
                isSuccess = false,
                status = status,
                message = message,
                obj = null
            )
        }
    }
}

/**
 * Data class representing user data in login response
 * 
 * Implements LLR-0.5.1: UserData Field Layout Implementation
 * 
 * Field layout with specified bit positions and memory alignment:
 * - String references: 8 bytes each (0-63, 64-127, 128-191, 192-255, 256-319, 320-383, 384-447, 448-511)
 * - Int field: 4 bytes (512-543)
 */
@Serializable
data class UserData(
    val email: String,                // Bit position: 0-63
    val password: String,             // Bit position: 64-127
    val userId: String,               // Bit position: 128-191
    val username: String,             // Bit position: 192-255
    val role: String,                 // Bit position: 256-319
    val missingStep: Int,             // Bit position: 320-351
    val accessToken: String,          // Bit position: 352-415
    val refreshToken: String          // Bit position: 416-479
) {
    
    /**
     * Validation constraints for UserData fields
     */
    init {
        // LLR-0.5.1: UserData Field Layout Implementation
        validateUserData()
    }
    
    /**
     * Validates UserData field constraints
     */
    fun validateUserData() {
        // Validate email
        require(email.isNotBlank()) {
            "Email cannot be blank"
        }
        require(email.contains("@") && email.contains(".")) {
            "Email must be in valid format"
        }
        require(email.length in 5..254) {
            "Email length must be between 5 and 254 characters"
        }
        
        // Validate password (basic checks, not exposing actual password)
        require(password.isNotBlank()) {
            "Password cannot be blank"
        }
        require(password.length >= 8) {
            "Password must be at least 8 characters long"
        }
        
        // Validate userId
        require(userId.isNotBlank()) {
            "User ID cannot be blank"
        }
        require(userId.length in 1..50) {
            "User ID length must be between 1 and 50 characters"
        }
        
        // Validate username
        require(username.isNotBlank()) {
            "Username cannot be blank"
        }
        require(username.length in 3..50) {
            "Username length must be between 3 and 50 characters"
        }
        require(username.matches(Regex("^[a-zA-Z0-9._-]+$"))) {
            "Username contains invalid characters"
        }
        
        // Validate role
        require(role.isNotBlank()) {
            "Role cannot be blank"
        }
        require(role in listOf("captain", "voyager", "business")) {
            "Role must be one of: captain, voyager, business"
        }
        
        // Validate missing step
        require(missingStep >= 0) {
            "Missing step cannot be negative"
        }
        require(missingStep <= 10) {
            "Missing step cannot exceed 10"
        }
        
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
     * Get user data summary for logging (without exposing sensitive data)
     */
    fun getUserSummary(): String {
        return "UserData(" +
                "email='$email', " +
                "userId='$userId', " +
                "username='$username', " +
                "role='$role', " +
                "missingStep=$missingStep, " +
                "hasPassword=${password.isNotBlank()}, " +
                "hasAccessToken=${accessToken.isNotBlank()}, " +
                "hasRefreshToken=${refreshToken.isNotBlank()}" +
                ")"
    }
    
    /**
     * Check if user has completed all required steps
     */
    fun isRegistrationComplete(): Boolean {
        return missingStep == 0
    }
    
    /**
     * Check if user is authenticated (has valid tokens)
     */
    fun isAuthenticated(): Boolean {
        return accessToken.isNotBlank() && refreshToken.isNotBlank()
    }
    
    companion object {
        // Validation constants
        private const val MIN_EMAIL_LENGTH = 5
        private const val MAX_EMAIL_LENGTH = 254
        private const val MIN_PASSWORD_LENGTH = 8
        private const val MIN_USERNAME_LENGTH = 3
        private const val MAX_USERNAME_LENGTH = 50
        private const val MIN_USER_ID_LENGTH = 1
        private const val MAX_USER_ID_LENGTH = 50
        private const val MIN_TOKEN_LENGTH = 10
        private const val MAX_TOKEN_LENGTH = 2048
        private const val MAX_MISSING_STEP = 10
        
        private val USERNAME_PATTERN = Regex("^[a-zA-Z0-9._-]+$")
        private val TOKEN_PATTERN = Regex("^[A-Za-z0-9._-]+$")
        
        val VALID_ROLES = listOf("captain", "voyager", "business")
        
        /**
         * Create UserData for testing purposes
         */
        fun createTestUser(
            email: String,
            username: String,
            role: String,
            accessToken: String,
            refreshToken: String
        ): UserData {
            return UserData(
                email = email,
                password = "testPassword123",
                userId = "test_${System.currentTimeMillis()}",
                username = username,
                role = role,
                missingStep = 0,
                accessToken = accessToken,
                refreshToken = refreshToken
            )
        }
    }
}
