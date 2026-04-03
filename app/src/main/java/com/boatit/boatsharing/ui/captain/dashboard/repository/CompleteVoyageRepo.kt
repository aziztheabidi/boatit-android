package com.boatit.boatsharing.ui.captain.dashboard.repository

import com.boatit.boatsharing.data.remote.api.VoyageApi
import com.boatit.boatsharing.data.remote.RemoteMapper
import com.boatit.boatsharing.ui.captain.dashboard.model.VoyageCompleteRequest
import com.boatit.boatsharing.ui.captain.dashboard.model.VoyageCompleteResponse

class CompleteVoyageRepository(private val api: VoyageApi) {
    suspend fun status(profile: VoyageCompleteRequest): Result<VoyageCompleteResponse> {
        return try {
            RemoteMapper.toResult(api.completeVoyage(profile))
        } catch (e: Exception) {
            Result.failure(Exception("Network Error: ${e.localizedMessage}", e))
        }
    }
}
