package com.boatit.boatsharing.data.remote.api

import com.boatit.boatsharing.ui.signup.business.model.BusinessInfoRequest
import com.boatit.boatsharing.ui.signup.business.model.BusinessInfoResponse
import com.boatit.boatsharing.ui.signup.business.model.BusinessProfileRequest
import com.boatit.boatsharing.ui.signup.business.model.GetBusinessProfileResponse
import com.boatit.boatsharing.ui.signup.business.model.SaveBusinessAboutRequest
import com.boatit.boatsharing.ui.signup.business.model.SaveBusinessAboutResponse
import com.boatit.boatsharing.ui.signup.business.model.SaveBusinessInfoResponse
import com.boatit.boatsharing.ui.signup.business.model.SaveBusinessLogoResponse
import com.boatit.boatsharing.ui.signup.business.model.SaveBusinessProfileResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

/** Business onboarding and profile endpoints. */
interface BusinessProfileApi {

    @POST("BusinessProfile/Save")
    suspend fun saveBusinessProfile(@Body body: BusinessProfileRequest): Response<SaveBusinessProfileResponse>

    @GET("BusinessProfile/GetByUserId")
    suspend fun getBusinessProfile(@Query("UserId") userId: String): Response<GetBusinessProfileResponse>

    @POST("BusinessInfo/Save")
    suspend fun saveBusinessInfo(@Body body: BusinessInfoRequest): Response<SaveBusinessInfoResponse>

    @GET("BusinessInfo/GetByUserId")
    suspend fun getBusinessInfo(@Query("UserId") userId: String): Response<BusinessInfoResponse>

    @POST("BusinessInfo/SaveAbout")
    suspend fun saveBusinessAbout(@Body body: SaveBusinessAboutRequest): Response<SaveBusinessAboutResponse>

    @Multipart
    @POST("BusinessInfo/SaveLogo")
    suspend fun saveBusinessLogo(
        @Part("UserId") userId: RequestBody,
        @Part logo: MultipartBody.Part,
    ): Response<SaveBusinessLogoResponse>
}
