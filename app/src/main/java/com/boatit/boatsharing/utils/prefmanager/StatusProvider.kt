package com.boatit.boatsharing.utils.prefmanager

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class StatusProvider(context: Context) {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val secure: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        "SecureCaptainPrefs", masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    private val legacy: SharedPreferences =
        context.getSharedPreferences("CaptainPrefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_CAPTAIN_STATUS = "captain_status"
        private const val KEY__MIGRATED__ = "_migrated_secure_"
    }

    init {
        migrateIfNeeded()
    }

    private fun migrateIfNeeded() {
        if (secure.getBoolean(KEY__MIGRATED__, false)) return
        val hadValue = legacy.contains(KEY_CAPTAIN_STATUS)
        if (hadValue) {
            val value = legacy.getBoolean(KEY_CAPTAIN_STATUS, false)
            secure.edit().putBoolean(KEY_CAPTAIN_STATUS, value).putBoolean(KEY__MIGRATED__, true).apply()
            legacy.edit().clear().apply()
        } else {
            secure.edit().putBoolean(KEY__MIGRATED__, true).apply()
        }
    }

    fun setCaptainStatus(isOnline: Boolean) {
        secure.edit().putBoolean(KEY_CAPTAIN_STATUS, isOnline).apply()
    }

    fun isCaptainOnline(): Boolean {
        return secure.getBoolean(KEY_CAPTAIN_STATUS, false)
    }
}



