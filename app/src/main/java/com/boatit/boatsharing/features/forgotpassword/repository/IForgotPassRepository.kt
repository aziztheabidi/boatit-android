package com.boatit.boatsharing.features.forgotpassword.repository

import com.boatit.boatsharing.features.forgotpassword.domain.model.ForgotPasswordDomainModel

interface IForgotPassRepository {
    suspend fun forgotPassResp(email: String): Result<ForgotPasswordDomainModel>
}
