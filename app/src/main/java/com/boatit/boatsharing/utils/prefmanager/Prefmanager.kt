package com.boatit.boatsharing.utils.prefmanager

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.boatit.boatsharing.ui.login.model.UserData
import com.boatit.boatsharing.utils.AppConstants

class SharedPrefManager(context: Context) {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val securePrefs: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        "SecureBoatUserPrefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    private val legacyPrefs: SharedPreferences =
        context.getSharedPreferences("BoatUserPrefs", Context.MODE_PRIVATE)

    var appContext: Context = context

    companion object {
        private const val KEY_EMAIL = "email"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USERNAME = "username"
        private const val KEY_USERROLE = "userrole"
        private const val KEY_STEP = "step"
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
        private const val KEY__MIGRATED__ = "_migrated_secure_"
    }

    init {
        migrateIfNeeded()
    }

    private fun migrateIfNeeded() {
        if (securePrefs.getBoolean(KEY__MIGRATED__, false)) return

        val all = legacyPrefs.all
        if (all.isNotEmpty()) {
            with(securePrefs.edit()) {
                for ((k, v) in all) {
                    when (v) {
                        is String -> putString(k, v)
                        is Int -> putInt(k, v)
                        is Boolean -> putBoolean(k, v)
                        is Float -> putFloat(k, v)
                        is Long -> putLong(k, v)
                        is Set<*> -> @Suppress("UNCHECKED_CAST")
                        putStringSet(k, v as? Set<String>)
                    }
                }
                putBoolean(KEY__MIGRATED__, true)
                apply()
            }
            // Wipe legacy after successful copy
            legacyPrefs.edit().clear().apply()
        } else {
            securePrefs.edit().putBoolean(KEY__MIGRATED__, true).apply()
        }
    }

    fun saveLoginData(userData: UserData) {
        AppConstants.USER_ID = userData.UserId
        AppConstants.USER_NAME = userData.Username
        securePrefs.edit().apply {
            putString(KEY_EMAIL, userData.Email)
            putString(KEY_USER_ID, userData.UserId)
            putString(KEY_USERNAME, userData.Username)
            putString(KEY_USERROLE, userData.Role)
            putInt(KEY_STEP, userData.MissingStep)
            putString(KEY_ACCESS_TOKEN, userData.Accesstoken)
            putString(KEY_REFRESH_TOKEN, userData.Refreshtoken)
            apply()
        }
    }

    fun saveMissingStep(userData: Int) {
        securePrefs.edit().apply {
            putInt(KEY_STEP, userData)
            apply()
        }
    }

    fun getUserId(): String? = securePrefs.getString(KEY_USER_ID, null)

    fun getUserData(): UserData? {
        val email = securePrefs.getString(KEY_EMAIL, null) ?: return null
        val userId = securePrefs.getString(KEY_USER_ID, null) ?: return null
        val username = securePrefs.getString(KEY_USERNAME, null) ?: return null
        val userrole = securePrefs.getString(KEY_USERROLE, null) ?: return null
        val step = securePrefs.getInt(KEY_STEP, 0)
        val accessToken = securePrefs.getString(KEY_ACCESS_TOKEN, null) ?: return null
        val refreshToken = securePrefs.getString(KEY_REFRESH_TOKEN, null) ?: return null

        return UserData(
            Email = email,
            Password = "",
            UserId = userId,
            Username = username,
            Role = userrole,
            MissingStep = step,
            Accesstoken = accessToken,
            Refreshtoken = refreshToken
        )
    }

    fun clearUserData() {
        securePrefs.edit().clear().commit()
        clearAppCache()
    }

    fun clearAppCache() {
        try {
            val cacheDir = appContext.cacheDir
            cacheDir?.deleteRecursively()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

