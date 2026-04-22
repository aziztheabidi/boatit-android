package com.boatit.boatsharing.features.captain.dashboard.model

import kotlinx.serialization.Serializable

@Serializable
data class VoyageStartRequest(
    val Id: String,
    val OTP: String,
)

@Serializable
data class VoyageStartResponse(
    val Status: Int,
    val Message: String,
)
