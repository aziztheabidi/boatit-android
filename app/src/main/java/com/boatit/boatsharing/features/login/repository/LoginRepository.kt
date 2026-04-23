package com.boatit.boatsharing.features.login.repository

import com.boatit.boatsharing.data.network.di.ApiConstants
import com.boatit.boatsharing.data.network.di.executePostRequest
import com.boatit.boatsharing.data.network.di.networkFailure
import com.boatit.boatsharing.domain.core.ExceptionMapper
import com.boatit.boatsharing.features.login.model.LoginRequest
import com.boatit.boatsharing.features.login.model.LoginResponse
import io.ktor.client.HttpClient
import io.ktor.http.HttpStatusCode

class LoginRepository(
    private val httpClient: HttpClient,
) : ILoginRepository {
    override suspend fun login(
        username: String,
        password: String,
    ): Result<LoginResponse> {
        return executePostRequest(
            httpClient = httpClient,
            url = "${ApiConstants.BASE_URL}${ApiConstants.Endpoints.LOGIN}",
            requestBody = LoginRequest(username, password),
            successStatus = HttpStatusCode.OK,
            onApiError = { _, status -> ExceptionMapper.mapHttpException(status.value, status.description) },
            onException = { e -> networkFailure("Error during login", e) },
        )
    }
}
