package com.boatit.boatsharing.features.login.repository

import com.boatit.boatsharing.data.network.di.ApiConstants
import com.boatit.boatsharing.data.network.retrofit.ApiExecutor
import com.boatit.boatsharing.features.login.model.LoginRequest
import com.boatit.boatsharing.features.login.model.LoginResponse

class LoginRepository(
    private val apiExecutor: ApiExecutor,
) : ILoginRepository {
    override suspend fun login(
        username: String,
        password: String,
    ): Result<LoginResponse> =
        apiExecutor.post(
            endpoint = ApiConstants.Endpoints.LOGIN,
            body = LoginRequest(username, password),
            successCode = 200,
        )
}
