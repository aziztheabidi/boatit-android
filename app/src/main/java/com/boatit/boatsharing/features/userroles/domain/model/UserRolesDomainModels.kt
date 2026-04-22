package com.boatit.boatsharing.features.userroles.domain.model

import com.boatit.boatsharing.features.userroles.data.dto.RoleResponseDto
import com.boatit.boatsharing.features.userroles.data.dto.UpdateDeviceTokenResponseDto

data class RoleAssignmentDomainModel(
    val status: Int?,
    val message: String?,
    val accessToken: String?,
    val refreshToken: String?,
)

data class DeviceTokenUpdateDomainModel(
    val status: Int,
    val message: String,
)

fun RoleResponseDto.toDomainModel(): RoleAssignmentDomainModel =
    RoleAssignmentDomainModel(
        status = Status,
        message = Message,
        accessToken = obj?.Accesstoken,
        refreshToken = obj?.Refreshtoken,
    )

fun UpdateDeviceTokenResponseDto.toDomainModel(): DeviceTokenUpdateDomainModel =
    DeviceTokenUpdateDomainModel(
        status = Status,
        message = Message,
    )
