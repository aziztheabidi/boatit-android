package com.boatit.boatsharing.features.signup.general.repository

import com.boatit.boatsharing.data.network.di.ApiConstants
import com.boatit.boatsharing.data.network.retrofit.ApiExecutor
import com.boatit.boatsharing.features.signup.general.model.VerifyEmailRequest
import com.boatit.boatsharing.features.signup.general.model.VerifyEmailResponse

class VerifyEmailRepository(
    private val apiExecutor: ApiExecutor,
) : IVerifyEmailRepository {
    override suspend fun verifyEmail(
        email: String,
        otp: String,
    ): Result<VerifyEmailResponse> =
        apiExecutor.post(
            endpoint = ApiConstants.Endpoints.VERIFY,
            body = VerifyEmailRequest(email, otp),
            successCode = 200,
        )
}
