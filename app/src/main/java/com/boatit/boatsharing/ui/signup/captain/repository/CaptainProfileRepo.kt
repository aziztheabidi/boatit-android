package com.boatit.boatsharing.ui.signup.captain.repository


import com.boatit.boatsharing.data.remote.api.UserProfileApi
import com.boatit.boatsharing.data.remote.RemoteMapper
import com.boatit.boatsharing.ui.signup.captain.model.CaptainProfileRequest
import com.boatit.boatsharing.ui.signup.captain.model.CaptainProfileResponse

class CaptainProfileRepository(private val api: UserProfileApi) {
    suspend fun CaptainProfile(profile: CaptainProfileRequest): Result<CaptainProfileResponse> {
        return try {
            RemoteMapper.toResult(api.saveCaptainProfile(profile))
        } catch (e: Exception) {
            Result.failure(Exception("Network Error: ${e.localizedMessage}", e))
        }
    }
}
