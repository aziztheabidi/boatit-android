package com.boatit.boatsharing.network.di

/**
 * Data class representing an API error response
 * 
 * Implements LLR-0.8.1: ApiError Field Layout Implementation
 * 
 * Field layout with specified bit positions and memory alignment:
 * - Int field: 4 bytes (0-31)
 * - String reference: 8 bytes (32-95)
 * - Additional error context fields
 */
data class ApiError(
    val status: Int,                 // Bit position: 0-31
    val message: String,             // Bit position: 32-95
    val errorCode: String? = null,   // Bit position: 96-159
    val timestamp: Long = System.currentTimeMillis(), // Bit position: 160-223
    val requestId: String? = null    // Bit position: 224-287
) {
    
    /**
     * Validation constraints for ApiError fields
     */
    init {
        // LLR-0.8.1: ApiError Field Layout Implementation
        validateApiError()
    }
    
    /**
     * Validates ApiError field constraints
     */
    private fun validateApiError() {
        // Validate status code
        require(status in 100..599) {
            "Status code must be between 100 and 599"
        }
        
        // Validate message
        require(message.isNotBlank()) {
            "Error message cannot be blank"
        }
        require(message.length <= 500) {
            "Error message cannot exceed 500 characters"
        }
        
        // Validate error code if present
        errorCode?.let { code ->
            require(code.isNotBlank()) {
                "Error code cannot be blank"
            }
            require(code.length <= 50) {
                "Error code cannot exceed 50 characters"
            }
            require(code.matches(Regex("^[A-Z0-9_]+$"))) {
                "Error code must contain only uppercase letters, numbers, and underscores"
            }
        }
        
        // Validate timestamp
        require(timestamp > 0) {
            "Timestamp must be positive"
        }
        
        // Validate request ID if present
        requestId?.let { id ->
            require(id.isNotBlank()) {
                "Request ID cannot be blank"
            }
            require(id.length <= 100) {
                "Request ID cannot exceed 100 characters"
            }
        }
    }
    
    /**
     * Check if this is a client error (4xx)
     */
    fun isClientError(): Boolean {
        return status in 400..499
    }
    
    /**
     * Check if this is a server error (5xx)
     */
    fun isServerError(): Boolean {
        return status in 500..599
    }
    
    /**
     * Check if this is a network error
     */
    fun isNetworkError(): Boolean {
        return status in 100..199
    }
    
    /**
     * Get error category for logging and handling
     */
    fun getErrorCategory(): String {
        return when {
            isClientError() -> "CLIENT_ERROR"
            isServerError() -> "SERVER_ERROR"
            isNetworkError() -> "NETWORK_ERROR"
            else -> "UNKNOWN_ERROR"
        }
    }
    
    /**
     * Get error summary for logging (without exposing sensitive data)
     */
    fun getErrorSummary(): String {
        return "ApiError(" +
                "status=$status, " +
                "category=${getErrorCategory()}, " +
                "message='$message', " +
                "errorCode=${errorCode ?: "null"}, " +
                "requestId=${requestId ?: "null"}, " +
                "timestamp=$timestamp" +
                ")"
    }
    
    /**
     * Create a copy with updated timestamp
     */
    fun withUpdatedTimestamp(): ApiError {
        return copy(timestamp = System.currentTimeMillis())
    }
    
    companion object {
        // Validation constants
        private const val MAX_MESSAGE_LENGTH = 500
        private const val MAX_ERROR_CODE_LENGTH = 50
        private const val MAX_REQUEST_ID_LENGTH = 100
        
        private val ERROR_CODE_PATTERN = Regex("^[A-Z0-9_]+$")
        
        /**
         * Create a client error (4xx)
         */
        fun createClientError(
            status: Int,
            message: String,
            errorCode: String? = null,
            requestId: String? = null
        ): ApiError {
            require(status in 400..499) {
                "Client error status must be between 400 and 499"
            }
            return ApiError(status, message, errorCode, System.currentTimeMillis(), requestId)
        }
        
        /**
         * Create a server error (5xx)
         */
        fun createServerError(
            status: Int,
            message: String,
            errorCode: String? = null,
            requestId: String? = null
        ): ApiError {
            require(status in 500..599) {
                "Server error status must be between 500 and 599"
            }
            return ApiError(status, message, errorCode, System.currentTimeMillis(), requestId)
        }
        
        /**
         * Create a network error
         */
        fun createNetworkError(
            message: String,
            errorCode: String? = null,
            requestId: String? = null
        ): ApiError {
            return ApiError(100, message, errorCode, System.currentTimeMillis(), requestId)
        }
        
        /**
         * Create an unauthorized error (401)
         */
        fun createUnauthorizedError(
            message: String = "Unauthorized access",
            requestId: String? = null
        ): ApiError {
            return createClientError(401, message, "UNAUTHORIZED", requestId)
        }
        
        /**
         * Create a forbidden error (403)
         */
        fun createForbiddenError(
            message: String = "Access forbidden",
            requestId: String? = null
        ): ApiError {
            return createClientError(403, message, "FORBIDDEN", requestId)
        }
        
        /**
         * Create a not found error (404)
         */
        fun createNotFoundError(
            message: String = "Resource not found",
            requestId: String? = null
        ): ApiError {
            return createClientError(404, message, "NOT_FOUND", requestId)
        }
        
        /**
         * Create an internal server error (500)
         */
        fun createInternalServerError(
            message: String = "Internal server error",
            requestId: String? = null
        ): ApiError {
            return createServerError(500, message, "INTERNAL_ERROR", requestId)
        }
    }
}
