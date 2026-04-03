package com.boatit.boatsharing.ui.userroles.repository

import com.boatit.boatsharing.data.remote.api.AccountApi
import com.boatit.boatsharing.data.remote.RemoteMapper
import com.boatit.boatsharing.ui.userroles.model.UpdateDeviceTokenRequest
import com.boatit.boatsharing.ui.userroles.model.UpdateDeviceTokenResponse

class FCMTokenRepository(private val api: AccountApi) {
    suspend fun login(userid: String, deviceToken: String): Result<UpdateDeviceTokenResponse> {
        return try {
            RemoteMapper.toResult(api.updateDeviceToken(UpdateDeviceTokenRequest(userid, deviceToken)))
        } catch (e: Exception) {
            Result.failure(Exception("Error registering: ${e.localizedMessage}", e))
        }
    }
}
