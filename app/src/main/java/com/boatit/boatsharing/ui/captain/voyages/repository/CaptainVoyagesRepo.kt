package com.boatit.boatsharing.ui.captain.voyages.repository

import com.boatit.boatsharing.data.remote.api.VoyageApi
import com.boatit.boatsharing.data.remote.RemoteMapper
import com.boatit.boatsharing.ui.captain.voyages.model.CaptainVoyagesResponse
import com.boatit.boatsharing.utils.AppConstants

class CaptainVoyagesRepository(private val api: VoyageApi) {
    suspend fun voyages(): Result<CaptainVoyagesResponse> {
        return try {
            val userId = AppConstants.USER_ID ?: return Result.failure(Exception("User id missing"))
            RemoteMapper.toResult(api.getCaptainVoyages(userId))
        } catch (e: Exception) {
            Result.failure(Exception("Network Error: ${e.localizedMessage}", e))
        }
    }
}
