package com.boatit.boatsharing.features.captain.dashboard.model

import kotlinx.serialization.Serializable

@Serializable
data class VoyageCompleteRequest(
    val Id: String,
)

@Serializable
data class VoyageCompleteResponse(
    val Status: Int,
    val Message: String,
)
