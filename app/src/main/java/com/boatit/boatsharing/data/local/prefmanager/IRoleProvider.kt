package com.boatit.boatsharing.data.local.prefmanager

interface IRoleProvider {
    fun getRole(): String?

    fun saveRole(role: String)

    fun clearRole()
}
