package com.boatit.boatsharing.ui.signup.captain.repository


import com.boatit.boatsharing.data.remote.api.UserProfileApi
import com.boatit.boatsharing.data.remote.RemoteMapper
import com.boatit.boatsharing.ui.signup.captain.model.GetCaptainBoatResponse
import com.boatit.boatsharing.utils.AppConstants

class GetCaptainBoatRepository(private val api: UserProfileApi) {
    suspend fun GetCaptainBoat(): Result<GetCaptainBoatResponse> {
        return try {
            val userId = AppConstants.USER_ID ?: return Result.failure(Exception("User id missing"))
            RemoteMapper.toResult(api.getCaptainBoat(userId))
        } catch (e: Exception) {
            Result.failure(Exception("Network Error: ${e.localizedMessage}", e))
        }
    }
}
