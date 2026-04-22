package com.boatit.boatsharing.data.local.session

import com.boatit.boatsharing.ui.navigation.NavigationManager

class SessionController(
    private val clearSessionUseCase: ClearSessionUseCase,
) {
    fun logoutAndResolveRoute(): String {
        clearSessionUseCase(SessionClearReason.UserLogout)
        return NavigationManager.LOGIN_SCREEN
    }

    fun resolveRedirectRoute(event: SessionEvent): String? {
        return when (event) {
            SessionEvent.SessionExpired,
            SessionEvent.TokenRefreshFailed,
            SessionEvent.AccountDeactivated,
            -> {
                clearSessionUseCase(SessionClearReason.SessionInvalidated)
                NavigationManager.LOGIN_SCREEN
            }
            is SessionEvent.LoginSuccess,
            is SessionEvent.LogoutSuccess,
            is SessionEvent.AuthenticationError,
            -> null
        }
    }
}
