package com.boatit.boatsharing.data.network.networkresponse

import kotlinx.serialization.Serializable

@Serializable
data class RefreshRequest(
    val accesstoken: String?,
    val refreshtoken: String?,
)
