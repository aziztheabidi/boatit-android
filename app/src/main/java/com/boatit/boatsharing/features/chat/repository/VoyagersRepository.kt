package com.boatit.boatsharing.features.chat.repository

import com.boatit.boatsharing.data.network.di.ApiConstants
import com.boatit.boatsharing.data.network.di.networkFailure
import com.boatit.boatsharing.data.network.di.toResult
import com.boatit.boatsharing.features.chat.model.ActiveVoyagersResponse
import com.boatit.boatsharing.data.local.prefmanager.UserSessionStore
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode

class VoyagersRepository(
    private val httpClient: HttpClient,
    private val userSessionStore: UserSessionStore,
) {
    suspend fun voyages(): Result<ActiveVoyagersResponse> {
        return try {
            val userId = userSessionStore.currentUserId()
            val response: HttpResponse =
                httpClient.get("${ApiConstants.BASE_URL}${ApiConstants.Endpoints.GET_VOYAGERS_LIST}") {
                    url {
                        parameters.append("UserId", userId)
                    }
                }
            response.toResult<ActiveVoyagersResponse>(successStatus = HttpStatusCode.OK)
        } catch (e: Exception) {
            networkFailure("Network Error", e)
        }
    }
}
