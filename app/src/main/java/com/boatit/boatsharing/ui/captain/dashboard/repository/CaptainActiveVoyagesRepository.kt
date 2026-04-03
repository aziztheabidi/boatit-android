package com.boatit.boatsharing.ui.captain.dashboard.repository

import com.boatit.boatsharing.data.remote.RemoteMapper
import com.boatit.boatsharing.data.remote.api.VoyageApi
import com.boatit.boatsharing.ui.captain.dashboard.model.CaptainActiveVoyagesResponse
import com.boatit.boatsharing.utils.AppConstants

class CaptainActiveVoyagesRepository(private val api: VoyageApi) {
    suspend fun voyages(): Result<CaptainActiveVoyagesResponse> {
        return try {
            val userId = AppConstants.USER_ID ?: return Result.failure(Exception("User id missing"))
            RemoteMapper.toResult(api.getCaptainActiveVoyages(userId))
        } catch (e: Exception) {
            Result.failure(Exception("Network Error: ${e.localizedMessage}", e))
        }
    }
}
