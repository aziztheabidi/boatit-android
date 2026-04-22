package com.boatit.boatsharing.features.voyager.dashboard.repository

import android.util.Log
import com.boatit.boatsharing.data.network.di.ApiConstants
import com.boatit.boatsharing.data.network.di.networkFailure
import com.boatit.boatsharing.data.network.di.toResult
import com.boatit.boatsharing.features.voyager.dashboard.model.BookVoyageRequest
import com.boatit.boatsharing.features.voyager.dashboard.model.BookVoyageResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class BookVoyageRepo(
    private val httpClient: HttpClient,
) {
    suspend fun bookVoyage(profile: BookVoyageRequest): Result<BookVoyageResponse> {
        return try {
            val response: HttpResponse =
                httpClient.post("${ApiConstants.BASE_URL}${ApiConstants.Endpoints.BOOK_VOYAGE}") {
                    contentType(ContentType.Application.Json)
                    setBody(profile)
                }

            if (response.status == HttpStatusCode.Created) {
                response.toResult<BookVoyageResponse>(successStatus = HttpStatusCode.Created)
            } else {
                print("viewModel" + "Error fetching places: ${response.bodyAsText()}")
                val placesResponse: BookVoyageResponse = response.body()
                Result.failure(Exception(placesResponse.Message))
            }
        } catch (e: Exception) {
            Log.e("viewModel", "Error fetching places: ${e.localizedMessage}", e)
            networkFailure("Error fetching places", e)
        }
    }
}
