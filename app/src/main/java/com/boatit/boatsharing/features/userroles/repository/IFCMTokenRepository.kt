package com.boatit.boatsharing.features.userroles.repository

import com.boatit.boatsharing.features.userroles.domain.model.DeviceTokenUpdateDomainModel

interface IFCMTokenRepository {
    suspend fun updateDeviceToken(
        userId: String,
        deviceToken: String,
    ): Result<DeviceTokenUpdateDomainModel>
}
