package com.boatit.boatsharing.features.voyager.dashboard.repository

import com.boatit.boatsharing.data.network.di.ApiConstants
import com.boatit.boatsharing.data.network.di.networkFailure
import com.boatit.boatsharing.data.network.di.toResult
import com.boatit.boatsharing.features.voyager.dashboard.model.PaymentConfirmationRequest
import com.boatit.boatsharing.features.voyager.dashboard.model.VoyagePaymentResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class SponsorPaymentConfirmationRepository(private val httpClient: HttpClient) {
    suspend fun payment(profile: PaymentConfirmationRequest): Result<VoyagePaymentResponse> {
        return try {
            val response: HttpResponse =
                httpClient.post("${ApiConstants.BASE_URL}${ApiConstants.Endpoints.SPONSOR_PAYMENT_CONFIRMATION}") {
                    contentType(ContentType.Application.Json)
                    setBody(profile)
                }
            response.toResult<VoyagePaymentResponse>(successStatus = HttpStatusCode.Created)
        } catch (e: Exception) {
            networkFailure("Network Error", e)
        }
    }
}
