package com.boatit.boatsharing.ui.signup.captain.repository


import com.boatit.boatsharing.data.remote.api.UserProfileApi
import com.boatit.boatsharing.data.remote.RemoteMapper
import com.boatit.boatsharing.ui.signup.captain.model.GetCaptainProfileResponse
import com.boatit.boatsharing.utils.AppConstants

class GetCaptainProfileRepository(private val api: UserProfileApi) {
    suspend fun GetCaptainProfile(): Result<GetCaptainProfileResponse> {
        return try {
            val userId = AppConstants.USER_ID ?: return Result.failure(Exception("User id missing"))
            RemoteMapper.toResult(api.getCaptainProfile(userId))
        } catch (e: Exception) {
            Result.failure(Exception("Network Error: ${e.localizedMessage}", e))
        }
    }
}
