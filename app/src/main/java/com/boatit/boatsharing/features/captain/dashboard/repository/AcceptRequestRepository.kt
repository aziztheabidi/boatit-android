package com.boatit.boatsharing.features.captain.dashboard.repository

import com.boatit.boatsharing.data.network.di.ApiConstants
import com.boatit.boatsharing.data.network.di.executePostRequest
import com.boatit.boatsharing.features.captain.dashboard.model.AcceptVoyageRequest
import com.boatit.boatsharing.features.captain.dashboard.model.AcceptVoyageResponse
import io.ktor.client.HttpClient
import io.ktor.http.HttpStatusCode

class AcceptRequestRepository(private val httpClient: HttpClient) {
    suspend fun status(profile: AcceptVoyageRequest): Result<AcceptVoyageResponse> {
        return executePostRequest(
            httpClient = httpClient,
            url = "${ApiConstants.BASE_URL}${ApiConstants.Endpoints.ACCEPT_REQUEST}",
            requestBody = profile,
            successStatus = HttpStatusCode.Created,
            onApiError = { body, _ -> Exception("API Error: ${body.Message}") },
            onException = { e -> Result.failure(Exception("Network Error: ${e.localizedMessage}", e)) },
        )
    }

    suspend fun decline(profile: AcceptVoyageRequest): Result<AcceptVoyageResponse> {
        return executePostRequest(
            httpClient = httpClient,
            url = "${ApiConstants.BASE_URL}${ApiConstants.Endpoints.DECLINE_REQUEST}",
            requestBody = profile,
            successStatus = HttpStatusCode.Created,
            onApiError = { body, _ -> Exception("API Error: ${body.Message}") },
            onException = { e -> Result.failure(Exception("Network Error: ${e.localizedMessage}", e)) },
        )
    }
}
