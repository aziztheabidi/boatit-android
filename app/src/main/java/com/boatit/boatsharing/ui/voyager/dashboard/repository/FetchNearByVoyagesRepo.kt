package com.boatit.boatsharing.ui.voyager.dashboard.repository

import com.boatit.boatsharing.data.remote.api.DockApi
import com.boatit.boatsharing.data.remote.RemoteMapper
import com.boatit.boatsharing.ui.voyager.dashboard.model.NearbyPlacesResponse

class FetchNearByVoyagesRepo(
    private val api: DockApi,
) {
    suspend fun getNearbyPlaces(): Result<NearbyPlacesResponse> {
        return try {
            RemoteMapper.toResult(api.getActiveDocks())
        } catch (e: Exception) {
            Result.failure(Exception("Error fetching places: ${e.localizedMessage}", e))
        }
    }
}
