package com.boatit.boatsharing.ui.chat.model
import kotlinx.serialization.Serializable

@Serializable
data class ActiveVoyagersResponse(
    val Status: Int,
    val Message: String,
    val obj: VoyagerRelationshipObj
)

@Serializable
data class FollowResponse(
    val Status: Int,
    val Message: String,
    val obj: Int ? = null
)

@Serializable
data class FollowRequest(
    val VoyagerUsrId: String,
)

@Serializable
data class VoyagerRelationshipObj(
    val MySelf: VoyagerInfo,
    val Followed: List<VoyagerInfo>,
    val UnFollowed: List<VoyagerInfo>
)

@Serializable
data class VoyagerInfo(
    val UserId: String,
    val FirstName: String,
    val LastName: String,
    val PhoneNumber: String,
    val Address: String,
    val DateOfBirth: String
)



