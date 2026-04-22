package com.boatit.boatsharing.features.captain.dashboard.repository

import com.boatit.boatsharing.data.network.di.ApiConstants
import com.boatit.boatsharing.features.captain.dashboard.model.CaptainFeedbackRequest
import com.boatit.boatsharing.features.captain.dashboard.model.CaptainFeedbackResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class CaptainFeedbackRepository(private val httpClient: HttpClient) {
    suspend fun status(profile: CaptainFeedbackRequest): Result<CaptainFeedbackResponse> {
        return try {
            val response: HttpResponse =
                httpClient.post("${ApiConstants.BASE_URL}${ApiConstants.Endpoints.CAPTAIN_FEEDBACK}") {
                    contentType(ContentType.Application.Json)
                    setBody(profile)
                }
            if (response.status == HttpStatusCode.Created) {
                val result: CaptainFeedbackResponse = response.body()
                Result.success(result)
            } else {
                Result.failure(Exception("API Error: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network Error: ${e.localizedMessage}", e))
        }
    }
}
