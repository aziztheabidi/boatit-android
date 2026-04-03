package com.boatit.boatsharing.network.networkresponse

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RefreshRequest(
    @SerializedName("Accesstoken")
    @SerialName("Accesstoken")
    val accesstoken: String?,
    @SerializedName("Refreshtoken")
    @SerialName("Refreshtoken")
    val refreshtoken: String?,
)
