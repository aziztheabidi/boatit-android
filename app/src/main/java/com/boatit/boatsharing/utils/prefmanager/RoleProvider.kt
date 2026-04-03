package com.boatit.boatsharing.utils.prefmanager

import android.content.Context

class RoleProvider(context: Context) {

    private val sharedPrefManager = SharedPrefManager(context)

    fun getRole(): String? = sharedPrefManager.getUserData()?.Role

    fun saveRole(role: String) {
        val userData = sharedPrefManager.getUserData()?.apply {
            Role = role
        }
        if (userData != null) {
            sharedPrefManager.saveLoginData(userData)
        }
    }

    fun clearRole() {
        sharedPrefManager.clearUserData()
    }
}
