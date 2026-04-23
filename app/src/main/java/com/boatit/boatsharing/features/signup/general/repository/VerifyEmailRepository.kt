package com.boatit.boatsharing.features.signup.general.repository

import com.boatit.boatsharing.data.network.di.ApiConstants
import com.boatit.boatsharing.data.network.di.executePostRequest
import com.boatit.boatsharing.data.network.di.networkFailure
import com.boatit.boatsharing.domain.core.ExceptionMapper
import com.boatit.boatsharing.features.signup.general.model.VerifyEmailRequest
import com.boatit.boatsharing.features.signup.general.model.VerifyEmailResponse
import io.ktor.client.HttpClient
import io.ktor.http.HttpStatusCode

class VerifyEmailRepository(
    private val httpClient: HttpClient,
) : IVerifyEmailRepository {
    override suspend fun verifyEmail(
        email: String,
        otp: String,
    ): Result<VerifyEmailResponse> {
        return executePostRequest(
            httpClient = httpClient,
            url = "${ApiConstants.BASE_URL}${ApiConstants.Endpoints.VERIFY}",
            requestBody = VerifyEmailRequest(email, otp),
            successStatus = HttpStatusCode.OK,
            onApiError = { _, status -> ExceptionMapper.mapHttpException(status.value, status.description) },
            onException = { e -> networkFailure("Error during email verification", e) },
        )
    }
}
