package com.boatit.boatsharing.features.captain.voyages.repository

import com.boatit.boatsharing.data.network.di.ApiConstants
import com.boatit.boatsharing.data.network.di.executeGetRequest
import com.boatit.boatsharing.data.network.di.networkFailure
import com.boatit.boatsharing.data.network.di.toResult
import com.boatit.boatsharing.features.voyager.dashboard.model.CaptainCompletedVoyageResponse
import io.ktor.client.HttpClient
import io.ktor.http.HttpStatusCode

class CaptainVoyagesRepository(
    private val httpClient: HttpClient,
    private val baseUrl: String = ApiConstants.BASE_URL,
) : ICaptainVoyagesRepository {
    override suspend fun voyages(): Result<CaptainCompletedVoyageResponse> {
        return executeGetRequest(
            httpClient = httpClient,
            url = "$baseUrl${ApiConstants.Endpoints.GET_CAPTAIN_VOYAGES}",
            handleResponse = { response ->
                if (response.status == HttpStatusCode.OK) {
                    response.toResult<CaptainCompletedVoyageResponse>(successStatus = HttpStatusCode.OK)
                } else {
                    Result.failure(Exception("Failed to fetch captain voyages: HTTP ${response.status.value}"))
                }
            },
            onException = { e -> networkFailure("Error fetching captain voyages", e) },
        )
    }
}
