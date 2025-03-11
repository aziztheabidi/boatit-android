package com.boatit.boatsharing.ui.signup.captain.model

import kotlinx.serialization.Serializable

@Serializable
data class SaveCaptainBoatRequest(
    val UserId: String,
    val Name: String,
    val Make: String,
    val Model: String,
    val Year: Int,
    val Size: Int,
    val Capacity: Int
)

@Serializable
data class SaveCaptainBoatResponse(
    val Status: Int,
    val Message: String
)

@Serializable
data class GetCaptainBoatResponse(
    val Status: Int,
    val Message: String,
    val obj: CaptainBoat
)

@Serializable
data class CaptainBoat(
    val Name: String,
    val Make: String,
    val Model: String,
    val Year: Int,
    val Size: Int,
    val Capacity: Int,
    val UserId: String,
    val ChangedOn: String,
    val ChangedBy: String
)