package com.boatit.boatsharing.ui.captain.dashboard.repository

import com.boatit.boatsharing.data.remote.api.VoyageApi
import com.boatit.boatsharing.data.remote.RemoteMapper
import com.boatit.boatsharing.ui.captain.dashboard.model.VoyageStartRequest
import com.boatit.boatsharing.ui.captain.dashboard.model.VoyageStartResponse

class StartVoyageRepository(private val api: VoyageApi) {
    suspend fun status(profile: VoyageStartRequest): Result<VoyageStartResponse> {
        return try {
            RemoteMapper.toResult(api.startVoyage(profile))
        } catch (e: Exception) {
            Result.failure(Exception("Network Error: ${e.localizedMessage}", e))
        }
    }
}
