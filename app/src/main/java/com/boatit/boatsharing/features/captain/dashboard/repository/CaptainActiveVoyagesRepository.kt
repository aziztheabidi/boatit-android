package com.boatit.boatsharing.features.captain.dashboard.repository

import com.boatit.boatsharing.data.network.di.ApiConstants
import com.boatit.boatsharing.data.network.di.networkFailure
import com.boatit.boatsharing.data.network.di.toResult
import com.boatit.boatsharing.features.captain.dashboard.model.CaptainActiveVoyagesResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode

class CaptainActiveVoyagesRepository(
    private val httpClient: HttpClient,
    private val baseUrl: String = ApiConstants.BASE_URL,
) : ICaptainActiveVoyagesRepository {
    override suspend fun voyages(): Result<CaptainActiveVoyagesResponse> {
        return try {
            val response: HttpResponse = httpClient.get("$baseUrl${ApiConstants.Endpoints.GET_CAPTAIN_ACTIVE_VOYAGES}")
            if (response.status == HttpStatusCode.OK) {
                response.toResult<CaptainActiveVoyagesResponse>(successStatus = HttpStatusCode.OK)
            } else {
                Result.failure(Exception("Failed to fetch active voyages: HTTP ${response.status.value}"))
            }
        } catch (e: Exception) {
            networkFailure("Error fetching active voyages", e)
        }
    }
}
