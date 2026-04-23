package com.boatit.boatsharing.features.forgotpassword.repository

import com.boatit.boatsharing.data.network.di.ApiConstants
import com.boatit.boatsharing.data.network.di.executePostRequest
import com.boatit.boatsharing.data.network.di.networkFailure
import com.boatit.boatsharing.domain.core.ExceptionMapper
import com.boatit.boatsharing.features.forgotpassword.data.dto.ForgotPassRequestDto
import com.boatit.boatsharing.features.forgotpassword.data.dto.ForgotPassResponseDto
import com.boatit.boatsharing.features.forgotpassword.domain.model.ForgotPasswordDomainModel
import com.boatit.boatsharing.features.forgotpassword.domain.model.toDomainModel
import io.ktor.client.HttpClient
import io.ktor.http.HttpStatusCode

class ForgotPassRepository(
    private val httpClient: HttpClient,
) : IForgotPassRepository {
    override suspend fun forgotPassResp(email: String): Result<ForgotPasswordDomainModel> {
        return executePostRequest<ForgotPassRequestDto, ForgotPassResponseDto>(
            httpClient = httpClient,
            url = "${ApiConstants.BASE_URL}${ApiConstants.Endpoints.FORGOTPASS}",
            requestBody = ForgotPassRequestDto(Email = email),
            successStatus = HttpStatusCode.OK,
            onApiError = { _, status -> ExceptionMapper.mapHttpException(status.value, status.description) },
            onException = { e -> networkFailure("Error during forgot password request", e) },
        ).map { it.toDomainModel() }
    }
}
