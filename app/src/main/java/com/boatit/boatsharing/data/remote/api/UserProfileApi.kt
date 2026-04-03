package com.boatit.boatsharing.data.remote.api

import com.boatit.boatsharing.ui.captain.availabilitystatus.model.CaptainAvailabilityRequest
import com.boatit.boatsharing.ui.captain.availabilitystatus.model.CaptainAvailabilityResponse
import com.boatit.boatsharing.ui.signup.captain.model.CaptainProfileRequest
import com.boatit.boatsharing.ui.signup.captain.model.CaptainProfileResponse
import com.boatit.boatsharing.ui.signup.captain.model.GetCaptainBoatResponse
import com.boatit.boatsharing.ui.signup.captain.model.GetCaptainDocumentResponse
import com.boatit.boatsharing.ui.signup.captain.model.GetCaptainProfileResponse
import com.boatit.boatsharing.ui.signup.captain.model.SaveCaptainBoatRequest
import com.boatit.boatsharing.ui.signup.captain.model.SaveCaptainBoatResponse
import com.boatit.boatsharing.ui.signup.captain.model.SaveCaptainDocumentRequest
import com.boatit.boatsharing.ui.signup.captain.model.SaveCaptainDocumentResponse
import com.boatit.boatsharing.ui.signup.general.model.GetVoyagerProfileResponse
import com.boatit.boatsharing.ui.signup.general.model.VoyagerProfileRequest
import com.boatit.boatsharing.ui.signup.general.model.VoyagerProfileResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/** Voyager profile, captain profile, boat, documents, and availability. */
interface UserProfileApi {

    @POST("VoyagerProfile/Save")
    suspend fun saveVoyagerProfile(@Body body: VoyagerProfileRequest): Response<VoyagerProfileResponse>

    @GET("VoyagerProfile/GetByUserId")
    suspend fun getVoyagerProfile(@Query("UserId") userId: String): Response<GetVoyagerProfileResponse>

    @POST("CaptainProfile/Save")
    suspend fun saveCaptainProfile(@Body body: CaptainProfileRequest): Response<CaptainProfileResponse>

    @GET("CaptainProfile/GetByUserId")
    suspend fun getCaptainProfile(@Query("UserId") userId: String): Response<GetCaptainProfileResponse>

    @POST("CaptainBoat/Save")
    suspend fun saveCaptainBoat(@Body body: SaveCaptainBoatRequest): Response<SaveCaptainBoatResponse>

    @GET("CaptainBoat/GetByUserId")
    suspend fun getCaptainBoat(@Query("UserId") userId: String): Response<GetCaptainBoatResponse>

    @POST("CaptainDocument/Save")
    suspend fun saveCaptainDocument(@Body body: SaveCaptainDocumentRequest): Response<SaveCaptainDocumentResponse>

    @GET("CaptainDocument/GetByUserId")
    suspend fun getCaptainDocuments(@Query("UserId") userId: String): Response<GetCaptainDocumentResponse>

    @POST("CaptainProfile/Availability")
    suspend fun updateCaptainAvailability(@Body body: CaptainAvailabilityRequest): Response<CaptainAvailabilityResponse>
}
