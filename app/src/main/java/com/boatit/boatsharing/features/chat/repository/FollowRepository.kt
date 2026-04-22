package com.boatit.boatsharing.features.chat.repository

import com.boatit.boatsharing.data.network.di.ApiConstants
import com.boatit.boatsharing.data.network.di.networkFailure
import com.boatit.boatsharing.data.network.di.toResult
import com.boatit.boatsharing.features.chat.model.ComplainRequest
import com.boatit.boatsharing.features.chat.model.FollowRequest
import com.boatit.boatsharing.features.chat.model.FollowResponse
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class FollowRepository(
    private val httpClient: HttpClient,
) {
    suspend fun findboat(profile: FollowRequest): Result<FollowResponse> {
        return try {
            val response: HttpResponse =
                httpClient.post("${ApiConstants.BASE_URL}${ApiConstants.Endpoints.FOLLOW_VOYAGER}") {
                    contentType(ContentType.Application.Json)
                    setBody(profile)
                }
            response.toResult<FollowResponse>(successStatus = HttpStatusCode.Created)
        } catch (e: Exception) {
            networkFailure("Error fetching places", e)
        }
    }

    suspend fun complian(profile: ComplainRequest): Result<FollowResponse> {
        return try {
            val response: HttpResponse =
                httpClient.post("${ApiConstants.BASE_URL}${ApiConstants.Endpoints.COMPLAIN_VOYAGER}") {
                    contentType(ContentType.Application.Json)
                    setBody(profile)
                }
            response.toResult<FollowResponse>(successStatus = HttpStatusCode.Created)
        } catch (e: Exception) {
            networkFailure("Error fetching places", e)
        }
    }
}
