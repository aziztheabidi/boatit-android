@file:Suppress("ktlint:standard:filename")

package com.boatit.boatsharing.features.voyager.dashboard.repository

import com.boatit.boatsharing.data.network.di.ApiConstants
import com.boatit.boatsharing.data.network.di.networkFailure
import com.boatit.boatsharing.data.network.di.toResult
import com.boatit.boatsharing.features.voyager.dashboard.model.VoyagerFollowBusinessRequest
import com.boatit.boatsharing.features.voyager.dashboard.model.VoyagerFollowBusinessResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class FollowBusinessRepository(private val httpClient: HttpClient) {
    suspend fun status(profile: VoyagerFollowBusinessRequest): Result<VoyagerFollowBusinessResponse> {
        return try {
            val response: HttpResponse =
                httpClient.post("${ApiConstants.BASE_URL}${ApiConstants.Endpoints.VOYAGER_FOLLOW_BUSINESS}") {
                    contentType(ContentType.Application.Json)
                    setBody(profile)
                }
            response.toResult<VoyagerFollowBusinessResponse>(successStatus = HttpStatusCode.Created)
        } catch (e: Exception) {
            networkFailure("Network Error", e)
        }
    }

    suspend fun unFollow(profile: VoyagerFollowBusinessRequest): Result<VoyagerFollowBusinessResponse> {
        return try {
            val response: HttpResponse =
                httpClient.post("${ApiConstants.BASE_URL}${ApiConstants.Endpoints.VOYAGER_UNFOLLOW_BUSINESS}") {
                    contentType(ContentType.Application.Json)
                    setBody(profile)
                }
            response.toResult<VoyagerFollowBusinessResponse>(successStatus = HttpStatusCode.OK)
        } catch (e: Exception) {
            networkFailure("Network Error", e)
        }
    }
}
