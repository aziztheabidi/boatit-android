package com.boatit.boatsharing.data.local.session

sealed class SessionEvent {
    data class LoginSuccess(val userId: String) : SessionEvent()

    data class LogoutSuccess(val reason: String) : SessionEvent()

    data class AuthenticationError(val message: String) : SessionEvent()

    data object SessionExpired : SessionEvent()

    data object TokenRefreshFailed : SessionEvent()

    data object AccountDeactivated : SessionEvent()
}
