@file:Suppress("ktlint:standard:filename")

package com.boatit.boatsharing.features.voyager.dashboard.repository

import com.boatit.boatsharing.data.network.di.ApiConstants
import com.boatit.boatsharing.data.network.di.executePostRequest
import com.boatit.boatsharing.features.voyager.dashboard.model.PaymentSheetConfigResponse
import com.boatit.boatsharing.features.voyager.dashboard.model.VoyagePaymentRequest
import io.ktor.client.HttpClient
import io.ktor.http.HttpStatusCode

class PaymentSheetConfigRepository(private val httpClient: HttpClient) {
    suspend fun sheetConfig(id: String): Result<PaymentSheetConfigResponse> {
        return executePostRequest(
            httpClient = httpClient,
            url = "${ApiConstants.BASE_URL}${ApiConstants.Endpoints.PAYMENT}",
            requestBody = VoyagePaymentRequest(id),
            successStatus = HttpStatusCode.OK,
            onApiError = { _, status -> Exception("API Error: $status") },
            onException = { e -> Result.failure(Exception("Network Error: ${e.localizedMessage}", e)) },
        )
    }
}
