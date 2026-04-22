@file:Suppress("ktlint:standard:filename")

package com.boatit.boatsharing.features.voyager.dashboard.repository

import com.boatit.boatsharing.data.network.di.ApiConstants
import com.boatit.boatsharing.data.network.di.networkFailure
import com.boatit.boatsharing.data.network.di.toResult
import com.boatit.boatsharing.features.captain.dashboard.model.DeclineRequest
import com.boatit.boatsharing.features.voyager.dashboard.model.PaymentSheetConfigResponse
import com.boatit.boatsharing.features.voyager.dashboard.model.SponsorVoyagePaymentRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class SponsorPaymentSheetConfigRepository(private val httpClient: HttpClient) {
    suspend fun sheetConfig(id: SponsorVoyagePaymentRequest): Result<PaymentSheetConfigResponse> {
        return try {
            val response: HttpResponse =
                httpClient.post("${ApiConstants.BASE_URL}${ApiConstants.Endpoints.SPONSOR_PAYMENT_INITIATE}") {
                    contentType(ContentType.Application.Json)
                    setBody(id)
                }
            response.toResult<PaymentSheetConfigResponse>(successStatus = HttpStatusCode.OK)
        } catch (e: Exception) {
            networkFailure("Network Error", e)
        }
    }

    suspend fun paymentDecline(id: DeclineRequest): Result<PaymentSheetConfigResponse> {
        return try {
            val response: HttpResponse =
                httpClient.post("${ApiConstants.BASE_URL}${ApiConstants.Endpoints.SPONSOR_PAYMENT_Decline}") {
                    contentType(ContentType.Application.Json)
                    setBody(id)
                }
            response.toResult<PaymentSheetConfigResponse>(successStatus = HttpStatusCode.OK)
        } catch (e: Exception) {
            networkFailure("Network Error", e)
        }
    }
}
