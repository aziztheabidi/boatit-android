package com.boatit.boatsharing.ui.captain.availablitystatus.model

import kotlinx.serialization.Serializable

@Serializable
data class CaptainAvailabilityRequest(
    val UserId: String,
    val IsAvailable: Boolean
)

@Serializable
data class CaptainAvailabilityResponse(
    val Status: Int,
    val Message: String,
)