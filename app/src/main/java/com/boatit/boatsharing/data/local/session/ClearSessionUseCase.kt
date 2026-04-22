package com.boatit.boatsharing.data.local.session

import com.boatit.boatsharing.data.network.di.invalidateTokens
import io.ktor.client.HttpClient

enum class SessionClearReason {
    /** User chose sign out from the app menu. */
    UserLogout,

    /** Tokens invalid, refresh failed, account deactivated, or forced re-login. */
    SessionInvalidated,
}

class ClearSessionUseCase(
    private val sessionManager: SessionManager,
    private val httpClientProvider: () -> HttpClient,
) {
    operator fun invoke(reason: SessionClearReason = SessionClearReason.SessionInvalidated) {
        runCatching { httpClientProvider().invalidateTokens() }
        when (reason) {
            SessionClearReason.UserLogout ->
                sessionManager.onLogout("User initiated logout")
            SessionClearReason.SessionInvalidated ->
                sessionManager.onSessionExpired()
        }
    }
}
