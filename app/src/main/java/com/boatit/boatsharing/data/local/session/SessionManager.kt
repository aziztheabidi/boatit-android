package com.boatit.boatsharing.data.local.session

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.boatit.boatsharing.data.local.prefmanager.SharedPrefManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Centralized session manager for the entire application
 * Single source of truth for authentication state and session lifecycle
 *
 * Responsibilities:
 * - Manage user session lifecycle (login, logout, session validation)
 * - Store and retrieve authentication tokens securely
 * - Track session state (authenticated, not authenticated, expired)
 * - Emit session events for observers
 * - Handle logout with proper cleanup
 *
 * Design:
 * - Single instance (singleton)
 * - Thread-safe operations
 * - Reactive state management
 * - Clear separation of concerns
 *
 * Example usage:
 * ```
 * // Inject into ViewModel
 * class LoginViewModel(
 *     private val sessionManager: SessionManager
 * ) : ViewModel() {
 *     fun login(email: String, password: String) {
 *         // ... authentication logic
 *         sessionManager.onLoginSuccess(token, refreshToken, user)
 *     }
 * }
 *
 * // Observe session state
 * sessionManager.sessionState.collect { state ->
 *     when (state) {
 *         SessionState.Authenticated -> navigateToHome()
 *         SessionState.NotAuthenticated -> navigateToLogin()
 *         SessionState.SessionExpired -> showExpiredDialog()
 *     }
 * }
 * ```
 */
class SessionManager(
    private val context: Context,
    private val prefManager: SharedPrefManager,
) {
    private val _sessionState = MutableStateFlow<SessionState>(SessionState.NotAuthenticated)
    val sessionState = _sessionState.asStateFlow()

    private val _sessionEvents = MutableStateFlow<SessionEvent?>(null)
    val sessionEvents = _sessionEvents.asStateFlow()

    private val _isAuthenticated = MutableLiveData<Boolean>(false)
    val isAuthenticated: LiveData<Boolean> = _isAuthenticated

    init {
        // Check if user has valid tokens on init
        if (hasValidTokens()) {
            _sessionState.value = SessionState.Authenticated
            _isAuthenticated.value = true
        }
    }

    /**
     * Called when login is successful
     * Stores tokens and updates session state
     */
    fun onLoginSuccess(
        accessToken: String,
        refreshToken: String,
        userId: String? = null,
        userRole: String? = null,
    ) {
        prefManager.saveTokens(accessToken, refreshToken)
        if (userId != null) prefManager.saveUserId(userId)
        if (userRole != null) prefManager.saveUserRole(userRole)

        _sessionState.value = SessionState.Authenticated
        _isAuthenticated.value = true
        emitSessionEvent(SessionEvent.LoginSuccess(userId ?: ""))
    }

    /**
     * Called when logout is required
     * Clears all session data and tokens
     */
    fun onLogout(reason: String = "User initiated logout") {
        prefManager.clearUserData()
        prefManager.clearTokens()

        val alreadyLoggedOut = _sessionState.value == SessionState.NotAuthenticated && !hasValidTokens()
        _sessionState.value = SessionState.NotAuthenticated
        _isAuthenticated.value = false
        if (!alreadyLoggedOut) {
            emitSessionEvent(SessionEvent.LogoutSuccess(reason))
        }
    }

    /**
     * Called when session expires or tokens become invalid
     * Triggers re-authentication flow
     */
    fun onSessionExpired() {
        prefManager.clearUserData()
        val alreadyExpired = _sessionState.value == SessionState.SessionExpired
        _sessionState.value = SessionState.SessionExpired
        _isAuthenticated.value = false
        if (!alreadyExpired) {
            emitSessionEvent(SessionEvent.SessionExpired)
        }
    }

    /**
     * Called when authentication fails
     */
    fun onAuthenticationError(message: String) {
        _sessionState.value = SessionState.AuthenticationError
        _isAuthenticated.value = false
        emitSessionEvent(SessionEvent.AuthenticationError(message))
    }

    /**
     * Update tokens (typically from token refresh)
     */
    fun updateTokens(accessToken: String, refreshToken: String) {
        prefManager.saveTokens(accessToken, refreshToken)
        if (_sessionState.value != SessionState.Authenticated) {
            _sessionState.value = SessionState.Authenticated
            _isAuthenticated.value = true
        }
    }

    /**
     * Check if current session has valid tokens
     */
    fun hasValidTokens(): Boolean {
        val accessToken = prefManager.getAccessToken()
        val refreshToken = prefManager.getRefreshToken()
        return !accessToken.isNullOrEmpty() && !refreshToken.isNullOrEmpty()
    }

    /**
     * Get current access token
     */
    fun getAccessToken(): String? = prefManager.getAccessToken()

    /**
     * Get current refresh token
     */
    fun getRefreshToken(): String? = prefManager.getRefreshToken()

    /**
     * Get current user ID
     */
    fun getUserId(): String? = prefManager.getUserId()

    /**
     * Get current user role
     */
    fun getUserRole(): String? = prefManager.getUserRole()

    /**
     * Check if user is authenticated
     */
    fun isUserAuthenticated(): Boolean = _sessionState.value == SessionState.Authenticated

    /**
     * Clear all session data (full reset)
     */
    fun clearAllSessionData() {
        prefManager.clearUserData()
        prefManager.clearTokens()
        _sessionState.value = SessionState.NotAuthenticated
        _isAuthenticated.value = false
    }

    /**
     * Get current session state
     */
    fun getCurrentSessionState(): SessionState = _sessionState.value

    /**
     * Emit session event to observers
     */
    private fun emitSessionEvent(event: SessionEvent) {
        _sessionEvents.value = event
    }
}

/**
 * Represents different states of user session
 */
sealed interface SessionState {
    object Authenticated : SessionState
    object NotAuthenticated : SessionState
    object SessionExpired : SessionState
    object AuthenticationError : SessionState
    object Loading : SessionState
}

