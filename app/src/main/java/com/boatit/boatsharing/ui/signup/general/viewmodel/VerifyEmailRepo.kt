package com.boatit.boatsharing.ui.signup.general.viewmodel


import com.boatit.boatsharing.data.remote.api.RegistrationTempApi
import com.boatit.boatsharing.data.remote.RemoteMapper
import com.boatit.boatsharing.ui.signup.general.model.VerifyEmailRequest
import com.boatit.boatsharing.ui.signup.general.model.VerifyEmailResponse

class VerifyEmailRepository(private val api: RegistrationTempApi) {
    suspend fun verifyEmail(email: String, otp: String): Result<VerifyEmailResponse> {
        return try {
            RemoteMapper.toResult(api.verifyEmail(VerifyEmailRequest(email, otp)))
        } catch (e: Exception) {
            Result.failure(Exception("Error registering: ${e.localizedMessage}", e))
        }
    }
}
