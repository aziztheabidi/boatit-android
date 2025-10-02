package com.boatit.boatsharing.ui.signup.general.viewmodel


import com.boatit.boatsharing.network.di.ApiConstants
import com.boatit.boatsharing.ui.login.model.LoginResponse
import com.boatit.boatsharing.ui.signup.general.model.PasswordRequest
import com.boatit.boatsharing.utils.AppConstants

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType


class PasswordRepository(private val httpClient: HttpClient) {
    suspend fun passwordRepository(password: String, token: String): Result<LoginResponse> {
        return try {
            AppConstants.JWT_TOKEN = token
            val response: HttpResponse = httpClient.post("${ApiConstants.BASE_URL}${ApiConstants.Endpoints.REGISTER}") {
                contentType(ContentType.Application.Json)
                setBody(PasswordRequest(password))
            }
            if (response.status == HttpStatusCode.Created) {
                AppConstants.JWT_TOKEN = null
                val registrationResponse: LoginResponse = response.body()
                Result.success(registrationResponse)
            } else {
                val registrationResponse: LoginResponse = response.body()
                Result.failure(Exception("Registration failed:${registrationResponse.message}"))
            }
        } catch (e: Exception) {
            println("Parsing Error: ${e.message}")
            Result.failure(Exception("Parsing Error: ${e.message}", e))
        }
    }
}

