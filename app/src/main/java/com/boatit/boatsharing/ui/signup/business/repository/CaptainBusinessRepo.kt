package com.boatit.boatsharing.ui.signup.business.repository


import com.boatit.boatsharing.data.remote.api.BusinessProfileApi
import com.boatit.boatsharing.data.remote.RemoteMapper
import com.boatit.boatsharing.ui.signup.business.model.BusinessProfileRequest
import com.boatit.boatsharing.ui.signup.business.model.SaveBusinessProfileResponse

class BusinessProfileRepository(private val api: BusinessProfileApi) {
    suspend fun BusinessProfile(profile: BusinessProfileRequest): Result<SaveBusinessProfileResponse> {
        return try {
            RemoteMapper.toResult(api.saveBusinessProfile(profile))
        } catch (e: Exception) {
            Result.failure(Exception("Network Error: ${e.localizedMessage}", e))
        }
    }
}
