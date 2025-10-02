package com.boatit.boatsharing.utils.session

/**
 * Sealed class representing different session events that can occur
 * throughout the application lifecycle.
 * 
 * Implements LLR-0.2.1: SessionEvent Type Layout Implementation
 * 
 * Sealed class hierarchy with specific type identifiers and bit positions:
 * - Each object type has a unique Type ID (0x01-0x07)
 * - Singleton objects for efficient memory usage and fast type checking
 * - Bit positions: 0-7, 8-15, 16-23, 24-31, 32-39, 40-47, 48-55
 */
sealed class SessionEvent {
    
    /**
     * Type ID for each event type (for serialization and debugging)
     */
    abstract val typeId: Int
    
    /**
     * User explicitly requested logout
     * Type ID: 0x01, Bit position: 0-7
     */
    object LogoutRequired : SessionEvent() {
        override val typeId: Int = 0x01
        
        override fun toString(): String = "LogoutRequired(typeId=$typeId)"
    }
    
    /**
     * Session has expired and user needs to login again
     * Type ID: 0x02, Bit position: 8-15
     */
    object SessionExpired : SessionEvent() {
        override val typeId: Int = 0x02
        
        override fun toString(): String = "SessionExpired(typeId=$typeId)"
    }
    
    /**
     * Token refresh failed, user needs to login again
     * Type ID: 0x03, Bit position: 16-23
     */
    object TokenRefreshFailed : SessionEvent() {
        override val typeId: Int = 0x03
        
        override fun toString(): String = "TokenRefreshFailed(typeId=$typeId)"
    }
    
    /**
     * User account has been deactivated
     * Type ID: 0x04, Bit position: 24-31
     */
    object AccountDeactivated : SessionEvent() {
        override val typeId: Int = 0x04
        
        override fun toString(): String = "AccountDeactivated(typeId=$typeId)"
    }
    
    /**
     * Maintenance mode is active
     * Type ID: 0x05, Bit position: 32-39
     */
    object MaintenanceMode : SessionEvent() {
        override val typeId: Int = 0x05
        
        override fun toString(): String = "MaintenanceMode(typeId=$typeId)"
    }
    
    /**
     * Force logout due to security reasons
     * Type ID: 0x06, Bit position: 40-47
     */
    object ForceLogout : SessionEvent() {
        override val typeId: Int = 0x06
        
        override fun toString(): String = "ForceLogout(typeId=$typeId)"
    }
    
    /**
     * Session restored successfully
     * Type ID: 0x07, Bit position: 48-55
     */
    object SessionRestored : SessionEvent() {
        override val typeId: Int = 0x07
        
        override fun toString(): String = "SessionRestored(typeId=$typeId)"
    }
    
    companion object {
        // Type ID constants for validation
        const val LOGOUT_REQUIRED_TYPE_ID = 0x01
        const val SESSION_EXPIRED_TYPE_ID = 0x02
        const val TOKEN_REFRESH_FAILED_TYPE_ID = 0x03
        const val ACCOUNT_DEACTIVATED_TYPE_ID = 0x04
        const val MAINTENANCE_MODE_TYPE_ID = 0x05
        const val FORCE_LOGOUT_TYPE_ID = 0x06
        const val SESSION_RESTORED_TYPE_ID = 0x07
        
        // All valid type IDs
        val VALID_TYPE_IDS = setOf(
            LOGOUT_REQUIRED_TYPE_ID,
            SESSION_EXPIRED_TYPE_ID,
            TOKEN_REFRESH_FAILED_TYPE_ID,
            ACCOUNT_DEACTIVATED_TYPE_ID,
            MAINTENANCE_MODE_TYPE_ID,
            FORCE_LOGOUT_TYPE_ID,
            SESSION_RESTORED_TYPE_ID
        )
        
        /**
         * Get SessionEvent by type ID
         */
        fun fromTypeId(typeId: Int): SessionEvent? {
            return when (typeId) {
                LOGOUT_REQUIRED_TYPE_ID -> LogoutRequired
                SESSION_EXPIRED_TYPE_ID -> SessionExpired
                TOKEN_REFRESH_FAILED_TYPE_ID -> TokenRefreshFailed
                ACCOUNT_DEACTIVATED_TYPE_ID -> AccountDeactivated
                MAINTENANCE_MODE_TYPE_ID -> MaintenanceMode
                FORCE_LOGOUT_TYPE_ID -> ForceLogout
                SESSION_RESTORED_TYPE_ID -> SessionRestored
                else -> null
            }
        }
        
        /**
         * Validate type ID
         */
        fun isValidTypeId(typeId: Int): Boolean {
            return VALID_TYPE_IDS.contains(typeId)
        }
        
        /**
         * Get all session events
         */
        fun getAllEvents(): List<SessionEvent> {
            return listOf(
                LogoutRequired,
                SessionExpired,
                TokenRefreshFailed,
                AccountDeactivated,
                MaintenanceMode,
                ForceLogout,
                SessionRestored
            )
        }
        
        /**
         * Get critical events that require immediate user action
         */
        fun getCriticalEvents(): List<SessionEvent> {
            return listOf(
                SessionExpired,
                TokenRefreshFailed,
                AccountDeactivated,
                ForceLogout
            )
        }
        
        /**
         * Get informational events
         */
        fun getInformationalEvents(): List<SessionEvent> {
            return listOf(
                LogoutRequired,
                MaintenanceMode,
                SessionRestored
            )
        }
    }
}
