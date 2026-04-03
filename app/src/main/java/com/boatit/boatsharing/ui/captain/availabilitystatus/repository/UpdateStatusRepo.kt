package com.boatit.boatsharing.ui.captain.availabilitystatus.repository

import com.boatit.boatsharing.data.remote.api.UserProfileApi
import com.boatit.boatsharing.data.remote.RemoteMapper
import com.boatit.boatsharing.ui.captain.availabilitystatus.model.CaptainAvailabilityRequest
import com.boatit.boatsharing.ui.captain.availabilitystatus.model.CaptainAvailabilityResponse

class UpdateStatusRepository(private val api: UserProfileApi) {
    suspend fun status(profile: CaptainAvailabilityRequest): Result<CaptainAvailabilityResponse> {
        return try {
            RemoteMapper.toResult(api.updateCaptainAvailability(profile))
        } catch (e: Exception) {
            Result.failure(Exception("Network Error: ${e.localizedMessage}", e))
        }
    }
}
