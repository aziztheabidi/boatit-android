package com.boatit.boatsharing.ui.login.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

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
    @SerialName("Status")
    val status: Int,                  // Bit position: 8-39
    @SerialName("Message")
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
        if (status in 200..299) {
            require(obj != null) {
                "Success response must include user data"
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
        return status in 200..299 && obj != null
    }
    
    /**
     * Get response summary for logging (without exposing sensitive data)
     */
    fun getResponseSummary(): String {
        return "LoginResponse(" +
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
    @SerialName("Email")
    val email: String,                // Bit position: 0-63
    @SerialName("Password")
    val password: String,             // Bit position: 64-127
    @SerialName("UserId")
    val userId: String,               // Bit position: 128-191
    @SerialName("Username")
    val username: String,             // Bit position: 192-255
    @SerialName("Role")
    var role: String,                 // Bit position: 256-319
    @SerialName("MissingStep")
    val missingStep: Int,             // Bit position: 320-351
    @SerialName("Accesstoken")
    var accessToken: String,          // Bit position: 352-415
    @SerialName("Refreshtoken")
    var refreshToken: String          // Bit position: 416-479
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
        // Validate email (allow temporary email for token storage)
        if (email.isNotBlank()) {
            require(email.contains("@") && email.contains(".")) {
                "Email must be in valid format"
            }
            require(email.length in 5..254) {
                "Email length must be between 5 and 254 characters"
            }
        }
        
        // Validate password (basic checks, not exposing actual password)
        // Allow empty password for temporary token storage objects
        if (password.isNotBlank()) {
            require(password.length >= 8) {
                "Password must be at least 8 characters long"
            }
        }
        
        // Validate userId (allow temporary userId for token storage)
        if (userId.isNotBlank()) {
            require(userId.length in 1..50) {
                "User ID length must be between 1 and 50 characters"
            }
        }
        
        // Validate username (can be email address or traditional username)
        if (username.isNotBlank()) {
            require(username.length in 3..254) { // Increased max length to accommodate email addresses
                "Username length must be between 3 and 254 characters"
            }
            // Allow email addresses or traditional usernames
            val isEmail = username.contains("@") && username.contains(".")
            val isTraditionalUsername = username.matches(Regex("^[a-zA-Z0-9._-]+$"))
            require(isEmail || isTraditionalUsername) {
                "Username must be a valid email address or contain only alphanumeric characters, dots, underscores, and hyphens"
            }
        }
        
        // Validate role (allow temporary role for token storage)
        if (role.isNotBlank()) {
            // Normalize role to lowercase for comparison (server sends capitalized)
            val normalizedRole = role.lowercase().trim()
            require(normalizedRole in VALID_ROLES) {
                "Role must be one of: ${VALID_ROLES.joinToString(", ")} (received: '$role')"
            }
        }
        
        // Validate missingStep
        require(missingStep in 0..MAX_MISSING_STEP) {
            "Missing step must be between 0 and $MAX_MISSING_STEP"
        }
        
        // Validate accessToken
        require(accessToken.isNotBlank()) {
            "Access token cannot be blank"
        }
        require(accessToken.length in MIN_TOKEN_LENGTH..MAX_TOKEN_LENGTH) {
            "Access token length must be between $MIN_TOKEN_LENGTH and $MAX_TOKEN_LENGTH characters"
        }
        require(accessToken.matches(TOKEN_PATTERN)) {
            "Access token must contain only alphanumeric characters, dots, underscores, and hyphens"
        }
        
        // Validate refreshToken
        require(refreshToken.isNotBlank()) {
            "Refresh token cannot be blank"
        }
        require(refreshToken.length in MIN_TOKEN_LENGTH..MAX_TOKEN_LENGTH) {
            "Refresh token length must be between $MIN_TOKEN_LENGTH and $MAX_TOKEN_LENGTH characters"
        }
        require(refreshToken.matches(TOKEN_PATTERN)) {
            "Refresh token must contain only alphanumeric characters, dots, underscores, and hyphens"
        }
    }
    
    /**
     * Get user data summary for logging (without exposing sensitive data)
     */
    fun getUserSummary(): String {
        return "UserData(" +
                "email='${email.take(3)}***', " +
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
        
        val VALID_ROLES = listOf(
            "captain", "voyager", "business",
            "user", "admin", "member", "customer",
            "Captain", "Voyager", "Business", // Capitalized versions
            "USER", "ADMIN", "MEMBER", "CUSTOMER" // Uppercase versions
        )
        
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
