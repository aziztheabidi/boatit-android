package com.boatit.boatsharing.ui.voyager.dashbaord.repository

import com.boatit.boatsharing.network.di.ApiConstants
import com.boatit.boatsharing.ui.voyager.dashbaord.model.VoyagerFeedbackRequest
import com.boatit.boatsharing.ui.voyager.dashbaord.model.VoyagerFeedbackResponse
import com.boatit.boatsharing.ui.voyager.dashbaord.model.VoyagerFollowBusinessRequest
import com.boatit.boatsharing.ui.voyager.dashbaord.model.VoyagerFollowBusinessResponse
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class FollowBusinessRepository(private val httpClient: HttpClient) {
    suspend fun status(profile: VoyagerFollowBusinessRequest): Result<VoyagerFollowBusinessResponse> {
        return try {
            val response: HttpResponse = httpClient.post("${ApiConstants.BASE_URL}${ApiConstants.Endpoints.VOYAGER_FOLLOW_BUSINESS}") {
                contentType(ContentType.Application.Json)
                setBody(profile)
            }
            if (response.status == HttpStatusCode.Created) {
                val result: VoyagerFollowBusinessResponse = response.body()
                Result.success(result)
            } else {
                Result.failure(Exception("API Error: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network Error: ${e.localizedMessage}", e))
        }
    }

    suspend fun unFollow(profile: VoyagerFollowBusinessRequest): Result<VoyagerFollowBusinessResponse> {
        return try {
            val response: HttpResponse = httpClient.post("${ApiConstants.BASE_URL}${ApiConstants.Endpoints.VOYAGER_UNFOLLOW_BUSINESS}") {
                contentType(ContentType.Application.Json)
                setBody(profile)
            }
            if (response.status == HttpStatusCode.OK) {
                val result: VoyagerFollowBusinessResponse = response.body()
                Result.success(result)
            } else {
                Result.failure(Exception("API Error: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network Error: ${e.localizedMessage}", e))
        }
    }
}
