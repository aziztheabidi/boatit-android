package com.boatit.boatsharing.features.signup.general.repository

import com.boatit.boatsharing.data.network.di.ApiConstants
import com.boatit.boatsharing.data.network.di.networkFailure
import com.boatit.boatsharing.data.network.di.toResult
import com.boatit.boatsharing.features.signup.general.model.GetVoyagerProfileResponse
import com.boatit.boatsharing.data.local.prefmanager.UserSessionStore
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode

class GetVoyagerProfileRepository(
    private val httpClient: HttpClient,
    private val userSessionStore: UserSessionStore,
) : IGetVoyagerProfileRepository {
    override suspend fun getVoyagerProfile(): Result<GetVoyagerProfileResponse> {
        return try {
            val userId = userSessionStore.currentUserId()
            val response: HttpResponse =
                httpClient.get("${ApiConstants.BASE_URL}${ApiConstants.Endpoints.GET_VOYAGER_PROFILE}") {
                    url {
                        parameters.append("UserId", userId)
                    }
                }

            if (response.status == HttpStatusCode.OK) {
                response.toResult<GetVoyagerProfileResponse>(successStatus = HttpStatusCode.OK)
            } else {
                Result.failure(Exception("Failed to fetch profile: HTTP ${response.status}"))
            }
        } catch (e: Exception) {
            networkFailure("Error fetching voyager profile", e)
        }
    }
}
