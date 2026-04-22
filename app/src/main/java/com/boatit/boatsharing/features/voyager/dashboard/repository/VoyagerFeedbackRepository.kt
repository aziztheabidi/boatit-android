@file:Suppress(
    "ktlint:standard:filename",
)

package com.boatit.boatsharing.features.voyager.dashboard.repository

import com.boatit.boatsharing.data.network.di.ApiConstants
import com.boatit.boatsharing.data.network.di.networkFailure
import com.boatit.boatsharing.data.network.di.toResult
import com.boatit.boatsharing.features.voyager.dashboard.model.VoyagerFeedbackRequest
import com.boatit.boatsharing.features.voyager.dashboard.model.VoyagerFeedbackResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class VoyagerFeedbackRepository(private val httpClient: HttpClient) {
    suspend fun status(profile: VoyagerFeedbackRequest): Result<VoyagerFeedbackResponse> {
        return try {
            val response: HttpResponse =
                httpClient.post("${ApiConstants.BASE_URL}${ApiConstants.Endpoints.VOYAGER_FEEDBACK}") {
                    contentType(ContentType.Application.Json)
                    setBody(profile)
                }

            if (response.status == HttpStatusCode.Created) {
                response.toResult<VoyagerFeedbackResponse>(successStatus = HttpStatusCode.Created)
            } else {
                Result.failure(Exception("API Error: ${response.status}"))
            }
        } catch (e: Exception) {
            networkFailure("Network Error", e)
        }
    }
}
