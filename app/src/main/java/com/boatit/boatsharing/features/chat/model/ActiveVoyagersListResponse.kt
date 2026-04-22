package com.boatit.boatsharing.features.chat.model
import kotlinx.serialization.Serializable

@Serializable
data class ActiveVoyagersResponse(
    val Status: Int,
    val Message: String,
    val obj: VoyagerRelationshipObj,
)

@Serializable
data class FollowResponse(
    val Status: Int,
    val Message: String,
    val obj: String? = null,
)

@Serializable
data class FollowRequest(
    val VoyagerUserId: String,
)

@Serializable
data class ComplainRequest(
    val VoyageId: String,
    val Description: String,
)

@Serializable
data class VoyagerRelationshipObj(
    val MySelf: VoyagerInfo,
    val Followed: List<VoyagerInfo>,
    val UnFollowed: List<VoyagerInfo>,
)

@Serializable
data class VoyagerInfo(
    val UserId: String,
    val FirstName: String,
    val LastName: String,
    val PhoneNumber: String,
    val Address: String,
    val DateOfBirth: String,
)
