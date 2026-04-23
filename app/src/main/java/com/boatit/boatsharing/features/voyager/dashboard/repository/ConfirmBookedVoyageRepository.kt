@file:Suppress("ktlint:standard:filename")

package com.boatit.boatsharing.features.voyager.dashboard.repository

import com.boatit.boatsharing.data.network.di.ApiConstants
import com.boatit.boatsharing.data.network.di.executePostRequest
import com.boatit.boatsharing.data.network.di.networkFailure
import com.boatit.boatsharing.features.voyager.dashboard.model.ConfirmBookedVoyageResponse
import com.boatit.boatsharing.features.voyager.dashboard.model.ConfirmBookedVoyages
import io.ktor.client.HttpClient
import io.ktor.http.HttpStatusCode

class ConfirmBookedVoyageRepository(
    private val httpClient: HttpClient,
) {
    suspend fun findboat(profile: ConfirmBookedVoyages): Result<ConfirmBookedVoyageResponse> {
        return executePostRequest(
            httpClient = httpClient,
            url = "${ApiConstants.BASE_URL}${ApiConstants.Endpoints.CONFIRM_BOOKED_VOYAGE}",
            requestBody = profile,
            successStatus = HttpStatusCode.Created,
            onApiError = { body, _ -> Exception(body.Message) },
            onException = { e -> networkFailure("Error fetching places", e) },
        )
    }
}
