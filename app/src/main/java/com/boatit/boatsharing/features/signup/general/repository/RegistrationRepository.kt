package com.boatit.boatsharing.features.signup.general.repository

import com.boatit.boatsharing.data.network.di.ApiConstants
import com.boatit.boatsharing.data.network.di.executePostRequest
import com.boatit.boatsharing.data.network.di.networkFailure
import com.boatit.boatsharing.domain.core.ExceptionMapper
import com.boatit.boatsharing.features.signup.general.model.RegistrationRequest
import com.boatit.boatsharing.features.signup.general.model.RegistrationResponse
import io.ktor.client.HttpClient
import io.ktor.http.HttpStatusCode

class RegistrationRepository(
    private val httpClient: HttpClient,
) : IRegistrationRepository {
    override suspend fun tempRegister(
        username: String,
        phoneNumber: String,
        email: String,
    ): Result<RegistrationResponse> {
        return executePostRequest(
            httpClient = httpClient,
            url = "${ApiConstants.BASE_URL}${ApiConstants.Endpoints.ADD}",
            requestBody = RegistrationRequest(username, phoneNumber, email),
            successStatus = HttpStatusCode.OK,
            onApiError = { _, status -> ExceptionMapper.mapHttpException(status.value, status.description) },
            onException = { e -> networkFailure("Error during registration", e) },
        )
    }
}
