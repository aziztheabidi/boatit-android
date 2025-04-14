package com.boatit.boatsharing.ui.userroles.model

import kotlinx.serialization.Serializable

@Serializable
data class UpdateDeviceTokenRequest(
    val UserId: String,
    val DeviceToken: String
)

@Serializable
data class UpdateDeviceTokenResponse(
    val Status: Int,
    val Message: String,
)