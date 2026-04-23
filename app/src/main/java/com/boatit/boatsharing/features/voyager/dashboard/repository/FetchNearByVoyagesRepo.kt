package com.boatit.boatsharing.features.voyager.dashboard.repository

import android.content.Context
import com.boatit.boatsharing.data.network.di.ApiConstants
import com.boatit.boatsharing.data.network.di.executeGetRequest
import com.boatit.boatsharing.data.network.di.networkFailure
import com.boatit.boatsharing.data.network.di.toResult
import com.boatit.boatsharing.features.voyager.dashboard.model.NearbyPlacesResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode

class FetchNearByVoyagesRepo(
    private val httpClient: HttpClient,
    private val context: Context,
) {
    suspend fun getNearbyPlaces(): Result<NearbyPlacesResponse> {
        return executeGetRequest(
            httpClient = httpClient,
            url = "${ApiConstants.BASE_URL}${ApiConstants.Endpoints.DOCK}",
            handleResponse = { response ->
                response.toResult<NearbyPlacesResponse>(successStatus = HttpStatusCode.OK)
            },
            onException = { e -> networkFailure("Error fetching places", e) },
        )
    }
}
