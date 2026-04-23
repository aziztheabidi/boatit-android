package com.boatit.boatsharing.features.voyager.dashboard.repository

import com.boatit.boatsharing.data.network.di.ApiConstants
import com.boatit.boatsharing.data.network.di.executeGetRequest
import com.boatit.boatsharing.data.network.di.networkFailure
import com.boatit.boatsharing.data.network.di.toResult
import com.boatit.boatsharing.features.voyager.dashboard.model.TravelNowResponse
import io.ktor.client.HttpClient
import io.ktor.http.HttpStatusCode

class TravelNowRepo(private val httpClient: HttpClient) {
    suspend fun voyages(): Result<TravelNowResponse> {
        return executeGetRequest(
            httpClient = httpClient,
            url = "${ApiConstants.BASE_URL}${ApiConstants.Endpoints.GET_NOW_BOOKED_VOYAGES}",
            handleResponse = { response ->
                response.toResult<TravelNowResponse>(successStatus = HttpStatusCode.OK)
            },
            onException = { e -> networkFailure("Network Error", e) },
        )
    }
}
