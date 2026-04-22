package com.boatit.boatsharing.features.signup.general.repository

import com.boatit.boatsharing.features.signup.general.model.VerifyEmailResponse

interface IVerifyEmailRepository {
    suspend fun verifyEmail(
        email: String,
        otp: String,
    ): Result<VerifyEmailResponse>
}
