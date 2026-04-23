@file:Suppress("ktlint:standard:filename")

package com.boatit.boatsharing.features.voyager.dashboard.repository

import com.boatit.boatsharing.data.network.di.ApiConstants
import com.boatit.boatsharing.data.network.di.executePostRequest
import com.boatit.boatsharing.data.network.di.networkFailure
import com.boatit.boatsharing.domain.core.ExceptionMapper
import com.boatit.boatsharing.features.voyager.dashboard.model.VoyagerFollowBusinessRequest
import com.boatit.boatsharing.features.voyager.dashboard.model.VoyagerFollowBusinessResponse
import io.ktor.client.HttpClient
import io.ktor.http.HttpStatusCode

class FollowBusinessRepository(private val httpClient: HttpClient) {
    suspend fun status(profile: VoyagerFollowBusinessRequest): Result<VoyagerFollowBusinessResponse> {
        return executePostRequest(
            httpClient = httpClient,
            url = "${ApiConstants.BASE_URL}${ApiConstants.Endpoints.VOYAGER_FOLLOW_BUSINESS}",
            requestBody = profile,
            successStatus = HttpStatusCode.Created,
            onApiError = { _, status -> ExceptionMapper.mapHttpException(status.value, status.description) },
            onException = { e -> networkFailure("Network Error", e) },
        )
    }

    suspend fun unFollow(profile: VoyagerFollowBusinessRequest): Result<VoyagerFollowBusinessResponse> {
        return executePostRequest(
            httpClient = httpClient,
            url = "${ApiConstants.BASE_URL}${ApiConstants.Endpoints.VOYAGER_UNFOLLOW_BUSINESS}",
            requestBody = profile,
            successStatus = HttpStatusCode.OK,
            onApiError = { _, status -> ExceptionMapper.mapHttpException(status.value, status.description) },
            onException = { e -> networkFailure("Network Error", e) },
        )
    }
}
