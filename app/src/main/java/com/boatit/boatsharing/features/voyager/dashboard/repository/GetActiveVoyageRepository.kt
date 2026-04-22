@file:Suppress("ktlint:standard:filename")

package com.boatit.boatsharing.features.voyager.dashboard.repository

import android.util.Log
import com.boatit.boatsharing.data.network.di.ApiConstants
import com.boatit.boatsharing.data.network.di.networkFailure
import com.boatit.boatsharing.data.network.di.toResult
import com.boatit.boatsharing.features.voyager.dashboard.model.ActiveVoyageResponse
import com.boatit.boatsharing.features.voyager.dashboard.model.VoyagerVoyagesResponse
import com.boatit.boatsharing.data.local.prefmanager.UserSessionStore
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode

class GetActiveVoyageRepository(
    private val httpClient: HttpClient,
    private val userSessionStore: UserSessionStore,
) {
    suspend fun voyages(): Result<ActiveVoyageResponse> {
        return try {
            val userId = userSessionStore.currentUserId()
            val response: HttpResponse =
                httpClient.get("${ApiConstants.BASE_URL}${ApiConstants.Endpoints.GET_ACTIVE_VOYAGES}") {
                    url {
                        parameters.append("VoyagerUserId", userId)
                    }
                }

            if (response.status == HttpStatusCode.OK) {
                val result = response.toResult<ActiveVoyageResponse>(successStatus = HttpStatusCode.OK)
                Log.e("popup_res_main", response.body())
                result
            } else {
                val result: VoyagerVoyagesResponse = response.body()
                Result.failure(Exception("API Error: ${result.Message}"))
            }
        } catch (e: Exception) {
            networkFailure("Network Error", e)
        }
    }
}
