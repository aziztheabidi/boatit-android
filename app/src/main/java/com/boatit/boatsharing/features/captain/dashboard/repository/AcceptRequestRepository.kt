package com.boatit.boatsharing.features.captain.dashboard.repository

import com.boatit.boatsharing.data.network.di.ApiConstants
import com.boatit.boatsharing.features.captain.dashboard.model.AcceptVoyageRequest
import com.boatit.boatsharing.features.captain.dashboard.model.AcceptVoyageResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class AcceptRequestRepository(private val httpClient: HttpClient) {
    suspend fun status(profile: AcceptVoyageRequest): Result<AcceptVoyageResponse> {
        return try {
            val response: HttpResponse =
                httpClient.post("${ApiConstants.BASE_URL}${ApiConstants.Endpoints.ACCEPT_REQUEST}") {
                    contentType(ContentType.Application.Json)
                    setBody(profile)
                }
            if (response.status == HttpStatusCode.Created) {
                val result: AcceptVoyageResponse = response.body()
                Result.success(result)
            } else {
                val result: AcceptVoyageResponse = response.body()
                Result.failure(Exception("API Error: ${result.Message}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network Error: ${e.localizedMessage}", e))
        }
    }

    suspend fun decline(profile: AcceptVoyageRequest): Result<AcceptVoyageResponse> {
        return try {
            val response: HttpResponse =
                httpClient.post("${ApiConstants.BASE_URL}${ApiConstants.Endpoints.DECLINE_REQUEST}") {
                    contentType(ContentType.Application.Json)
                    setBody(profile)
                }
            if (response.status == HttpStatusCode.Created) {
                val result: AcceptVoyageResponse = response.body()
                Result.success(result)
            } else {
                val result: AcceptVoyageResponse = response.body()
                Result.failure(Exception("API Error: ${result.Message}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network Error: ${e.localizedMessage}", e))
        }
    }
}
