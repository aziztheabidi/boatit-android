package com.boatit.boatsharing.ui.chat.repository

import com.boatit.boatsharing.data.remote.RemoteMapper
import com.boatit.boatsharing.data.remote.api.VoyageApi
import com.boatit.boatsharing.ui.chat.model.ActiveVoyagersResponse
import com.boatit.boatsharing.utils.AppConstants

class VoyagersRepository(private val api: VoyageApi) {
    suspend fun voyages(): Result<ActiveVoyagersResponse> {
        return try {
            val userId = AppConstants.USER_ID ?: return Result.failure(Exception("User id missing"))
            RemoteMapper.toResult(api.getActiveVoyagers(userId))
        } catch (e: Exception) {
            Result.failure(Exception("Network Error: ${e.localizedMessage}", e))
        }
    }
}
