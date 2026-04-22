package com.boatit.boatsharing.features.signup.captain.repository

import com.boatit.boatsharing.data.network.di.ApiConstants
import com.boatit.boatsharing.features.signup.captain.model.CaptainProfileRequest
import com.boatit.boatsharing.features.signup.captain.model.CaptainProfileResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class CaptainProfileRepository(private val httpClient: HttpClient) {
    suspend fun CaptainProfile(profile: CaptainProfileRequest): Result<CaptainProfileResponse> {
        return try {
            val response: HttpResponse =
                httpClient.post("${ApiConstants.BASE_URL}${ApiConstants.Endpoints.SAVE_CAPTAIN_PROFILE}") {
                    contentType(ContentType.Application.Json)
                    setBody(profile)
                }
            if (response.status == HttpStatusCode.OK) {
                val result: CaptainProfileResponse = response.body()
                Result.success(result)
            } else {
                Result.failure(Exception("API Error: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network Error: ${e.localizedMessage}", e))
        }
    }
}
