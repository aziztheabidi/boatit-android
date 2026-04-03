package com.boatit.boatsharing.data.remote.api

import com.boatit.boatsharing.ui.captain.dashboard.model.AcceptVoyageRequest
import com.boatit.boatsharing.ui.captain.dashboard.model.AcceptVoyageResponse
import com.boatit.boatsharing.ui.captain.dashboard.model.VoyageCompleteRequest
import com.boatit.boatsharing.ui.captain.dashboard.model.VoyageCompleteResponse
import com.boatit.boatsharing.ui.captain.dashboard.model.VoyageStartRequest
import com.boatit.boatsharing.ui.captain.dashboard.model.VoyageStartResponse
import com.boatit.boatsharing.ui.captain.voyages.model.CaptainVoyagesResponse
import com.boatit.boatsharing.ui.voyager.dashboard.model.FindBoatRequest
import com.boatit.boatsharing.ui.voyager.dashboard.model.FindBoatResponse
import com.boatit.boatsharing.ui.voyager.dashboard.model.VoyagePaymentRequest
import com.boatit.boatsharing.ui.voyager.dashboard.model.VoyagePaymentResponse
import com.boatit.boatsharing.ui.voyager.dashboard.model.VoyagerVoyagesResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/** Voyage lifecycle and captain dashboard voyage listing. */
interface VoyageApi {

    @POST("Voyage/FindBoat")
    suspend fun findBoat(@Body body: FindBoatRequest): Response<FindBoatResponse>

    @POST("Voyage/Accept")
    suspend fun acceptVoyage(@Body body: AcceptVoyageRequest): Response<AcceptVoyageResponse>

    @POST("Voyage/Start")
    suspend fun startVoyage(@Body body: VoyageStartRequest): Response<VoyageStartResponse>

    @POST("Voyage/Complete")
    suspend fun completeVoyage(@Body body: VoyageCompleteRequest): Response<VoyageCompleteResponse>

    @POST("Voyage/Cancel")
    suspend fun cancelVoyage(@Body body: VoyageCompleteRequest): Response<VoyageCompleteResponse>

    @POST("Voyage/Payment")
    suspend fun payment(@Body body: VoyagePaymentRequest): Response<VoyagePaymentResponse>

    @GET("CaptainDashboard/GetVoyages")
    suspend fun getCaptainVoyages(@Query("CaptainUserId") captainUserId: String): Response<CaptainVoyagesResponse>

    @GET("CaptainDashboard/GetVoyages")
    suspend fun getVoyagerVoyages(@Query("UserId") userId: String): Response<VoyagerVoyagesResponse>
}
