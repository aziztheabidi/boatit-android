@file:Suppress("ktlint:standard:filename")

package com.boatit.boatsharing.features.voyager.dashboard.repository

import com.boatit.boatsharing.data.network.di.ApiConstants
import com.boatit.boatsharing.data.network.di.executePostRequest
import com.boatit.boatsharing.data.network.di.networkFailure
import com.boatit.boatsharing.features.voyager.dashboard.model.CancelBookedVoyageResponse
import com.boatit.boatsharing.features.voyager.dashboard.model.CancelBookedVoyages
import io.ktor.client.HttpClient
import io.ktor.http.HttpStatusCode

class CancelBookedVoyageRepository(
    private val httpClient: HttpClient,
) {
    suspend fun findboat(profile: CancelBookedVoyages): Result<CancelBookedVoyageResponse> {
        return executePostRequest(
            httpClient = httpClient,
            url = "${ApiConstants.BASE_URL}${ApiConstants.Endpoints.CANCEL_VOYAGE}",
            requestBody = profile,
            successStatus = HttpStatusCode.Created,
            onApiError = { body, _ -> Exception(body.Message) },
            onException = { e -> networkFailure("Network Error", e) },
        )
    }
}
