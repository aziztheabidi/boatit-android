package com.boatit.boatsharing.ui.signup.general.repository


import com.boatit.boatsharing.data.remote.api.RegistrationTempApi
import com.boatit.boatsharing.data.remote.RemoteMapper
import com.boatit.boatsharing.ui.signup.general.model.RegistrationRequest
import com.boatit.boatsharing.ui.signup.general.model.RegistrationResponse


class RegistrationRepository(private val api: RegistrationTempApi) {
    suspend fun tempRegister(username: String, phoneNumber: String, email: String): Result<RegistrationResponse> {
        return try {
            RemoteMapper.toResult(api.addRegistration(RegistrationRequest(username, phoneNumber, email)))
        } catch (e: Exception) {
            Result.failure(Exception("Error registering: ${e.localizedMessage}", e))
        }
    }
}
