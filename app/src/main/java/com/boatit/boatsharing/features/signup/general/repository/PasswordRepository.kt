package com.boatit.boatsharing.features.signup.general.repository

import com.boatit.boatsharing.data.network.di.ApiConstants
import com.boatit.boatsharing.data.network.retrofit.ApiExecutor
import com.boatit.boatsharing.features.login.model.LoginResponse
import com.boatit.boatsharing.features.signup.general.model.PasswordRequest

class PasswordRepository(private val apiExecutor: ApiExecutor) : IPasswordRepository {
    override suspend fun passwordRepository(
        password: String,
        token: String,
    ): Result<LoginResponse> =
        apiExecutor.post(
            endpoint = ApiConstants.Endpoints.REGISTER,
            body = PasswordRequest(password),
            successCode = 201,
            authorization = "Bearer $token",
        )
}
