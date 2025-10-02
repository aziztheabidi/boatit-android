package com.boatit.boatsharing.network.networkresponse

/**
 * Sealed class representing different network response states
 * 
 * Implements LLR-0.7.1: NetworkResponse Field Layout Implementation
 * 
 * Sealed class hierarchy with specific type identifiers and bit positions:
 * - Each response type has a unique Type ID (0x01-0x03)
 * - Generic type T for flexible data handling
 * - Bit positions: 0-7, 8-15, 16-23
 */
sealed class NetworkResponse<T>(
    val data: T? = null,
    val message: String? = null
) {
    
    /**
     * Type ID for each response type (for serialization and debugging)
     */
    abstract val typeId: Int
    
    /**
     * Success response with data
     * Type ID: 0x01, Bit position: 0-7
     */
    class Success<T>(data: T) : NetworkResponse<T>(data) {
        override val typeId: Int = 0x01
        
        override fun toString(): String = "Success(typeId=$typeId, hasData=${data != null})"
    }
    
    /**
     * Error response with optional data and error message
     * Type ID: 0x02, Bit position: 8-15
     */
    class Error<T>(message: String, data: T? = null) : NetworkResponse<T>(data, message) {
        override val typeId: Int = 0x02
        
        override fun toString(): String = "Error(typeId=$typeId, message='$message', hasData=${data != null})"
    }
    
    /**
     * Loading response indicating request in progress
     * Type ID: 0x03, Bit position: 16-23
     */
    class Loading<T> : NetworkResponse<T>() {
        override val typeId: Int = 0x03
        
        override fun toString(): String = "Loading(typeId=$typeId)"
    }
    
    /**
     * Check if response is successful
     */
    fun isSuccess(): Boolean = this is Success
    
    /**
     * Check if response is an error
     */
    fun isError(): Boolean = this is Error
    
    /**
     * Check if response is loading
     */
    fun isLoading(): Boolean = this is Loading
    
    /**
     * Get data safely, returning null if not available
     */
    fun getDataOrNull(): T? = data
    
    /**
     * Get error message safely, returning null if not available
     */
    fun getErrorMessageOrNull(): String? = message
    
    /**
     * Get response summary for logging
     */
    fun getResponseSummary(): String {
        return when (this) {
            is Success -> "Success(hasData=${data != null})"
            is Error -> "Error(message='$message', hasData=${data != null})"
            is Loading -> "Loading"
        }
    }
    
    companion object {
        // Type ID constants for validation
        const val SUCCESS_TYPE_ID = 0x01
        const val ERROR_TYPE_ID = 0x02
        const val LOADING_TYPE_ID = 0x03
        
        // All valid type IDs
        val VALID_TYPE_IDS = setOf(
            SUCCESS_TYPE_ID,
            ERROR_TYPE_ID,
            LOADING_TYPE_ID
        )
        
        /**
         * Create a success response
         */
        fun <T> success(data: T): NetworkResponse<T> {
            return Success(data)
        }
        
        /**
         * Create an error response
         */
        fun <T> error(message: String, data: T? = null): NetworkResponse<T> {
            return Error(message, data)
        }
        
        /**
         * Create a loading response
         */
        fun <T> loading(): NetworkResponse<T> {
            return Loading()
        }
        
        /**
         * Validate type ID
         */
        fun isValidTypeId(typeId: Int): Boolean {
            return VALID_TYPE_IDS.contains(typeId)
        }
        
        /**
         * Get all response types
         */
        fun <T> getAllResponseTypes(): List<NetworkResponse<T>> {
            return listOf(
                Loading(),
                Error("Sample error"),
                success(null as T)
            )
        }
    }
}