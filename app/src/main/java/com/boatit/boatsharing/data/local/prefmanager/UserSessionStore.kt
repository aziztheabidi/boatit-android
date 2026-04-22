package com.boatit.boatsharing.data.local.prefmanager

class UserSessionStore(private val sharedPrefManager: SharedPrefManager) {
    fun currentUserId(): String = sharedPrefManager.getUserId().orEmpty()

    fun currentUserName(): String = sharedPrefManager.getUserData()?.Username.orEmpty()
}
