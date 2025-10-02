package com.boatit.boatsharing.utils.prefmanager


import android.content.Context
import android.content.SharedPreferences
import com.boatit.boatsharing.ui.login.model.UserData
import com.boatit.boatsharing.utils.AppConstants

class SharedPrefManager(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("BoatUserPrefs", Context.MODE_PRIVATE)
    var appContext: Context = context

    companion object {
        private const val KEY_EMAIL = "email"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USERNAME = "username"
        private const val KEY_USERROLE = "userrole"
        private const val KEY_STEP = "step"
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
    }

    fun saveLoginData(userData: UserData) {
        AppConstants.USER_ID = userData.userId
        AppConstants.USER_NAME = userData.username
        sharedPreferences.edit().apply {
            putString(KEY_EMAIL, userData.email)
            putString(KEY_USER_ID, userData.userId)
            putString(KEY_USERNAME, userData.username)
            putString(KEY_USERROLE, userData.role)
            putInt(KEY_STEP, userData.missingStep)
            putString(KEY_ACCESS_TOKEN, userData.accessToken)
            putString(KEY_REFRESH_TOKEN, userData.refreshToken)
            apply()
        }
    }

    fun saveMissingStep(userData: Int) {
        sharedPreferences.edit().apply {
            putInt(KEY_STEP, userData)
            apply()
        }
    }

    fun getUserId(): String? {
        return sharedPreferences.getString(KEY_USER_ID, null)
    }

    fun getUserData(): UserData? {
        val email = sharedPreferences.getString(KEY_EMAIL, null) ?: return null
        val userId = sharedPreferences.getString(KEY_USER_ID, null) ?: return null
        val username = sharedPreferences.getString(KEY_USERNAME, null) ?: return null
        val userrole = sharedPreferences.getString(KEY_USERROLE, null) ?: return null
        val step = sharedPreferences.getInt(KEY_STEP, 0)
        val accessToken = sharedPreferences.getString(KEY_ACCESS_TOKEN, null) ?: return null
        val refreshToken = sharedPreferences.getString(KEY_REFRESH_TOKEN, null) ?: return null

        return UserData(
            email = email,
            password = "",
            userId = userId,
            username = username,
            role = userrole,
            missingStep = step,
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }

    fun clearUserData() {
        sharedPreferences.edit().clear().commit()
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
