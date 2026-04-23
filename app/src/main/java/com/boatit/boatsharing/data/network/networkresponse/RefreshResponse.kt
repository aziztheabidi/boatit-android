package com.boatit.boatsharing.data.network.networkresponse
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class TokenResponse(
    val Status: Int,
    val Message: String,
    val obj: TokenData,
)

@Serializable
data class TokenData(
    @SerialName("Accesstoken")
    val accessToken: String,
    @SerialName("Refreshtoken")
    val refreshToken: String,
)
