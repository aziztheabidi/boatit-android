package com.boatit.boatsharing.ui.login.repository

import com.boatit.boatsharing.data.remote.api.AccountApi
import com.boatit.boatsharing.data.remote.RemoteMapper
import com.boatit.boatsharing.ui.login.model.LoginRequest
import com.boatit.boatsharing.ui.login.model.LoginResponse

class LoginRepository(private val api: AccountApi) {
    suspend fun login(username: String, password: String): Result<LoginResponse> {
        return try {
            RemoteMapper.toResult(api.login(LoginRequest(username, password)))
        } catch (e: Exception) {
            Result.failure(Exception("Error registering: ${e.localizedMessage}", e))
        }
    }
}
