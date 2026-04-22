package com.boatit.boatsharing.features.captain.dashboard.model

import kotlinx.serialization.Serializable

/** Wire / JSON DTOs for accept / decline voyage actions. */

@Serializable
data class AcceptVoyageRequest(
    val Id: String,
    val CaptainUserId: String,
    val CaptainBookingLatitude: Double,
    val CaptainBookingLongitude: Double,
)

@Serializable
data class DeclineRequest(
    val Id: String,
)

@Serializable
data class AcceptVoyageResponse(
    val Status: Int,
    val Message: String,
)
