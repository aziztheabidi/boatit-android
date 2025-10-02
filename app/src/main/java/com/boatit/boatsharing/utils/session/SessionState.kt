package com.boatit.boatsharing.utils.session

/**
 * Data class representing the current session state
 * 
 * Implements LLR-0.1.1: SessionState Field Layout Implementation
 * 
 * Field layout with specified bit positions and memory alignment:
 * - Boolean fields: 1 byte each (0-7, 8-15, 16-23, 24-31)
 * - String references: 8 bytes each (32-63, 64-95)
 * - Long fields: 8 bytes each (96-159, 160-223)
 * - Int fields: 4 bytes each (224-255, 256-287)
 */
data class SessionState(
    // Boolean fields (1 byte each, bit positions 0-31)
    val isAuthenticated: Boolean = false,           // Bit position: 0-7
    val isSessionExpired: Boolean = false,          // Bit position: 8-15
    val isTokenRefreshing: Boolean = false,        // Bit position: 16-23
    val isInMaintenanceMode: Boolean = false,      // Bit position: 24-31
    
    // String references (8 bytes each, bit positions 32-95)
    val userId: String? = null,                    // Bit position: 32-63
    val username: String? = null,                  // Bit position: 64-95
    val userRole: String? = null,                  // Bit position: 96-127
    val accessToken: String? = null,               // Bit position: 128-159
    val refreshToken: String? = null,              // Bit position: 160-191
    
    // Long fields (8 bytes each, bit positions 192-287)
    val lastActivityTimestamp: Long = System.currentTimeMillis(), // Bit position: 192-255
    val sessionTimeoutMinutes: Long = 30L,         // Bit position: 256-319
    
    // Int fields (4 bytes each, bit positions 288-351)
    val retryAttempts: Int = 0,                   // Bit position: 288-319
    val maxRetryAttempts: Int = 3,               // Bit position: 320-351
    
    // Additional fields for comprehensive session management
    val isSessionActive: Boolean = false,          // Bit position: 352-359
    val errorMessage: String? = null,             // Bit position: 360-391
    val isAccountDeactivated: Boolean = false    // Bit position: 392-399
) {
    
    /**
     * Validation constraints for SessionState fields
     */
    init {
        // LLR-0.1.1: SessionState Field Layout Implementation
        validateSessionState()
    }
    
    /**
     * Validates SessionState field constraints
     */
    private fun validateSessionState() {
        // Validate timeout constraints
        require(sessionTimeoutMinutes in 1L..480L) {
            "Session timeout must be between 1 and 480 minutes (8 hours)"
        }
        
        // Validate retry attempt constraints
        require(retryAttempts >= 0) {
            "Retry attempts cannot be negative"
        }
        
        require(maxRetryAttempts in 1..10) {
            "Max retry attempts must be between 1 and 10"
        }
        
        require(retryAttempts <= maxRetryAttempts) {
            "Current retry attempts cannot exceed max retry attempts"
        }
        
        // Validate timestamp constraints
        require(lastActivityTimestamp > 0) {
            "Last activity timestamp must be positive"
        }
        
        // Validate authentication state consistency
        if (isAuthenticated) {
            require(!userId.isNullOrBlank()) {
                "Authenticated sessions must have a valid userId"
            }
            require(!userRole.isNullOrBlank()) {
                "Authenticated sessions must have a valid userRole"
            }
        }
        
        // Validate token state consistency
        if (isTokenRefreshing) {
            require(isAuthenticated) {
                "Token refresh can only occur for authenticated sessions"
            }
        }
        
        // Validate session state consistency
        if (isSessionExpired) {
            require(!isAuthenticated) {
                "Expired sessions cannot be authenticated"
            }
        }
        
        if (isAccountDeactivated) {
            require(!isAuthenticated) {
                "Deactivated accounts cannot be authenticated"
            }
        }
    }
    
    /**
     * Check if session is still valid based on timeout and state
     */
    fun isSessionValid(): Boolean {
        val currentTime = System.currentTimeMillis()
        val timeoutMillis = sessionTimeoutMinutes * 60 * 1000
        val timeSinceLastActivity = currentTime - lastActivityTimestamp
        
        return isAuthenticated && 
               !isSessionExpired && 
               !isAccountDeactivated &&
               !isInMaintenanceMode &&
               timeSinceLastActivity < timeoutMillis
    }
    
    /**
     * Check if we can retry token refresh
     */
    fun canRetryTokenRefresh(): Boolean {
        return retryAttempts < maxRetryAttempts && 
               isAuthenticated && 
               !isSessionExpired &&
               !isAccountDeactivated
    }
    
    /**
     * Check if session is in a terminal state (cannot be recovered)
     */
    fun isTerminalState(): Boolean {
        return isSessionExpired || 
               isAccountDeactivated || 
               isInMaintenanceMode
    }
    
    /**
     * Get session status summary for logging
     */
    fun getSessionSummary(): String {
        return "SessionState(" +
                "authenticated=$isAuthenticated, " +
                "expired=$isSessionExpired, " +
                "refreshing=$isTokenRefreshing, " +
                "maintenance=$isInMaintenanceMode, " +
                "userId=$userId, " +
                "userRole=$userRole, " +
                "retryAttempts=$retryAttempts/$maxRetryAttempts, " +
                "timeout=${sessionTimeoutMinutes}min" +
                ")"
    }
    
    companion object {
        // Default values for session configuration
        const val DEFAULT_SESSION_TIMEOUT_MINUTES = 30L
        const val DEFAULT_MAX_RETRY_ATTEMPTS = 3
        const val MIN_SESSION_TIMEOUT_MINUTES = 1L
        const val MAX_SESSION_TIMEOUT_MINUTES = 480L // 8 hours
        const val MIN_MAX_RETRY_ATTEMPTS = 1
        const val MAX_MAX_RETRY_ATTEMPTS = 10
        
        /**
         * Create a default SessionState for unauthenticated users
         */
        fun createUnauthenticated(): SessionState {
            return SessionState(
                isAuthenticated = false,
                isSessionExpired = false,
                isTokenRefreshing = false,
                isInMaintenanceMode = false,
                userId = null,
                username = null,
                userRole = null,
                accessToken = null,
                refreshToken = null,
                lastActivityTimestamp = System.currentTimeMillis(),
                sessionTimeoutMinutes = DEFAULT_SESSION_TIMEOUT_MINUTES,
                retryAttempts = 0,
                maxRetryAttempts = DEFAULT_MAX_RETRY_ATTEMPTS,
                isSessionActive = false,
                errorMessage = null,
                isAccountDeactivated = false
            )
        }
        
        /**
         * Create a SessionState for authenticated users
         */
        fun createAuthenticated(
            userId: String,
            username: String,
            userRole: String,
            accessToken: String,
            refreshToken: String,
            sessionTimeoutMinutes: Long = DEFAULT_SESSION_TIMEOUT_MINUTES
        ): SessionState {
            return SessionState(
                isAuthenticated = true,
                isSessionExpired = false,
                isTokenRefreshing = false,
                isInMaintenanceMode = false,
                userId = userId,
                username = username,
                userRole = userRole,
                accessToken = accessToken,
                refreshToken = refreshToken,
                lastActivityTimestamp = System.currentTimeMillis(),
                sessionTimeoutMinutes = sessionTimeoutMinutes,
                retryAttempts = 0,
                maxRetryAttempts = DEFAULT_MAX_RETRY_ATTEMPTS,
                isSessionActive = true,
                errorMessage = null,
                isAccountDeactivated = false
            )
        }
    }
}
