package com.boatit.boatsharing.features.voyager.dashboard.repository

import android.content.Context
import com.boatit.boatsharing.data.network.di.ApiConstants
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
        return try {
            val response: HttpResponse = httpClient.get("${ApiConstants.BASE_URL}${ApiConstants.Endpoints.DOCK}")
            response.toResult<NearbyPlacesResponse>(successStatus = HttpStatusCode.OK)
        } catch (e: Exception) {
            networkFailure("Error fetching places", e)
        }
    }
}
