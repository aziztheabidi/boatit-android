package com.boatit.boatsharing.ui.signup.business.repository


import com.boatit.boatsharing.network.di.ApiConstants
import com.boatit.boatsharing.ui.signup.business.model.SaveBusinessAboutRequest
import com.boatit.boatsharing.ui.signup.business.model.SaveBusinessAboutResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class BusinessAboutRepository(private val httpClient: HttpClient) {
    suspend fun BusinessAbout(profile: SaveBusinessAboutRequest): Result<SaveBusinessAboutResponse> {
        return try {
            val response: HttpResponse = httpClient.post("${ApiConstants.BASE_URL}${ApiConstants.Endpoints.SAVE_BUSINESS_ABOUT}") {
                contentType(ContentType.Application.Json)
                setBody(profile)
            }
            if (response.status == HttpStatusCode.OK) {
                val result: SaveBusinessAboutResponse = response.body()
                Result.success(result)
            } else {
                Result.failure(Exception("API Error: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network Error: ${e.localizedMessage}", e))
        }
    }
}

