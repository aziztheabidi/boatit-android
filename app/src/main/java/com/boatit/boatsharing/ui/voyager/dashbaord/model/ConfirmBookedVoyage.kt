package com.boatit.boatsharing.ui.voyager.dashbaord.model

import kotlinx.serialization.Serializable

@Serializable
data class ConfirmBookedVoyages(
    val Id: String
)


@Serializable
data class ConfirmBookedVoyageResponse(
    val Status: Int,
    val Message: String,
    val obj: String?
)

@Serializable
data class CancelBookedVoyages(
    val Id: String,
    val Reason: String
)


@Serializable
data class CancelBookedVoyageResponse(
    val Status: Int,
    val Message: String,
    val obj: String?
)

