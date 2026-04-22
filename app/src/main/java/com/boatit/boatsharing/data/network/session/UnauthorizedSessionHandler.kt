package com.boatit.boatsharing.data.network.session

import android.os.Handler
import android.os.Looper
import com.boatit.boatsharing.data.local.session.SessionManager
import com.boatit.boatsharing.data.network.di.invalidateTokens
import io.ktor.client.HttpClient
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Single entry for "401 / unauthorized" so Ktor, OkHttp, and refresh failures
 * share one policy: clear Ktor bearer cache on the main thread, then expire the app session.
 */
class UnauthorizedSessionHandler(
    private val sessionManager: SessionManager,
    private val httpClientProvider: () -> HttpClient,
) {
    private val mainHandler = Handler(Looper.getMainLooper())
    private val inFlight = AtomicBoolean(false)

    fun handleUnauthorizedResponse(encodedPath: String) {
        if (AuthRequestExclusions.isPublicAuthPath(encodedPath)) return
        if (!inFlight.compareAndSet(false, true)) return
        runCatching { httpClientProvider().invalidateTokens() }
        mainHandler.post {
            try {
                sessionManager.onSessionExpired()
            } finally {
                inFlight.set(false)
            }
        }
    }
}
