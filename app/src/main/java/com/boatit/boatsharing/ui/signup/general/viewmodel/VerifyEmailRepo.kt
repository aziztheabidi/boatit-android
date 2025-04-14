package com.boatit.boatsharing.ui.signup.general.viewmodel


import com.boatit.boatsharing.network.di.ApiConstants
import com.boatit.boatsharing.ui.signup.general.model.RegistrationRequest
import com.boatit.boatsharing.ui.signup.general.model.RegistrationResponse
import com.boatit.boatsharing.ui.signup.general.model.VerifyEmailRequest
import com.boatit.boatsharing.ui.signup.general.model.VerifyEmailResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class VerifyEmailRepository(private val httpClient: HttpClient) {
    suspend fun verifyEmail(email: String, otp: String): Result<VerifyEmailResponse> {
        return try {
            val response: HttpResponse = httpClient.post("${ApiConstants.BASE_URL}${ApiConstants.Endpoints.VERIFY}") {
                contentType(ContentType.Application.Json)
                setBody(VerifyEmailRequest(email, otp))
            }
            if (response.status == HttpStatusCode.OK) {
                val registrationResponse: VerifyEmailResponse = response.body()
                Result.success(registrationResponse)
            } else {
                Result.failure(Exception("Registration failed: HTTP ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error registering: ${e.localizedMessage}", e))
        }
    }
}

