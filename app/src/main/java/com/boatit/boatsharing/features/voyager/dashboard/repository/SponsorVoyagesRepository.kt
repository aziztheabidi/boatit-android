package com.boatit.boatsharing.features.voyager.dashboard.repository

import com.boatit.boatsharing.data.network.di.ApiConstants
import com.boatit.boatsharing.data.network.di.networkFailure
import com.boatit.boatsharing.data.network.di.toResult
import com.boatit.boatsharing.features.voyager.dashboard.model.SponsorPayments
import com.boatit.boatsharing.data.local.prefmanager.UserSessionStore
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode

class SponsorVoyagesRepository(
    private val httpClient: HttpClient,
    private val userSessionStore: UserSessionStore,
) : ISponsorVoyagesRepository {
    override suspend fun voyages(): Result<SponsorPayments> {
        return try {
            val userId = userSessionStore.currentUserId()
            val response: HttpResponse =
                httpClient.get("${ApiConstants.BASE_URL}${ApiConstants.Endpoints.GET_SPONSOR_PAYMENTS}") {
                    url {
                        parameters.append("UserId", userId)
                    }
                }

            if (response.status == HttpStatusCode.OK) {
                response.toResult<SponsorPayments>(successStatus = HttpStatusCode.OK)
            } else {
                val result: SponsorPayments = response.body()
                Result.failure(Exception(result.Message ?: "Failed to fetch sponsor payments"))
            }
        } catch (e: Exception) {
            networkFailure("Error fetching sponsor payments", e)
        }
    }
}
