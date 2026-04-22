package com.boatit.boatsharing.features.voyager.dashboard.repository

import com.boatit.boatsharing.data.network.di.ApiConstants
import com.boatit.boatsharing.data.network.di.networkFailure
import com.boatit.boatsharing.data.network.di.toResult
import com.boatit.boatsharing.features.voyager.dashboard.model.BusinessRelationshipResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode

class FetchBusinessRepo(
    private val httpClient: HttpClient,
) {
    suspend fun getNearbyPlaces(): Result<BusinessRelationshipResponse> {
        return try {
            val response: HttpResponse = httpClient.get("${ApiConstants.BASE_URL}${ApiConstants.Endpoints.FETCH_BUSINESS}")
            if (response.status == HttpStatusCode.OK) {
                response.toResult<BusinessRelationshipResponse>(successStatus = HttpStatusCode.OK)
            } else {
                Result.failure(Exception("Failed to fetch places: HTTP ${response.status}"))
            }
        } catch (e: Exception) {
            networkFailure("Error fetching places", e)
        }
    }
}
