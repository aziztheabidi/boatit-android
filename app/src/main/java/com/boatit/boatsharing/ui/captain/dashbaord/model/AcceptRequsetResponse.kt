package com.boatit.boatsharing.ui.captain.dashbaord.model

import kotlinx.serialization.Serializable

@Serializable
data class AcceptVoyageRequest(
    val Id: String,
    val CaptainUserId: String,
    val CaptainBookingLatitude: Double,
    val CaptainBookingLongitude: Double
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