package com.boatit.boatsharing.ui.captain.dashboard.repository

import com.boatit.boatsharing.data.remote.api.VoyageApi
import com.boatit.boatsharing.data.remote.RemoteMapper
import com.boatit.boatsharing.ui.captain.dashboard.model.AcceptVoyageRequest
import com.boatit.boatsharing.ui.captain.dashboard.model.AcceptVoyageResponse

class AcceptRequestRepository(private val api: VoyageApi) {
    suspend fun status(profile: AcceptVoyageRequest): Result<AcceptVoyageResponse> {
        return try {
            RemoteMapper.toResult(api.acceptVoyage(profile))
        } catch (e: Exception) {
            Result.failure(Exception("Network Error: ${e.localizedMessage}", e))
        }
    }
}
