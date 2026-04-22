@file:Suppress("ktlint:standard:filename")

package com.boatit.boatsharing.features.voyager.dashboard.repository

import com.boatit.boatsharing.data.network.di.ApiConstants
import com.boatit.boatsharing.features.voyager.dashboard.model.PaymentSheetConfigResponse
import com.boatit.boatsharing.features.voyager.dashboard.model.VoyagePaymentRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class PaymentSheetConfigRepository(private val httpClient: HttpClient) {
    suspend fun sheetConfig(id: String): Result<PaymentSheetConfigResponse> {
        return try {
            val response: HttpResponse =
                httpClient.post("${ApiConstants.BASE_URL}${ApiConstants.Endpoints.PAYMENT}") {
                    contentType(ContentType.Application.Json)
                    setBody(VoyagePaymentRequest(id))
                }
            if (response.status == HttpStatusCode.OK) {
                val result: PaymentSheetConfigResponse = response.body()
                Result.success(result)
            } else {
                Result.failure(Exception("API Error: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network Error: ${e.localizedMessage}", e))
        }
    }
}
