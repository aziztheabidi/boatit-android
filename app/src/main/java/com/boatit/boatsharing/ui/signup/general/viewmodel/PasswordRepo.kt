package com.boatit.boatsharing.ui.signup.general.viewmodel


import com.boatit.boatsharing.data.remote.api.AccountApi
import com.boatit.boatsharing.data.remote.RemoteMapper
import com.boatit.boatsharing.ui.login.model.LoginResponse
import com.boatit.boatsharing.ui.signup.general.model.PasswordRequest
import com.boatit.boatsharing.utils.AppConstants


class PasswordRepository(private val api: AccountApi) {
    suspend fun passwordRepository(password: String, token: String): Result<LoginResponse> {
        return try {
            AppConstants.JWT_TOKEN = token
            val result = RemoteMapper.toResult(api.register(PasswordRequest(password)))
            AppConstants.JWT_TOKEN = null
            result
        } catch (e: Exception) {
            AppConstants.JWT_TOKEN = null
            Result.failure(Exception("Parsing Error: ${e.message}", e))
        }
    }
}
