package com.boatit.boatsharing.utils.prefmanager

import android.content.Context
import android.content.SharedPreferences

class StatusProvider(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("CaptainPrefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_CAPTAIN_STATUS = "captain_status"
    }

    fun setCaptainStatus(isOnline: Boolean) {
        prefs.edit().putBoolean(KEY_CAPTAIN_STATUS, isOnline).apply()
    }

    fun isCaptainOnline(): Boolean {
        return prefs.getBoolean(KEY_CAPTAIN_STATUS, false) // Default: Offline
    }
}


