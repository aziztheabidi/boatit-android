package com.boatit.boatsharing.features.chat.repository

import com.boatit.boatsharing.data.network.di.ApiConstants
import com.boatit.boatsharing.data.network.di.executePostRequest
import com.boatit.boatsharing.data.network.di.networkFailure
import com.boatit.boatsharing.domain.core.ExceptionMapper
import com.boatit.boatsharing.features.chat.model.ComplainRequest
import com.boatit.boatsharing.features.chat.model.FollowRequest
import com.boatit.boatsharing.features.chat.model.FollowResponse
import io.ktor.client.HttpClient
import io.ktor.http.HttpStatusCode

class FollowRepository(
    private val httpClient: HttpClient,
) {
    suspend fun findboat(profile: FollowRequest): Result<FollowResponse> {
        return executePostRequest(
            httpClient = httpClient,
            url = "${ApiConstants.BASE_URL}${ApiConstants.Endpoints.FOLLOW_VOYAGER}",
            requestBody = profile,
            successStatus = HttpStatusCode.Created,
            onApiError = { _, status -> ExceptionMapper.mapHttpException(status.value, status.description) },
            onException = { e -> networkFailure("Error fetching places", e) },
        )
    }

    suspend fun complain(profile: ComplainRequest): Result<FollowResponse> {
        return executePostRequest(
            httpClient = httpClient,
            url = "${ApiConstants.BASE_URL}${ApiConstants.Endpoints.COMPLAIN_VOYAGER}",
            requestBody = profile,
            successStatus = HttpStatusCode.Created,
            onApiError = { _, status -> ExceptionMapper.mapHttpException(status.value, status.description) },
            onException = { e -> networkFailure("Error fetching places", e) },
        )
    }

    @Deprecated("Typo kept for compatibility. Use complain(profile).")
    suspend fun complian(profile: ComplainRequest): Result<FollowResponse> = complain(profile)
}
