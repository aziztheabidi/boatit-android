package com.boatit.boatsharing.ui.signup.business.repository

import com.boatit.boatsharing.data.remote.api.BusinessProfileApi
import com.boatit.boatsharing.data.remote.RemoteMapper
import com.boatit.boatsharing.ui.signup.business.model.BusinessInfoRequest
import com.boatit.boatsharing.ui.signup.business.model.SaveBusinessInfoResponse

class BusinessInfoRepository(private val api: BusinessProfileApi) {
    suspend fun BusinessInfo(profile: BusinessInfoRequest): Result<SaveBusinessInfoResponse> {
        return try {
            RemoteMapper.toResult(api.saveBusinessInfo(profile))
        } catch (e: Exception) {
            Result.failure(Exception("Network Error: ${e.localizedMessage}", e))
        }
    }
}
