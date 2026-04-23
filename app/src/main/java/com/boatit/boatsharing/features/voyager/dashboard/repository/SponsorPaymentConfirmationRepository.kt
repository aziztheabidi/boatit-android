package com.boatit.boatsharing.features.voyager.dashboard.repository

import com.boatit.boatsharing.data.network.di.ApiConstants
import com.boatit.boatsharing.data.network.di.executePostRequest
import com.boatit.boatsharing.data.network.di.networkFailure
import com.boatit.boatsharing.domain.core.ExceptionMapper
import com.boatit.boatsharing.features.voyager.dashboard.model.PaymentConfirmationRequest
import com.boatit.boatsharing.features.voyager.dashboard.model.VoyagePaymentResponse
import io.ktor.client.HttpClient
import io.ktor.http.HttpStatusCode

class SponsorPaymentConfirmationRepository(private val httpClient: HttpClient) {
    suspend fun payment(profile: PaymentConfirmationRequest): Result<VoyagePaymentResponse> {
        return executePostRequest(
            httpClient = httpClient,
            url = "${ApiConstants.BASE_URL}${ApiConstants.Endpoints.SPONSOR_PAYMENT_CONFIRMATION}",
            requestBody = profile,
            successStatus = HttpStatusCode.Created,
            onApiError = { _, status -> ExceptionMapper.mapHttpException(status.value, status.description) },
            onException = { e -> networkFailure("Network Error", e) },
        )
    }
}
