package com.boatit.boatsharing.ui.signup.general.viewmodel

import com.boatit.boatsharing.data.remote.api.UserProfileApi
import com.boatit.boatsharing.data.remote.RemoteMapper
import com.boatit.boatsharing.ui.signup.general.model.GetVoyagerProfileResponse
import com.boatit.boatsharing.utils.AppConstants

class GetVoyagerProfileRepository(private val api: UserProfileApi) {
    suspend fun getVoyagerProfile(): Result<GetVoyagerProfileResponse> {
        return try {
            val userId = AppConstants.USER_ID ?: return Result.failure(Exception("User id missing"))
            RemoteMapper.toResult(api.getVoyagerProfile(userId))
        } catch (e: Exception) {
            Result.failure(Exception("Network Error: ${e.localizedMessage}", e))
        }
    }
}
