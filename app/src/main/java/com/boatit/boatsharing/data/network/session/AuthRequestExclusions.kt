package com.boatit.boatsharing.data.network.session

import com.boatit.boatsharing.data.network.di.ApiConstants

/**
 * Paths that may return 401 without meaning "the current session is dead"
 * (e.g. wrong password on login). Refresh is intentionally not listed: 401 there
 * means tokens are invalid and the user must sign in again.
 */
internal object AuthRequestExclusions {
    private val publicPathSuffixes =
        listOf(
            ApiConstants.Endpoints.LOGIN,
            ApiConstants.Endpoints.REGISTER,
            ApiConstants.Endpoints.FORGOTPASS,
            ApiConstants.Endpoints.VERIFY,
            ApiConstants.Endpoints.ADD,
        )

    fun isPublicAuthPath(encodedPath: String): Boolean =
        publicPathSuffixes.any { encodedPath.endsWith(it) }
}
