package com.boatit.boatsharing.ui.signup.general.viewmodel


import com.boatit.boatsharing.network.di.ApiConstants
import com.boatit.boatsharing.ui.signup.general.model.RegistrationRequest
import com.boatit.boatsharing.ui.signup.general.model.RegistrationResponse
import com.boatit.boatsharing.ui.signup.general.model.VoyagerProfileRequest
import com.boatit.boatsharing.ui.signup.general.model.VoyagerProfileResponse
import com.boatit.boatsharing.utils.AppConstants
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class VoyagerProfileRepository(private val httpClient: HttpClient) {
    suspend fun saveVoyagerProfile(profile: VoyagerProfileRequest): Result<VoyagerProfileResponse> {
        return try {
            val response: HttpResponse = httpClient.post("${ApiConstants.BASE_URL}${ApiConstants.Endpoints.SAVE_VOYAGER_PROFILE}") {
                contentType(ContentType.Application.Json)
                setBody(profile)
            }
            if (response.status == HttpStatusCode.OK) {
                val result: VoyagerProfileResponse = response.body()
                Result.success(result)
            } else {
                val result: VoyagerProfileResponse = response.body()
                Result.failure(Exception("API Error: ${result.Message}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network Error: ${e.localizedMessage}", e))
        }
    }
}

