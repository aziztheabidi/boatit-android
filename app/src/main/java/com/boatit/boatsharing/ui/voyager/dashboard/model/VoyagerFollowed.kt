package com.boatit.boatsharing.ui.voyager.dashboard.model
import kotlinx.serialization.Serializable


@Serializable
data class FollowedVoyagersResponse(
    val Status: Int,
    val Message: String,
    val obj: FollowedVoyagerData
)

@Serializable
data class FollowedVoyagerData(
    val MySelf: VoyagerProfile,
    val Followed: List<VoyagerProfile>
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
