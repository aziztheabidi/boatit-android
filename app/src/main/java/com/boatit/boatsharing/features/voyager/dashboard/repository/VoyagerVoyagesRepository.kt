@file:Suppress(
    "ktlint:standard:filename",
)

package com.boatit.boatsharing.features.voyager.dashboard.repository

import com.boatit.boatsharing.data.network.di.ApiConstants
import com.boatit.boatsharing.data.network.di.networkFailure
import com.boatit.boatsharing.data.network.di.toResult
import com.boatit.boatsharing.features.voyager.dashboard.model.VoyagerVoyagesResponse
import com.boatit.boatsharing.data.local.prefmanager.UserSessionStore
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode

class VoyagerVoyagesRepository(
    private val httpClient: HttpClient,
    private val userSessionStore: UserSessionStore,
) {
    suspend fun voyages(): Result<VoyagerVoyagesResponse> {
        return try {
            val userId = userSessionStore.currentUserId()
            val response: HttpResponse =
                httpClient.get("${ApiConstants.BASE_URL}${ApiConstants.Endpoints.GET_Voyager_PAST_VOYAGES}") {
                    url {
                        parameters.append("UserId", userId)
                    }
                }

            if (response.status == HttpStatusCode.OK) {
                response.toResult<VoyagerVoyagesResponse>(successStatus = HttpStatusCode.OK)
            } else {
                val parsed: VoyagerVoyagesResponse = response.body()
                Result.failure(Exception("API Error: ${parsed.Message}"))
            }
        } catch (e: Exception) {
            networkFailure("Network Error", e)
        }
    }
}
