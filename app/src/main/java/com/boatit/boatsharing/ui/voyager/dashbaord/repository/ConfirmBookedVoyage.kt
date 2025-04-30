package com.boatit.boatsharing.ui.voyager.dashbaord.repository

import android.content.Context
import com.boatit.boatsharing.network.di.ApiConstants
import com.boatit.boatsharing.ui.voyager.dashbaord.model.ConfirmBookedVoyageResponse
import com.boatit.boatsharing.ui.voyager.dashbaord.model.ConfirmBookedVoyages
import com.boatit.boatsharing.ui.voyager.dashbaord.model.FindBoatRequest
import com.boatit.boatsharing.ui.voyager.dashbaord.model.FindBoatResponse
import com.boatit.boatsharing.ui.voyager.dashbaord.model.VoyagePaymentRequest
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class ConfirmBookedVoyageRepository(
    private val httpClient: HttpClient
) {
    suspend fun findboat(profile: ConfirmBookedVoyages): Result<ConfirmBookedVoyageResponse> {
        return try {
            val response: HttpResponse = httpClient.post("${ApiConstants.BASE_URL}${ApiConstants.Endpoints.CONFIRM_BOOKED_VOYAGE}") {
                contentType(ContentType.Application.Json)
                setBody(profile)
            }
            if (response.status == HttpStatusCode.Created) {
                val placesResponse: ConfirmBookedVoyageResponse = response.body()
                Result.success(placesResponse)
            } else {
                val placesResponse: ConfirmBookedVoyageResponse = response.body()
                Result.failure(Exception(placesResponse.Message))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error fetching places: ${e.localizedMessage}", e))
        }
    }
}
