package com.boatit.boatsharing.ui.voyager.dashboard.repository

import com.boatit.boatsharing.data.remote.RemoteMapper
import com.boatit.boatsharing.data.remote.api.VoyageApi
import com.boatit.boatsharing.ui.voyager.dashboard.model.CalculateFair

class CalculateFairRepository(private val api: VoyageApi) {
    suspend fun CalculateFairRepoFunc(
        fromDockId: String,
        toDockId: String,
        durationInHours: String,
    ): Result<CalculateFair> {
        return try {
            RemoteMapper.toResult(api.calculateFair(fromDockId, toDockId, durationInHours))
        } catch (e: Exception) {
            Result.failure(Exception("Network Error: ${e.localizedMessage}", e))
        }
    }
}
