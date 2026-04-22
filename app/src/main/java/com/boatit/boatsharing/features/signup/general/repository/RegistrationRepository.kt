package com.boatit.boatsharing.features.signup.general.repository

import com.boatit.boatsharing.data.network.di.ApiConstants
import com.boatit.boatsharing.data.network.retrofit.ApiExecutor
import com.boatit.boatsharing.features.signup.general.model.RegistrationRequest
import com.boatit.boatsharing.features.signup.general.model.RegistrationResponse

class RegistrationRepository(
    private val apiExecutor: ApiExecutor,
) : IRegistrationRepository {
    override suspend fun tempRegister(
        username: String,
        phoneNumber: String,
        email: String,
    ): Result<RegistrationResponse> =
        apiExecutor.post(
            endpoint = ApiConstants.Endpoints.ADD,
            body = RegistrationRequest(username, phoneNumber, email),
            successCode = 200,
        )
}
