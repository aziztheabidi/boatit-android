package com.boatit.boatsharing.ui.voyager.dashboard.repository

import com.boatit.boatsharing.network.di.ApiConstants
import com.boatit.boatsharing.ui.voyager.dashboard.model.VoyagerFeedbackRequest
import com.boatit.boatsharing.ui.voyager.dashboard.model.VoyagerFeedbackResponse
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class VoyagerFeedbackRepository(private val httpClient: HttpClient) {
    suspend fun status(profile: VoyagerFeedbackRequest): Result<VoyagerFeedbackResponse> {
        return try {
            val response: HttpResponse = httpClient.post("${ApiConstants.BASE_URL}${ApiConstants.Endpoints.VOYAGER_FEEDBACK}") {
                contentType(ContentType.Application.Json)
                setBody(profile)
            }
            if (response.status == HttpStatusCode.Created) {
                val result: VoyagerFeedbackResponse = response.body()
                Result.success(result)
            } else {
                Result.failure(Exception("API Error: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network Error: ${e.localizedMessage}", e))
        }
    }
}
