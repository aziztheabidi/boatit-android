@file:Suppress("ktlint:standard:filename")

package com.boatit.boatsharing.features.voyager.dashboard.repository

import com.boatit.boatsharing.data.network.di.ApiConstants
import com.boatit.boatsharing.data.network.di.executeGetRequest
import com.boatit.boatsharing.data.network.di.networkFailure
import com.boatit.boatsharing.data.network.di.toResult
import com.boatit.boatsharing.features.voyager.dashboard.model.FollowedVoyagersResponse
import com.boatit.boatsharing.data.local.prefmanager.UserSessionStore
import io.ktor.client.HttpClient
import io.ktor.http.HttpStatusCode

class FollowedVoyagerRepository(
    private val httpClient: HttpClient,
    private val userSessionStore: UserSessionStore,
) {
    suspend fun getFollowedVoyagers(): Result<FollowedVoyagersResponse> {
        val userId = userSessionStore.currentUserId()
        return executeGetRequest(
            httpClient = httpClient,
            url = "${ApiConstants.BASE_URL}${ApiConstants.Endpoints.GET_VOYAGERS_LIST}",
            requestConfig = {
                url {
                    parameters.append("UserId", userId)
                }
            },
            handleResponse = { response ->
                response.toResult<FollowedVoyagersResponse>(successStatus = HttpStatusCode.OK)
            },
            onException = { e -> networkFailure("Network Error", e) },
        )
    }
}
