package com.boatit.boatsharing.features.signup.captain.model

import kotlinx.serialization.Serializable

@Serializable
data class CaptainProfileRequest(
    val UserId: String,
    val PhoneNumber: String,
    val FirstName: String,
    val LastName: String,
    val Address: String,
    val DateOfBirth: String,
    val StripeEmail: String,
)

@Serializable
data class CaptainProfileResponse(
    val Status: Int,
    val Message: String,
)

@Serializable
data class GetCaptainProfileResponse(
    val Status: Int,
    val Message: String,
    val obj: CaptainProfile,
)

@Serializable
data class CaptainProfile(
    val PhoneNumber: String,
    val FirstName: String,
    val LastName: String,
    val Address: String,
    val DateOfBirth: String,
    val StripeEmail: String,
    val UserId: String,
    val ChangedOn: String,
    val ChangedBy: String,
)
