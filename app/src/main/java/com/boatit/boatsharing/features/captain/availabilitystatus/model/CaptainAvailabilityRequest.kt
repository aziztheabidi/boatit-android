package com.boatit.boatsharing.features.captain.availabilitystatus.model

import kotlinx.serialization.Serializable

@Serializable
data class CaptainAvailabilityRequest(
    val UserId: String,
    val IsAvailable: Boolean,
)
