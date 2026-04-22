package com.boatit.boatsharing.features.forgotpassword.repository

import com.boatit.boatsharing.data.network.di.ApiConstants
import com.boatit.boatsharing.data.network.retrofit.ApiExecutor
import com.boatit.boatsharing.features.forgotpassword.data.dto.ForgotPassRequestDto
import com.boatit.boatsharing.features.forgotpassword.data.dto.ForgotPassResponseDto
import com.boatit.boatsharing.features.forgotpassword.domain.model.ForgotPasswordDomainModel
import com.boatit.boatsharing.features.forgotpassword.domain.model.toDomainModel

class ForgotPassRepository(
    private val apiExecutor: ApiExecutor,
) : IForgotPassRepository {
    override suspend fun forgotPassResp(email: String): Result<ForgotPasswordDomainModel> =
        apiExecutor.post<ForgotPassResponseDto>(
            endpoint = ApiConstants.Endpoints.FORGOTPASS,
            body = ForgotPassRequestDto(Email = email),
            successCode = 200,
        ).map { it.toDomainModel() }
}
