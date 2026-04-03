package com.boatit.boatsharing.ui.voyager.dashboard.repository

import android.content.Context
import com.boatit.boatsharing.network.di.ApiConstants
import com.boatit.boatsharing.ui.voyager.dashboard.model.NearbyPlacesResponse
import com.boatit.boatsharing.ui.voyager.dashboard.model.VoyageCategoryDropdownResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class FetchCategoryRepo(
    private val httpClient: HttpClient,
    private val context: Context
) {
    suspend fun getNearbyPlaces(): Result<VoyageCategoryDropdownResponse> {
        return try {
            val response: HttpResponse = httpClient.get("${ApiConstants.BASE_URL}${ApiConstants.Endpoints.CATEGORY}")
            if (response.status == HttpStatusCode.OK) {
                val placesResponse: VoyageCategoryDropdownResponse = response.body()
                Result.success(placesResponse)
            } else {
                
                Result.failure(Exception("Failed to fetch places: HTTP ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error fetching places: ${e.localizedMessage}", e))
        }
    }
}
