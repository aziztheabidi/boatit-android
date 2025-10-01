package com.boatit.boatsharing.ui.voyager.dashboard.repository

import android.content.Context
import com.boatit.boatsharing.network.di.ApiConstants
import com.boatit.boatsharing.ui.voyager.dashboard.model.CancelBookedVoyageResponse
import com.boatit.boatsharing.ui.voyager.dashboard.model.CancelBookedVoyages
import com.boatit.boatsharing.ui.voyager.dashboard.model.ConfirmBookedVoyageResponse
import com.boatit.boatsharing.ui.voyager.dashboard.model.ConfirmBookedVoyages
import com.boatit.boatsharing.ui.voyager.dashboard.model.FindBoatRequest
import com.boatit.boatsharing.ui.voyager.dashboard.model.FindBoatResponse
import com.boatit.boatsharing.ui.voyager.dashboard.model.VoyagePaymentRequest
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class CancelBookedVoyageRepository(
    private val httpClient: HttpClient
) {
    suspend fun findboat(profile: CancelBookedVoyages): Result<CancelBookedVoyageResponse> {
        return try {
            val response: HttpResponse = httpClient.post("${ApiConstants.BASE_URL}${ApiConstants.Endpoints.CANCEL_VOYAGE}") {
                contentType(ContentType.Application.Json)
                setBody(profile)
            }
            if (response.status == HttpStatusCode.Created) {
                val placesResponse: CancelBookedVoyageResponse = response.body()
                Result.success(placesResponse)
            } else {
                val placesResponse: CancelBookedVoyageResponse = response.body()
                Result.failure(Exception(placesResponse.Message))
            }
        } catch (e: Exception) {
            Result.failure(Exception("${e.localizedMessage}", e))
        }
    }
}
