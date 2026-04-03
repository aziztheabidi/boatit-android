package com.boatit.boatsharing.data.remote.api

import com.boatit.boatsharing.ui.voyager.dashboard.model.NearbyPlacesResponse
import retrofit2.Response
import retrofit2.http.GET

interface DockApi {

    @GET("Dock/GetActive")
    suspend fun getActiveDocks(): Response<NearbyPlacesResponse>
}
