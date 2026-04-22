package com.boatit.boatsharing.features.userroles.repository

import com.boatit.boatsharing.data.network.di.ApiConstants
import com.boatit.boatsharing.data.network.retrofit.ApiExecutor
import com.boatit.boatsharing.features.userroles.data.dto.UpdateDeviceTokenRequestDto
import com.boatit.boatsharing.features.userroles.data.dto.UpdateDeviceTokenResponseDto
import com.boatit.boatsharing.features.userroles.domain.model.DeviceTokenUpdateDomainModel
import com.boatit.boatsharing.features.userroles.domain.model.toDomainModel

class FCMTokenRepository(
    private val apiExecutor: ApiExecutor,
) : IFCMTokenRepository {
    override suspend fun updateDeviceToken(
        userId: String,
        deviceToken: String,
    ): Result<DeviceTokenUpdateDomainModel> =
        apiExecutor.post<UpdateDeviceTokenResponseDto>(
            endpoint = ApiConstants.Endpoints.FCMToken,
            body = UpdateDeviceTokenRequestDto(UserId = userId, DeviceToken = deviceToken),
            successCode = 200,
        ).map { it.toDomainModel() }
}
