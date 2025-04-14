package com.boatit.boatsharing.ui.chat.model
import kotlinx.serialization.Serializable

@Serializable
data class ActiveVoyagersResponse(
    val Status: Int,
    val Message: String,
    val obj: List<VoyagerProfile>
)

@Serializable
data class VoyagerProfile(
    val PhoneNumber: String,
    val FirstName: String,
    val LastName: String,
    val Address: String,
    val DateOfBirth: String,
    val StripeEmail: String,
    val UserId: String,
    val ChangedOn: String,
    val ChangedBy: String
)
