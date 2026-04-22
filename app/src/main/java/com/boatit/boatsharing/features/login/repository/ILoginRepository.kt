package com.boatit.boatsharing.features.login.repository

import com.boatit.boatsharing.features.login.model.LoginResponse

interface ILoginRepository {
    suspend fun login(
        username: String,
        password: String,
    ): Result<LoginResponse>
}
