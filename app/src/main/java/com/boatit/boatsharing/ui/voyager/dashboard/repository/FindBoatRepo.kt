package com.boatit.boatsharing.ui.voyager.dashboard.repository

import com.boatit.boatsharing.data.remote.api.VoyageApi
import com.boatit.boatsharing.data.remote.RemoteMapper
import com.boatit.boatsharing.ui.voyager.dashboard.model.FindBoatRequest
import com.boatit.boatsharing.ui.voyager.dashboard.model.FindBoatResponse

class FindBoatRepo(
    private val api: VoyageApi,
) {
    suspend fun findboat(profile: FindBoatRequest): Result<FindBoatResponse> {
        return try {
            RemoteMapper.toResult(api.findBoat(profile))
        } catch (e: Exception) {
            Result.failure(Exception("Error fetching places: ${e.localizedMessage}", e))
        }
    }
}
