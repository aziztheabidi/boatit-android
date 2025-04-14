package com.boatit.boatsharing.network.networkreposne

import kotlinx.serialization.Serializable

@Serializable
data class RefreshRequest(
    val accesstoken : String?,
    val refreshtoken: String?
)
