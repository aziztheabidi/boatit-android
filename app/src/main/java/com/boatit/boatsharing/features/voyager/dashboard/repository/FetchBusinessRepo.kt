package com.boatit.boatsharing.features.voyager.dashboard.repository

import com.boatit.boatsharing.data.network.di.ApiConstants
import com.boatit.boatsharing.data.network.di.executeGetRequest
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
        return executeGetRequest(
            httpClient = httpClient,
            url = "${ApiConstants.BASE_URL}${ApiConstants.Endpoints.FETCH_BUSINESS}",
            handleResponse = { response ->
                if (response.status == HttpStatusCode.OK) {
                    response.toResult<BusinessRelationshipResponse>(successStatus = HttpStatusCode.OK)
                } else {
                    Result.failure(Exception("Failed to fetch places: HTTP ${response.status}"))
                }
            },
            onException = { e -> networkFailure("Error fetching places", e) },
        )
    }
}
