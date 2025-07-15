package com.boatit.boatsharing.ui.voyager.dashbaord.model

import kotlinx.serialization.Serializable

@Serializable
data class VoyagerFollowBusinessRequest(
    val BusinessDockId: Int
)

@Serializable
data class VoyagerFollowBusinessResponse(
    val Status: Int?=null,
    val Message: String?=null,
    val obj: String?=null,
)