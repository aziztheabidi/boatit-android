package com.boatit.boatsharing.ui.signup.business.repository


import com.boatit.boatsharing.data.remote.api.BusinessProfileApi
import com.boatit.boatsharing.data.remote.RemoteMapper
import com.boatit.boatsharing.ui.signup.business.model.GetBusinessProfileResponse
import com.boatit.boatsharing.utils.AppConstants

class GetBusinessProfileRepository(private val api: BusinessProfileApi) {
    suspend fun GetBusinessProfile(): Result<GetBusinessProfileResponse> {
        return try {
            val userId = AppConstants.USER_ID ?: return Result.failure(Exception("User id missing"))
            RemoteMapper.toResult(api.getBusinessProfile(userId))
        } catch (e: Exception) {
            Result.failure(Exception("Network Error: ${e.localizedMessage}", e))
        }
    }
}
