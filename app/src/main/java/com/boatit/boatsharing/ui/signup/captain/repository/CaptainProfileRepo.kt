package com.boatit.boatsharing.ui.signup.captain.repository


import com.boatit.boatsharing.network.di.ApiConstants
import com.boatit.boatsharing.ui.signup.captain.model.CaptainProfileRequest
import com.boatit.boatsharing.ui.signup.captain.model.CaptainProfileResponse
import com.boatit.boatsharing.ui.signup.captain.model.GetCaptainProfileResponse
import com.boatit.boatsharing.ui.signup.general.model.RegistrationRequest
import com.boatit.boatsharing.ui.signup.general.model.RegistrationResponse
import com.boatit.boatsharing.ui.signup.general.model.VoyagerProfileRequest
import com.boatit.boatsharing.ui.signup.general.model.VoyagerProfileResponse
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
            val response: HttpResponse = httpClient.post("${ApiConstants.BASE_URL}${ApiConstants.Endpoints.SAVE_CAPTAIN_PROFILE}") {
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

