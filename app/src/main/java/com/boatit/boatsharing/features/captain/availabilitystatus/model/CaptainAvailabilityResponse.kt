package com.boatit.boatsharing.features.captain.availabilitystatus.model

import kotlinx.serialization.Serializable

@Serializable
data class CaptainAvailabilityResponse(
    val Status: Int,
    val Message: String,
)
