package com.boatit.boatsharing.ui.signup.business.model

import kotlinx.serialization.Serializable

@Serializable
data class BusinessProfileRequest(
    val UserId: String,
    val PhoneNumber: String,
    val FirstName: String,
    val LastName: String,
    val Address: String,
    val DateOfBirth: String,
    val StripeEmail: String
)

@Serializable
data class SaveBusinessProfileResponse(
    val Status: Int,
    val Message: String,
    val obj: String?
)

@Serializable
data class GetBusinessProfileResponse(
    val Status: Int,
    val Message: String,
    val obj: BusinessProfileData?
)

@Serializable
data class BusinessProfileData(
    val UserId: String,
    val BusinessName: String?,
    val Address: String?,
    val PhoneNumber: String?,
    val Email: String?,
    val Website: String?
)