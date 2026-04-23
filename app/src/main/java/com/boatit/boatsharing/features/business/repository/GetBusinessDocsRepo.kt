package com.boatit.boatsharing.features.business.repository

import com.boatit.boatsharing.data.network.di.ApiConstants
import com.boatit.boatsharing.data.network.di.executeGetRequest
import com.boatit.boatsharing.features.business.model.DocksDropdownResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.http.HttpStatusCode

class GetBusinessDocksRepo(private val httpClient: HttpClient) {
    suspend fun voyages(): Result<DocksDropdownResponse> {
        return executeGetRequest(
            httpClient = httpClient,
            url = "${ApiConstants.BASE_URL}${ApiConstants.Endpoints.Zones}",
            handleResponse = { response ->
                if (response.status == HttpStatusCode.OK) {
                    val result: DocksDropdownResponse = response.body()

                    Result.success(result)
                } else {
                    val result: DocksDropdownResponse = response.body()
                    Result.failure(Exception("API Error: ${result.Message}"))
                }
            },
            onException = { e ->
                Result.failure(Exception("Network Error: ${e.localizedMessage}", e))
            },
        )
    }
}
