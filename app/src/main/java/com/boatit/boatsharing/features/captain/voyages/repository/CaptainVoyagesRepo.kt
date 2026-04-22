package com.boatit.boatsharing.features.captain.voyages.repository

import com.boatit.boatsharing.data.network.di.ApiConstants
import com.boatit.boatsharing.data.network.di.networkFailure
import com.boatit.boatsharing.data.network.di.toResult
import com.boatit.boatsharing.features.voyager.dashboard.model.CaptainCompletedVoyageResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode

class CaptainVoyagesRepository(
    private val httpClient: HttpClient,
    private val baseUrl: String = ApiConstants.BASE_URL,
) : ICaptainVoyagesRepository {
    override suspend fun voyages(): Result<CaptainCompletedVoyageResponse> {
        return try {
            val response: HttpResponse = httpClient.get("$baseUrl${ApiConstants.Endpoints.GET_CAPTAIN_VOYAGES}")
            if (response.status == HttpStatusCode.OK) {
                response.toResult<CaptainCompletedVoyageResponse>(successStatus = HttpStatusCode.OK)
            } else {
                Result.failure(Exception("Failed to fetch captain voyages: HTTP ${response.status.value}"))
            }
        } catch (e: Exception) {
            networkFailure("Error fetching captain voyages", e)
        }
    }
}
