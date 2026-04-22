package com.boatit.boatsharing.features.signup.general.repository

import com.boatit.boatsharing.data.network.di.ApiConstants
import com.boatit.boatsharing.data.network.di.networkFailure
import com.boatit.boatsharing.data.network.di.toResult
import com.boatit.boatsharing.features.signup.general.model.VoyagerProfileRequest
import com.boatit.boatsharing.features.signup.general.model.VoyagerProfileResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class VoyagerProfileRepository(private val httpClient: HttpClient) : IVoyagerProfileRepository {
    override suspend fun saveVoyagerProfile(profile: VoyagerProfileRequest): Result<VoyagerProfileResponse> {
        return try {
            val response: HttpResponse =
                httpClient.post("${ApiConstants.BASE_URL}${ApiConstants.Endpoints.SAVE_VOYAGER_PROFILE}") {
                    contentType(ContentType.Application.Json)
                    setBody(profile)
                }

            if (response.status == HttpStatusCode.OK) {
                response.toResult<VoyagerProfileResponse>(successStatus = HttpStatusCode.OK)
            } else {
                val result: VoyagerProfileResponse = response.body()
                Result.failure(Exception(result.Message ?: "Failed to save profile"))
            }
        } catch (e: Exception) {
            networkFailure("Error saving voyager profile", e)
        }
    }
}
