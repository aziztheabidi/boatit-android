package com.boatit.boatsharing.features.voyager.dashboard.repository

import android.content.Context
import com.boatit.boatsharing.data.network.di.ApiConstants
import com.boatit.boatsharing.data.network.di.executeGetRequest
import com.boatit.boatsharing.data.network.di.networkFailure
import com.boatit.boatsharing.data.network.di.toResult
import com.boatit.boatsharing.features.voyager.dashboard.model.VoyageCategoryDropdownResponse
import io.ktor.client.HttpClient
import io.ktor.http.HttpStatusCode

class FetchCategoryRepo(
    private val httpClient: HttpClient,
    private val context: Context,
) {
    suspend fun getNearbyPlaces(): Result<VoyageCategoryDropdownResponse> {
        return executeGetRequest(
            httpClient = httpClient,
            url = "${ApiConstants.BASE_URL}${ApiConstants.Endpoints.CATEGORY}",
            handleResponse = { response ->
                response.toResult<VoyageCategoryDropdownResponse>(successStatus = HttpStatusCode.OK)
            },
            onException = { e -> networkFailure("Error fetching places", e) },
        )
    }
}
