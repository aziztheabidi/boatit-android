package com.boatit.boatsharing.network.networkreposne
import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    val Status: Int,
    val Message: String,
    val obj: TokenData
)

@Serializable
data class TokenData(
    val Accesstoken: String,
    val Refreshtoken: String
)


