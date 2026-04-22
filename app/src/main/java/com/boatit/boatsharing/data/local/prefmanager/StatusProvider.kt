package com.boatit.boatsharing.data.local.prefmanager

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class StatusProvider(context: Context) : ICaptainStatusProvider {
    private val masterKey =
        MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

    private val secure: SharedPreferences = createSecurePrefsWithRecovery(context)

    private val legacy: SharedPreferences =
        context.getSharedPreferences("CaptainPrefs", Context.MODE_PRIVATE)

    companion object {
        private const val TAG = "StatusProvider"
        private const val SECURE_PREF_FILE = "SecureCaptainPrefs"
        private const val KEY_CAPTAIN_STATUS = "captain_status"
        private const val KEY__MIGRATED__ = "_migrated_secure_"
    }

    private fun createSecurePrefsWithRecovery(context: Context): SharedPreferences {
        return try {
            EncryptedSharedPreferences.create(
                context,
                SECURE_PREF_FILE,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
            )
        } catch (firstError: Exception) {
            Log.w(TAG, "Encrypted status prefs failed. Clearing and retrying.", firstError)
            context.deleteSharedPreferences(SECURE_PREF_FILE)
            EncryptedSharedPreferences.create(
                context,
                SECURE_PREF_FILE,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
            )
        }
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

    override fun setCaptainStatus(isOnline: Boolean) {
        secure.edit().putBoolean(KEY_CAPTAIN_STATUS, isOnline).apply()
    }

    override fun isCaptainOnline(): Boolean {
        return secure.getBoolean(KEY_CAPTAIN_STATUS, false)
    }
}
