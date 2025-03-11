package com.boatit.boatsharing.ui.signup.business.repository

import com.boatit.boatsharing.network.di.ApiConstants
import com.boatit.boatsharing.ui.signup.business.model.BusinessInfoRequest
import com.boatit.boatsharing.ui.signup.business.model.SaveBusinessInfoResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class BusinessInfoRepository(private val httpClient: HttpClient) {
    suspend fun BusinessInfo(profile: BusinessInfoRequest): Result<SaveBusinessInfoResponse> {
        return try {
            val response: HttpResponse = httpClient.post("${ApiConstants.BASE_URL}${ApiConstants.Endpoints.SAVE_BUSINESS_INFO_PROFILE}") {
                contentType(ContentType.Application.Json)
                setBody(profile)
            }
            if (response.status == HttpStatusCode.OK) {
                val result: SaveBusinessInfoResponse = response.body()
                Result.success(result)
            } else {
                Result.failure(Exception("API Error: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network Error: ${e.localizedMessage}", e))
        }
    }
}

