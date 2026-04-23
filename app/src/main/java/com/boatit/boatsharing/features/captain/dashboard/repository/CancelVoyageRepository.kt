package com.boatit.boatsharing.features.captain.dashboard.repository

import com.boatit.boatsharing.data.network.di.ApiConstants
import com.boatit.boatsharing.data.network.di.executePostRequest
import com.boatit.boatsharing.features.captain.dashboard.model.VoyageCompleteRequest
import com.boatit.boatsharing.features.captain.dashboard.model.VoyageCompleteResponse
import io.ktor.client.HttpClient
import io.ktor.http.HttpStatusCode

class CancelVoyageRepository(private val httpClient: HttpClient) {
    suspend fun status(profile: VoyageCompleteRequest): Result<VoyageCompleteResponse> {
        return executePostRequest(
            httpClient = httpClient,
            url = "${ApiConstants.BASE_URL}${ApiConstants.Endpoints.CANCEL_VOYAGE}",
            requestBody = profile,
            successStatus = HttpStatusCode.OK,
            onApiError = { _, status -> Exception("API Error: $status") },
            onException = { e -> Result.failure(Exception("Network Error: ${e.localizedMessage}", e)) },
        )
    }
}
