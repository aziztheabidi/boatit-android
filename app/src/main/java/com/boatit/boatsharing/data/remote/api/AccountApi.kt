package com.boatit.boatsharing.data.remote.api

import com.boatit.boatsharing.ui.forgotpassword.model.ForgotPassRequest
import com.boatit.boatsharing.ui.forgotpassword.view.ForgotPassResponse
import com.boatit.boatsharing.ui.login.model.LoginRequest
import com.boatit.boatsharing.ui.login.model.LoginResponse
import com.boatit.boatsharing.ui.signup.general.model.PasswordRequest
import com.boatit.boatsharing.ui.userroles.model.RoleRequest
import com.boatit.boatsharing.ui.userroles.model.RoleResponse
import com.boatit.boatsharing.ui.userroles.model.UpdateDeviceTokenRequest
import com.boatit.boatsharing.ui.userroles.model.UpdateDeviceTokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/** Account, auth, role, and device token endpoints under `Account/`. */
interface AccountApi {

    @POST("Account/Login")
    suspend fun login(@Body body: LoginRequest): Response<LoginResponse>

    @POST("Account/UpdateRole")
    suspend fun updateRole(@Body body: RoleRequest): Response<RoleResponse>

    @POST("Account/UpdateDeviceToken")
    suspend fun updateDeviceToken(@Body body: UpdateDeviceTokenRequest): Response<UpdateDeviceTokenResponse>

    @POST("Account/Register")
    suspend fun register(@Body body: PasswordRequest): Response<LoginResponse>

    @POST("Account/ForgotPassword")
    suspend fun forgotPassword(@Body body: ForgotPassRequest): Response<ForgotPassResponse>
}
