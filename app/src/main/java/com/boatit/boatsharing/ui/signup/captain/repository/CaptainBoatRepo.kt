package com.boatit.boatsharing.ui.signup.captain.repository

import com.boatit.boatsharing.data.remote.api.UserProfileApi
import com.boatit.boatsharing.data.remote.RemoteMapper
import com.boatit.boatsharing.ui.signup.captain.model.SaveCaptainBoatRequest
import com.boatit.boatsharing.ui.signup.captain.model.SaveCaptainBoatResponse

class CaptainBoatRepository(private val api: UserProfileApi) {
    suspend fun CaptainBoat(profile: SaveCaptainBoatRequest): Result<SaveCaptainBoatResponse> {
        return try {
            RemoteMapper.toResult(api.saveCaptainBoat(profile))
        } catch (e: Exception) {
            Result.failure(Exception("Network Error: ${e.localizedMessage}", e))
        }
    }
}
