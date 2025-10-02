package com.boatit.boatsharing.utils.prefmanager

import android.content.Context


class RoleProvider(context: Context) {

    private val sharedPrefManager = SharedPrefManager(context)

    fun getRole(): String? {
        return sharedPrefManager.getUserData()?.role
    }

    fun saveRole(role: String) {
        val userData = sharedPrefManager.getUserData()?.copy(role = role)
        if (userData != null) {
            sharedPrefManager.saveLoginData(userData)
        }
    }

    fun clearRole() {
        sharedPrefManager.clearUserData()
    }
}