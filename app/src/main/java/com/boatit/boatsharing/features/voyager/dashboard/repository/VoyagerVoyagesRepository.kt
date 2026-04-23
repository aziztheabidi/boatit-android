@file:Suppress(
    "ktlint:standard:filename",
)

package com.boatit.boatsharing.features.voyager.dashboard.repository

import com.boatit.boatsharing.data.network.di.ApiConstants
import com.boatit.boatsharing.data.network.di.executeGetRequest
import com.boatit.boatsharing.data.network.di.networkFailure
import com.boatit.boatsharing.data.network.di.toResult
import com.boatit.boatsharing.features.voyager.dashboard.model.VoyagerVoyagesResponse
import com.boatit.boatsharing.data.local.prefmanager.UserSessionStore
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.http.HttpStatusCode

class VoyagerVoyagesRepository(
    private val httpClient: HttpClient,
    private val userSessionStore: UserSessionStore,
) {
    suspend fun voyages(): Result<VoyagerVoyagesResponse> {
        val userId = userSessionStore.currentUserId()
        return executeGetRequest(
            httpClient = httpClient,
            url = "${ApiConstants.BASE_URL}${ApiConstants.Endpoints.GET_VOYAGER_PAST_VOYAGES}",
            requestConfig = {
                url {
                    parameters.append("UserId", userId)
                }
            },
            handleResponse = { response ->
                if (response.status == HttpStatusCode.OK) {
                    response.toResult<VoyagerVoyagesResponse>(successStatus = HttpStatusCode.OK)
                } else {
                    val parsed: VoyagerVoyagesResponse = response.body()
                    Result.failure(Exception("API Error: ${parsed.Message}"))
                }
            },
            onException = { e -> networkFailure("Network Error", e) },
        )
    }
}
