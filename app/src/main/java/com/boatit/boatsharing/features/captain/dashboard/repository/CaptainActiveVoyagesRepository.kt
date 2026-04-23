package com.boatit.boatsharing.features.captain.dashboard.repository

import com.boatit.boatsharing.data.network.di.ApiConstants
import com.boatit.boatsharing.data.network.di.executeGetRequest
import com.boatit.boatsharing.data.network.di.networkFailure
import com.boatit.boatsharing.data.network.di.toResult
import com.boatit.boatsharing.features.captain.dashboard.model.CaptainActiveVoyagesResponse
import io.ktor.client.HttpClient
import io.ktor.http.HttpStatusCode

class CaptainActiveVoyagesRepository(
    private val httpClient: HttpClient,
    private val baseUrl: String = ApiConstants.BASE_URL,
) : ICaptainActiveVoyagesRepository {
    override suspend fun voyages(): Result<CaptainActiveVoyagesResponse> {
        return executeGetRequest(
            httpClient = httpClient,
            url = "$baseUrl${ApiConstants.Endpoints.GET_CAPTAIN_ACTIVE_VOYAGES}",
            handleResponse = { response ->
                if (response.status == HttpStatusCode.OK) {
                    response.toResult<CaptainActiveVoyagesResponse>(successStatus = HttpStatusCode.OK)
                } else {
                    Result.failure(Exception("Failed to fetch active voyages: HTTP ${response.status.value}"))
                }
            },
            onException = { e -> networkFailure("Error fetching active voyages", e) },
        )
    }
}
