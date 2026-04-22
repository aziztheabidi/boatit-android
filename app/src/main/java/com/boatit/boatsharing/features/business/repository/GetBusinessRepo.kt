package com.boatit.boatsharing.features.business.repository

import com.boatit.boatsharing.data.network.di.ApiConstants
import com.boatit.boatsharing.features.business.model.GetBusinessResponse
import com.boatit.boatsharing.features.voyager.dashboard.model.VoyagerVoyagesResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode

class GetBusinessRepo(private val httpClient: HttpClient) {
    suspend fun voyages(): Result<GetBusinessResponse> {
        return try {
            val response: HttpResponse = httpClient.get("${ApiConstants.BASE_URL}${ApiConstants.Endpoints.GET_BUSINESS}")
            if (response.status == HttpStatusCode.OK) {
                val result: GetBusinessResponse = response.body()
                Result.success(result)
            } else {
                val result: VoyagerVoyagesResponse = response.body()
                Result.failure(Exception("API Error: ${result.Message}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network Error: ${e.localizedMessage}", e))
        }
    }
}
