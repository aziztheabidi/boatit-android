package com.boatit.boatsharing.ui.forgotpassword.repository

import com.boatit.boatsharing.network.di.ApiConstants
import com.boatit.boatsharing.ui.forgotpassword.model.ForgotPassRequest
import com.boatit.boatsharing.ui.forgotpassword.view.ForgotPassResponse
import com.boatit.boatsharing.ui.login.model.LoginResponse
import com.boatit.boatsharing.ui.login.model.LoginRequest
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class ForgotPassRepository(private val client: HttpClient) {
    suspend fun forgotPassResp(email: String): Result<ForgotPassResponse> {
        return try {
            val response: HttpResponse = client.post("${ApiConstants.BASE_URL}${ApiConstants.Endpoints.FORGOTPASS}") {
                contentType(ContentType.Application.Json)
                setBody(ForgotPassRequest(email))
            }
            if (response.status == HttpStatusCode.OK) {
                val result = response.body<ForgotPassResponse>()
                Result.success(result)
            } else {
                Result.failure(Exception("Registration failed: HTTP ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error registering: ${e.localizedMessage}", e))
        }
    }
}
