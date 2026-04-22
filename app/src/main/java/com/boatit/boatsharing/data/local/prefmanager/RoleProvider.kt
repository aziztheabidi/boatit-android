package com.boatit.boatsharing.data.local.prefmanager

import android.content.Context

class RoleProvider(context: Context) : IRoleProvider {
    private val sharedPrefManager = SharedPrefManager(context)

    override fun getRole(): String? = sharedPrefManager.getUserData()?.Role

    override fun saveRole(role: String) {
        val userData =
            sharedPrefManager.getUserData()?.apply {
                Role = role
            }
        if (userData != null) {
            sharedPrefManager.saveLoginData(userData)
        }
    }

    override fun clearRole() {
        sharedPrefManager.clearUserData()
    }
}
