package com.boatit.boatsharing.features.signup.general.model

import kotlinx.serialization.Serializable

/** Wire / JSON DTOs for general signup flows (field names match backend). */

@Serializable
data class RegistrationRequest(
    val Username: String? = null,
    val PhoneNumber: String? = null,
    val Email: String? = null,
)

@Serializable
data class RegistrationResponse(
    val Status: Int? = null,
    val Message: String? = null,
    val obj: String? = null,
)

@Serializable
data class VerifyEmailRequest(
    val Email: String,
    val OTP: String,
)

@Serializable
data class VerifyEmailResponse(
    val Status: Int? = null,
    val Message: String? = null,
    val obj: String? = null,
)

@Serializable
data class PasswordRequest(
    val Password: String,
)

@Serializable
data class VoyagerProfileRequest(
    val UserId: String?,
    val PhoneNumber: String,
    val FirstName: String,
    val LastName: String,
    val Address: String,
    val DateOfBirth: String,
    val StripeEmail: String,
)

@Serializable
data class VoyagerProfileResponse(
    val Status: Int? = null,
    val Message: String? = null,
)

@Serializable
data class GetVoyagerProfileResponse(
    val Status: Int? = null,
    val Message: String? = null,
    val obj: VoyagerProfileData? = null,
)

@Serializable
data class VoyagerProfileData(
    val UserId: String? = null,
    val PhoneNumber: String? = null,
    val FirstName: String? = null,
    val LastName: String? = null,
    val Address: String? = null,
    val DateOfBirth: String? = null,
    val StripeEmail: String? = null,
    val ChangedOn: String? = null,
    val ChangedBy: String? = null,
)
