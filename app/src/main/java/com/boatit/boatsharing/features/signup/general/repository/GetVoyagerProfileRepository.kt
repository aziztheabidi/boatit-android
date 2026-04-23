package com.boatit.boatsharing.features.signup.general.repository

import com.boatit.boatsharing.data.network.di.ApiConstants
import com.boatit.boatsharing.data.network.di.executeGetRequest
import com.boatit.boatsharing.data.network.di.networkFailure
import com.boatit.boatsharing.data.network.di.toResult
import com.boatit.boatsharing.features.signup.general.model.GetVoyagerProfileResponse
import com.boatit.boatsharing.data.local.prefmanager.UserSessionStore
import io.ktor.client.HttpClient
import io.ktor.http.HttpStatusCode

class GetVoyagerProfileRepository(
    private val httpClient: HttpClient,
    private val userSessionStore: UserSessionStore,
) : IGetVoyagerProfileRepository {
    override suspend fun getVoyagerProfile(): Result<GetVoyagerProfileResponse> {
        val userId = userSessionStore.currentUserId()
        return executeGetRequest(
            httpClient = httpClient,
            url = "${ApiConstants.BASE_URL}${ApiConstants.Endpoints.GET_VOYAGER_PROFILE}",
            requestConfig = {
                url {
                    parameters.append("UserId", userId)
                }
            },
            handleResponse = { response ->
                if (response.status == HttpStatusCode.OK) {
                    response.toResult<GetVoyagerProfileResponse>(successStatus = HttpStatusCode.OK)
                } else {
                    Result.failure(Exception("Failed to fetch profile: HTTP ${response.status}"))
                }
            },
            onException = { e -> networkFailure("Error fetching voyager profile", e) },
        )
    }
}
