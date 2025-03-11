package com.boatit.boatsharing.ui.voyager.dashbaord.repository

import android.content.Context
import com.boatit.boatsharing.network.di.ApiConstants
import com.boatit.boatsharing.ui.voyager.dashbaord.model.FindBoatRequest
import com.boatit.boatsharing.ui.voyager.dashbaord.model.FindBoatResponse
import com.boatit.boatsharing.ui.voyager.dashbaord.model.VoyagePaymentRequest
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class FindBoatRepo(
    private val httpClient: HttpClient
) {
    suspend fun findboat(profile: FindBoatRequest): Result<FindBoatResponse> {
        return try {
            val response: HttpResponse = httpClient.post("${ApiConstants.BASE_URL}${ApiConstants.Endpoints.FIND_BOAT}") {
                contentType(ContentType.Application.Json)
                setBody(profile)
            }
            if (response.status == HttpStatusCode.Created) {
                val placesResponse: FindBoatResponse = response.body()
                Result.success(placesResponse)
            } else {
                val placesResponse: FindBoatResponse = response.body()
                Result.failure(Exception(placesResponse.Message))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error fetching places: ${e.localizedMessage}", e))
        }
    }
}
