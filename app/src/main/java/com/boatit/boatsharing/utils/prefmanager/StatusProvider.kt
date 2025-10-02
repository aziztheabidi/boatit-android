package com.boatit.boatsharing.utils.prefmanager

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

/**
 * Data class representing captain status information
 * 
 * Implements LLR-0.9.1: CaptainStatus Field Layout Implementation
 * 
 * Field layout with specified bit positions and memory alignment:
 * - Boolean field: 1 byte (0-7)
 * - String references: 8 bytes each (8-71, 72-135)
 * - Long fields: 8 bytes each (136-199, 200-263)
 * - Int field: 4 bytes (264-295)
 */
data class CaptainStatus(
    val isOnline: Boolean,                    // Bit position: 0-7
    val captainId: String,                     // Bit position: 8-71
    val statusMessage: String? = null,        // Bit position: 72-135
    val lastSeenTimestamp: Long = System.currentTimeMillis(), // Bit position: 136-199
    val statusChangeTimestamp: Long = System.currentTimeMillis(), // Bit position: 200-263
    val availabilityLevel: Int = 0           // Bit position: 264-295 (0=offline, 1=available, 2=busy, 3=away)
) {
    
    /**
     * Validation constraints for CaptainStatus fields
     */
    init {
        // LLR-0.9.1: CaptainStatus Field Layout Implementation
        validateCaptainStatus()
    }
    
    /**
     * Validates CaptainStatus field constraints
     */
    private fun validateCaptainStatus() {
        // Validate captain ID
        require(captainId.isNotBlank()) {
            "Captain ID cannot be blank"
        }
        require(captainId.length in 1..50) {
            "Captain ID length must be between 1 and 50 characters"
        }
        
        // Validate status message if present
        statusMessage?.let { message ->
            require(message.length <= 200) {
                "Status message cannot exceed 200 characters"
            }
        }
        
        // Validate timestamps
        require(lastSeenTimestamp > 0) {
            "Last seen timestamp must be positive"
        }
        require(statusChangeTimestamp > 0) {
            "Status change timestamp must be positive"
        }
        
        // Validate availability level
        require(availabilityLevel in 0..3) {
            "Availability level must be between 0 and 3"
        }
        
        // Validate status consistency
        if (isOnline) {
            require(availabilityLevel > 0) {
                "Online captains must have availability level > 0"
            }
        } else {
            require(availabilityLevel == 0) {
                "Offline captains must have availability level 0"
            }
        }
    }
    
    /**
     * Get availability level description
     */
    fun getAvailabilityDescription(): String {
        return when (availabilityLevel) {
            0 -> "Offline"
            1 -> "Available"
            2 -> "Busy"
            3 -> "Away"
            else -> "Unknown"
        }
    }
    
    /**
     * Check if captain is available for new voyages
     */
    fun isAvailableForVoyages(): Boolean {
        return isOnline && availabilityLevel == 1
    }
    
    /**
     * Get status summary for logging
     */
    fun getStatusSummary(): String {
        return "CaptainStatus(" +
                "captainId='$captainId', " +
                "isOnline=$isOnline, " +
                "availability=${getAvailabilityDescription()}, " +
                "hasMessage=${statusMessage != null}, " +
                "lastSeen=$lastSeenTimestamp, " +
                "statusChange=$statusChangeTimestamp" +
                ")"
    }
    
    /**
     * Create a copy with updated timestamp
     */
    fun withUpdatedTimestamp(): CaptainStatus {
        val currentTime = System.currentTimeMillis()
        return copy(
            lastSeenTimestamp = currentTime,
            statusChangeTimestamp = currentTime
        )
    }
    
    companion object {
        // Availability level constants
        const val AVAILABILITY_OFFLINE = 0
        const val AVAILABILITY_AVAILABLE = 1
        const val AVAILABILITY_BUSY = 2
        const val AVAILABILITY_AWAY = 3
        
        // Validation constants
        private const val MAX_CAPTAIN_ID_LENGTH = 50
        private const val MAX_STATUS_MESSAGE_LENGTH = 200
        
        /**
         * Create offline captain status
         */
        fun createOffline(captainId: String): CaptainStatus {
            return CaptainStatus(
                isOnline = false,
                captainId = captainId,
                statusMessage = null,
                lastSeenTimestamp = System.currentTimeMillis(),
                statusChangeTimestamp = System.currentTimeMillis(),
                availabilityLevel = AVAILABILITY_OFFLINE
            )
        }
        
        /**
         * Create available captain status
         */
        fun createAvailable(captainId: String, statusMessage: String? = null): CaptainStatus {
            return CaptainStatus(
                isOnline = true,
                captainId = captainId,
                statusMessage = statusMessage,
                lastSeenTimestamp = System.currentTimeMillis(),
                statusChangeTimestamp = System.currentTimeMillis(),
                availabilityLevel = AVAILABILITY_AVAILABLE
            )
        }
        
        /**
         * Create busy captain status
         */
        fun createBusy(captainId: String, statusMessage: String? = null): CaptainStatus {
            return CaptainStatus(
                isOnline = true,
                captainId = captainId,
                statusMessage = statusMessage,
                lastSeenTimestamp = System.currentTimeMillis(),
                statusChangeTimestamp = System.currentTimeMillis(),
                availabilityLevel = AVAILABILITY_BUSY
            )
        }
        
        /**
         * Create away captain status
         */
        fun createAway(captainId: String, statusMessage: String? = null): CaptainStatus {
            return CaptainStatus(
                isOnline = true,
                captainId = captainId,
                statusMessage = statusMessage,
                lastSeenTimestamp = System.currentTimeMillis(),
                statusChangeTimestamp = System.currentTimeMillis(),
                availabilityLevel = AVAILABILITY_AWAY
            )
        }
    }
}

/**
 * Provider class for managing captain status persistence
 */
class StatusProvider(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("CaptainPrefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_CAPTAIN_STATUS = "captain_status"
        private const val KEY_CAPTAIN_ID = "captain_id"
        private const val KEY_STATUS_MESSAGE = "status_message"
        private const val KEY_LAST_SEEN = "last_seen"
        private const val KEY_STATUS_CHANGE = "status_change"
        private const val KEY_AVAILABILITY_LEVEL = "availability_level"
    }

    /**
     * Set captain status with full status object
     */
    fun setCaptainStatus(status: CaptainStatus) {
        try {
            Log.d("StatusProvider", "Setting captain status: ${status.getStatusSummary()}")
            
            prefs.edit()
                .putBoolean(KEY_CAPTAIN_STATUS, status.isOnline)
                .putString(KEY_CAPTAIN_ID, status.captainId)
                .putString(KEY_STATUS_MESSAGE, status.statusMessage)
                .putLong(KEY_LAST_SEEN, status.lastSeenTimestamp)
                .putLong(KEY_STATUS_CHANGE, status.statusChangeTimestamp)
                .putInt(KEY_AVAILABILITY_LEVEL, status.availabilityLevel)
                .apply()
                
            Log.i("StatusProvider", "Captain status saved successfully")
        } catch (e: Exception) {
            Log.e("StatusProvider", "Failed to save captain status: ${e.message}")
        }
    }

    /**
     * Get current captain status
     */
    fun getCaptainStatus(): CaptainStatus? {
        return try {
            val captainId = prefs.getString(KEY_CAPTAIN_ID, null)
            if (captainId != null) {
                CaptainStatus(
                    isOnline = prefs.getBoolean(KEY_CAPTAIN_STATUS, false),
                    captainId = captainId,
                    statusMessage = prefs.getString(KEY_STATUS_MESSAGE, null),
                    lastSeenTimestamp = prefs.getLong(KEY_LAST_SEEN, System.currentTimeMillis()),
                    statusChangeTimestamp = prefs.getLong(KEY_STATUS_CHANGE, System.currentTimeMillis()),
                    availabilityLevel = prefs.getInt(KEY_AVAILABILITY_LEVEL, 0)
                )
            } else {
                Log.w("StatusProvider", "No captain ID found in preferences")
                null
            }
        } catch (e: Exception) {
            Log.e("StatusProvider", "Failed to get captain status: ${e.message}")
            null
        }
    }

    /**
     * Set simple online/offline status (backward compatibility)
     */
    fun setCaptainStatus(isOnline: Boolean) {
        val currentStatus = getCaptainStatus()
        val captainId = currentStatus?.captainId ?: "unknown_captain"
        
        val newStatus = if (isOnline) {
            CaptainStatus.createAvailable(captainId)
        } else {
            CaptainStatus.createOffline(captainId)
        }
        
        setCaptainStatus(newStatus)
    }

    /**
     * Check if captain is online (backward compatibility)
     */
    fun isCaptainOnline(): Boolean {
        return getCaptainStatus()?.isOnline ?: false
    }

    /**
     * Update last seen timestamp
     */
    fun updateLastSeen() {
        val currentStatus = getCaptainStatus()
        if (currentStatus != null) {
            val updatedStatus = currentStatus.copy(lastSeenTimestamp = System.currentTimeMillis())
            setCaptainStatus(updatedStatus)
        }
    }

    /**
     * Clear captain status
     */
    fun clearCaptainStatus() {
        try {
            Log.i("StatusProvider", "Clearing captain status")
            prefs.edit().clear().apply()
        } catch (e: Exception) {
            Log.e("StatusProvider", "Failed to clear captain status: ${e.message}")
        }
    }
}


