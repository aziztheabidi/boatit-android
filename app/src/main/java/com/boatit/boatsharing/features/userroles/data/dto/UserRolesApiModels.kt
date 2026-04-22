package com.boatit.boatsharing.features.userroles.data.dto

import kotlinx.serialization.Serializable

/** Wire / JSON DTOs for user-role and device-token endpoints (not domain models). */
@Serializable
data class RoleRequestDto(
    val UserId: String,
    val Role: String,
)

@Serializable
data class RoleResponseDto(
    val Status: Int? = null,
    val Message: String? = null,
    val obj: RoleTokenDataDto? = null,
)

@Serializable
data class RoleTokenDataDto(
    val Accesstoken: String? = null,
    val Refreshtoken: String? = null,
)

@Serializable
data class UpdateDeviceTokenRequestDto(
    val UserId: String,
    val DeviceToken: String,
)

@Serializable
data class UpdateDeviceTokenResponseDto(
    val Status: Int,
    val Message: String,
)
