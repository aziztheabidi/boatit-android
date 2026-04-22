@file:Suppress("ktlint:standard:filename")

package com.boatit.boatsharing.features.voyager.dashboard.repository

import com.boatit.boatsharing.data.network.di.ApiConstants
import com.boatit.boatsharing.data.network.di.networkFailure
import com.boatit.boatsharing.data.network.di.toResult
import com.boatit.boatsharing.features.voyager.dashboard.model.ConfirmBookedVoyageResponse
import com.boatit.boatsharing.features.voyager.dashboard.model.ConfirmBookedVoyages
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class ConfirmBookedVoyageRepository(
    private val httpClient: HttpClient,
) {
    suspend fun findboat(profile: ConfirmBookedVoyages): Result<ConfirmBookedVoyageResponse> {
        return try {
            val response: HttpResponse =
                httpClient.post("${ApiConstants.BASE_URL}${ApiConstants.Endpoints.CONFIRM_BOOKED_VOYAGE}") {
                    contentType(ContentType.Application.Json)
                    setBody(profile)
                }

            if (response.status == HttpStatusCode.Created) {
                response.toResult<ConfirmBookedVoyageResponse>(successStatus = HttpStatusCode.Created)
            } else {
                val placesResponse: ConfirmBookedVoyageResponse = response.body()
                Result.failure(Exception(placesResponse.Message))
            }
        } catch (e: Exception) {
            networkFailure("Error fetching places", e)
        }
    }
}
