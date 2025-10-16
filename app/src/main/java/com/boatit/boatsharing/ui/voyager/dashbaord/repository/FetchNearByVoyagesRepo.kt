package com.boatit.boatsharing.ui.voyager.dashbaord.repository

import android.content.Context
import com.boatit.boatsharing.network.di.ApiConstants
import com.boatit.boatsharing.ui.voyager.dashbaord.model.NearbyPlacesResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class FetchNearByVoyagesRepo(
    private val httpClient: HttpClient,
    private val context: Context
) {
    suspend fun getNearbyPlaces(): Result<NearbyPlacesResponse> {
        return try {
            val response: HttpResponse = httpClient.get("${ApiConstants.BASE_URL}${ApiConstants.Endpoints.DOCK}")
            if (response.status == HttpStatusCode.OK) {
                val placesResponse: NearbyPlacesResponse = response.body()
                Result.success(placesResponse)
            } else {
                
                Result.failure(Exception("Failed to fetch places: HTTP ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error fetching places: ${e.localizedMessage}", e))
        }
    }
}
