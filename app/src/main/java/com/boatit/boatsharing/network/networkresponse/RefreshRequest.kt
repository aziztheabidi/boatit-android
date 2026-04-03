package com.boatit.boatsharing.network.networkresponse

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class RefreshRequest(
    @SerializedName("Accesstoken")
    val accesstoken: String?,
    @SerializedName("Refreshtoken")
    val refreshtoken: String?,
)
