package com.boatit.boatsharing.ui.voyager.dashboard.repository

import com.boatit.boatsharing.data.remote.RemoteMapper
import com.boatit.boatsharing.data.remote.api.VoyageApi
import com.boatit.boatsharing.ui.voyager.dashboard.model.BookVoyageRequest
import com.boatit.boatsharing.ui.voyager.dashboard.model.BookVoyageResponse

class BookVoyageRepo(private val api: VoyageApi) {
    suspend fun BookVoyageFunc(profile: BookVoyageRequest): Result<BookVoyageResponse> {
        return try {
            RemoteMapper.toResult(api.bookVoyage(profile))
        } catch (e: Exception) {
            Result.failure(Exception("Error fetching places: ${e.localizedMessage}", e))
        }
    }
}
