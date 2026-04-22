package com.boatit.boatsharing.features.signup.general.repository

import com.boatit.boatsharing.features.login.model.LoginResponse

interface IPasswordRepository {
    suspend fun passwordRepository(
        password: String,
        token: String,
    ): Result<LoginResponse>
}
