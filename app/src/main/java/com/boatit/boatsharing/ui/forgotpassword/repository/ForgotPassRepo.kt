package com.boatit.boatsharing.ui.forgotpassword.repository

import com.boatit.boatsharing.data.remote.api.AccountApi
import com.boatit.boatsharing.data.remote.RemoteMapper
import com.boatit.boatsharing.ui.forgotpassword.model.ForgotPassRequest
import com.boatit.boatsharing.ui.forgotpassword.view.ForgotPassResponse

class ForgotPassRepository(private val api: AccountApi) {
    suspend fun forgotPassResp(email: String): Result<ForgotPassResponse> {
        return try {
            RemoteMapper.toResult(api.forgotPassword(ForgotPassRequest(email)))
        } catch (e: Exception) {
            Result.failure(Exception("Error registering: ${e.localizedMessage}", e))
        }
    }
}
