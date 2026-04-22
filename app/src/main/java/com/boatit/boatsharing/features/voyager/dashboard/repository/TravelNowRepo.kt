package com.boatit.boatsharing.features.voyager.dashboard.repository

import com.boatit.boatsharing.data.network.di.ApiConstants
import com.boatit.boatsharing.data.network.di.networkFailure
import com.boatit.boatsharing.data.network.di.toResult
import com.boatit.boatsharing.features.voyager.dashboard.model.TravelNowResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode

class TravelNowRepo(private val httpClient: HttpClient) {
    suspend fun voyages(): Result<TravelNowResponse> {
        return try {
            val response: HttpResponse = httpClient.get("${ApiConstants.BASE_URL}${ApiConstants.Endpoints.GET_NOW_BOOKED_VOYAGES}")
            response.toResult<TravelNowResponse>(successStatus = HttpStatusCode.OK)
        } catch (e: Exception) {
            networkFailure("Network Error", e)
        }
    }
}
