@file:Suppress("ktlint:standard:filename")

package com.boatit.boatsharing.features.voyager.dashboard.repository

import android.util.Log
import com.boatit.boatsharing.data.network.di.ApiConstants
import com.boatit.boatsharing.data.network.di.executeGetRequest
import com.boatit.boatsharing.data.network.di.networkFailure
import com.boatit.boatsharing.data.network.di.toResult
import com.boatit.boatsharing.features.voyager.dashboard.model.ActiveVoyageResponse
import com.boatit.boatsharing.features.voyager.dashboard.model.VoyagerVoyagesResponse
import com.boatit.boatsharing.data.local.prefmanager.UserSessionStore
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.http.HttpStatusCode

class GetActiveVoyageRepository(
    private val httpClient: HttpClient,
    private val userSessionStore: UserSessionStore,
) {
    suspend fun voyages(): Result<ActiveVoyageResponse> {
        val userId = userSessionStore.currentUserId()
        return executeGetRequest(
            httpClient = httpClient,
            url = "${ApiConstants.BASE_URL}${ApiConstants.Endpoints.GET_ACTIVE_VOYAGES}",
            requestConfig = {
                url {
                    parameters.append("VoyagerUserId", userId)
                }
            },
            handleResponse = { response ->
                if (response.status == HttpStatusCode.OK) {
                    val result = response.toResult<ActiveVoyageResponse>(successStatus = HttpStatusCode.OK)
                    Log.e("popup_res_main", response.body())
                    result
                } else {
                    val result: VoyagerVoyagesResponse = response.body()
                    Result.failure(Exception("API Error: ${result.Message}"))
                }
            },
            onException = { e -> networkFailure("Network Error", e) },
        )
    }
}
