package com.boatit.boatsharing.ui.signup.business.repository


import com.boatit.boatsharing.data.remote.api.BusinessProfileApi
import com.boatit.boatsharing.data.remote.RemoteMapper
import com.boatit.boatsharing.ui.signup.business.model.SaveBusinessAboutRequest
import com.boatit.boatsharing.ui.signup.business.model.SaveBusinessAboutResponse

class BusinessAboutRepository(private val api: BusinessProfileApi) {
    suspend fun BusinessAbout(profile: SaveBusinessAboutRequest): Result<SaveBusinessAboutResponse> {
        return try {
            RemoteMapper.toResult(api.saveBusinessAbout(profile))
        } catch (e: Exception) {
            Result.failure(Exception("Network Error: ${e.localizedMessage}", e))
        }
    }
}
