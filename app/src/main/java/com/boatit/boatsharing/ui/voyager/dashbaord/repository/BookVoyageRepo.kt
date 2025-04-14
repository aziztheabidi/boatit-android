package com.boatit.boatsharing.ui.voyager.dashbaord.repository

import com.boatit.boatsharing.network.di.ApiConstants
import com.boatit.boatsharing.ui.voyager.dashbaord.model.BookVoyageRequest
import com.boatit.boatsharing.ui.voyager.dashbaord.model.BookVoyageResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class BookVoyageRepo(
    private val httpClient: HttpClient
) {
    suspend fun BookVoyageFunc(profile: BookVoyageRequest): Result<BookVoyageResponse> {
        return try {
            val response: HttpResponse = httpClient.post("${ApiConstants.BASE_URL}${ApiConstants.Endpoints.BOOK_VOYAGE}") {
                contentType(ContentType.Application.Json)
                setBody(profile)
            }
            if (response.status == HttpStatusCode.Created) {
                val placesResponse: BookVoyageResponse = response.body()
                Result.success(placesResponse)
            } else {
                val placesResponse: BookVoyageResponse = response.body()
                Result.failure(Exception(placesResponse.Message))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error fetching places: ${e.localizedMessage}", e))
        }
    }
}
