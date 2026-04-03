package com.boatit.boatsharing.ui.signup.business.repository


import com.boatit.boatsharing.data.remote.api.BusinessProfileApi
import com.boatit.boatsharing.data.remote.RemoteMapper
import com.boatit.boatsharing.ui.signup.business.model.BusinessInfoResponse
import com.boatit.boatsharing.utils.AppConstants

class GetBusinessInfoRepository(private val api: BusinessProfileApi) {
    suspend fun GetBusinessInfo(): Result<BusinessInfoResponse> {
        return try {
            val userId = AppConstants.USER_ID ?: return Result.failure(Exception("User id missing"))
            RemoteMapper.toResult(api.getBusinessInfo(userId))
        } catch (e: Exception) {
            Result.failure(Exception("Network Error: ${e.localizedMessage}", e))
        }
    }
}
