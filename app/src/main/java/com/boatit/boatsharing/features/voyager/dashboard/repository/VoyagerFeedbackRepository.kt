@file:Suppress(
    "ktlint:standard:filename",
)

package com.boatit.boatsharing.features.voyager.dashboard.repository

import com.boatit.boatsharing.data.network.di.ApiConstants
import com.boatit.boatsharing.data.network.di.executePostRequest
import com.boatit.boatsharing.data.network.di.networkFailure
import com.boatit.boatsharing.features.voyager.dashboard.model.VoyagerFeedbackRequest
import com.boatit.boatsharing.features.voyager.dashboard.model.VoyagerFeedbackResponse
import io.ktor.client.HttpClient
import io.ktor.http.HttpStatusCode

class VoyagerFeedbackRepository(private val httpClient: HttpClient) {
    suspend fun status(profile: VoyagerFeedbackRequest): Result<VoyagerFeedbackResponse> {
        return executePostRequest(
            httpClient = httpClient,
            url = "${ApiConstants.BASE_URL}${ApiConstants.Endpoints.VOYAGER_FEEDBACK}",
            requestBody = profile,
            successStatus = HttpStatusCode.Created,
            onApiError = { _, status -> Exception("API Error: $status") },
            onException = { e -> networkFailure("Network Error", e) },
        )
    }
}
