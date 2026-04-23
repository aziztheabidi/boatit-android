package com.boatit.boatsharing.features.userroles.repository

import com.boatit.boatsharing.data.network.di.ApiConstants
import com.boatit.boatsharing.data.network.di.executePostRequest
import com.boatit.boatsharing.data.network.di.networkFailure
import com.boatit.boatsharing.domain.core.ExceptionMapper
import com.boatit.boatsharing.features.userroles.data.dto.UpdateDeviceTokenRequestDto
import com.boatit.boatsharing.features.userroles.data.dto.UpdateDeviceTokenResponseDto
import com.boatit.boatsharing.features.userroles.domain.model.DeviceTokenUpdateDomainModel
import com.boatit.boatsharing.features.userroles.domain.model.toDomainModel
import io.ktor.client.HttpClient
import io.ktor.http.HttpStatusCode

class FCMTokenRepository(
    private val httpClient: HttpClient,
) : IFCMTokenRepository {
    override suspend fun updateDeviceToken(
        userId: String,
        deviceToken: String,
    ): Result<DeviceTokenUpdateDomainModel> {
        return executePostRequest<UpdateDeviceTokenRequestDto, UpdateDeviceTokenResponseDto>(
            httpClient = httpClient,
            url = "${ApiConstants.BASE_URL}${ApiConstants.Endpoints.FCMToken}",
            requestBody = UpdateDeviceTokenRequestDto(UserId = userId, DeviceToken = deviceToken),
            successStatus = HttpStatusCode.OK,
            onApiError = { _, status -> ExceptionMapper.mapHttpException(status.value, status.description) },
            onException = { e -> networkFailure("Error updating device token", e) },
        ).map { it.toDomainModel() }
    }
}
