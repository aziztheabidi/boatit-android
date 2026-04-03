package com.boatit.boatsharing.data.remote.api

import com.boatit.boatsharing.ui.signup.general.model.RegistrationRequest
import com.boatit.boatsharing.ui.signup.general.model.RegistrationResponse
import com.boatit.boatsharing.ui.signup.general.model.VerifyEmailRequest
import com.boatit.boatsharing.ui.signup.general.model.VerifyEmailResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/** Pre-registration flow: `RegistrationTemp/`. */
interface RegistrationTempApi {

    @POST("RegistrationTemp/Verify")
    suspend fun verifyEmail(@Body body: VerifyEmailRequest): Response<VerifyEmailResponse>

    @POST("RegistrationTemp/Add")
    suspend fun addRegistration(@Body body: RegistrationRequest): Response<RegistrationResponse>
}
