@file:Suppress("ktlint:standard:filename")

package com.boatit.boatsharing.features.voyager.dashboard.repository

import com.boatit.boatsharing.data.network.di.ApiConstants
import com.boatit.boatsharing.data.network.di.executePostRequest
import com.boatit.boatsharing.data.network.di.networkFailure
import com.boatit.boatsharing.domain.core.ExceptionMapper
import com.boatit.boatsharing.features.captain.dashboard.model.DeclineRequest
import com.boatit.boatsharing.features.voyager.dashboard.model.PaymentSheetConfigResponse
import com.boatit.boatsharing.features.voyager.dashboard.model.SponsorVoyagePaymentRequest
import io.ktor.client.HttpClient
import io.ktor.http.HttpStatusCode

class SponsorPaymentSheetConfigRepository(private val httpClient: HttpClient) {
    suspend fun sheetConfig(id: SponsorVoyagePaymentRequest): Result<PaymentSheetConfigResponse> {
        return executePostRequest(
            httpClient = httpClient,
            url = "${ApiConstants.BASE_URL}${ApiConstants.Endpoints.SPONSOR_PAYMENT_INITIATE}",
            requestBody = id,
            successStatus = HttpStatusCode.OK,
            onApiError = { _, status -> ExceptionMapper.mapHttpException(status.value, status.description) },
            onException = { e -> networkFailure("Network Error", e) },
        )
    }

    suspend fun paymentDecline(id: DeclineRequest): Result<PaymentSheetConfigResponse> {
        return executePostRequest(
            httpClient = httpClient,
            url = "${ApiConstants.BASE_URL}${ApiConstants.Endpoints.SPONSOR_PAYMENT_DECLINE}",
            requestBody = id,
            successStatus = HttpStatusCode.OK,
            onApiError = { _, status -> ExceptionMapper.mapHttpException(status.value, status.description) },
            onException = { e -> networkFailure("Network Error", e) },
        )
    }
}
