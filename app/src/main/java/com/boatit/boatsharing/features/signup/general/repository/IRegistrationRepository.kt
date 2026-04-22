package com.boatit.boatsharing.features.signup.general.repository

import com.boatit.boatsharing.features.signup.general.model.RegistrationResponse

interface IRegistrationRepository {
    suspend fun tempRegister(
        username: String,
        phoneNumber: String,
        email: String,
    ): Result<RegistrationResponse>
}
