package com.boatit.boatsharing.features.captain.availabilitystatus.repository

import com.boatit.boatsharing.data.network.di.ApiConstants
import com.boatit.boatsharing.features.captain.availabilitystatus.model.CaptainAvailabilityRequest
import com.boatit.boatsharing.features.captain.availabilitystatus.model.CaptainAvailabilityResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class UpdateStatusRepository(private val httpClient: HttpClient) {
    suspend fun status(profile: CaptainAvailabilityRequest): Result<CaptainAvailabilityResponse> {
        return try {
            val response: HttpResponse =
                httpClient.post("${ApiConstants.BASE_URL}${ApiConstants.Endpoints.UPDATE_CAPTAIN_STATUS}") {
                    contentType(ContentType.Application.Json)
                    setBody(profile)
                }
            if (response.status == HttpStatusCode.OK) {
                val result: CaptainAvailabilityResponse = response.body()
                Result.success(result)
            } else {
                Result.failure(Exception("API Error: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network Error: ${e.localizedMessage}", e))
        }
    }
}
