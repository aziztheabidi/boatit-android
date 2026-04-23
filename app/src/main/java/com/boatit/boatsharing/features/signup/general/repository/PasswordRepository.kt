package com.boatit.boatsharing.features.signup.general.repository

import com.boatit.boatsharing.data.network.di.ApiConstants
import com.boatit.boatsharing.data.network.di.executePostRequest
import com.boatit.boatsharing.data.network.di.networkFailure
import com.boatit.boatsharing.domain.core.ExceptionMapper
import com.boatit.boatsharing.features.login.model.LoginResponse
import com.boatit.boatsharing.features.signup.general.model.PasswordRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.http.HttpStatusCode

class PasswordRepository(private val httpClient: HttpClient) : IPasswordRepository {
    override suspend fun passwordRepository(
        password: String,
        token: String,
    ): Result<LoginResponse> {
        return executePostRequest(
            httpClient = httpClient,
            url = "${ApiConstants.BASE_URL}${ApiConstants.Endpoints.REGISTER}",
            requestBody = PasswordRequest(password),
            successStatus = HttpStatusCode.Created,
            requestConfig = {
                header("Authorization", "Bearer $token")
            },
            onApiError = { _, status -> ExceptionMapper.mapHttpException(status.value, status.description) },
            onException = { e -> networkFailure("Error during password registration", e) },
        )
    }
}
