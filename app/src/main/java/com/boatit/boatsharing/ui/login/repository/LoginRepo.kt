package com.boatit.boatsharing.ui.login.repository

import com.boatit.boatsharing.network.di.ApiConstants
import com.boatit.boatsharing.network.di.invalidateTokens
import com.boatit.boatsharing.ui.login.model.LoginResponse
import com.boatit.boatsharing.ui.login.model.LoginRequest
import com.boatit.boatsharing.utils.prefmanager.SharedPrefManager
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class LoginRepository(private val client: HttpClient) {
    suspend fun login(username: String, password: String): Result<LoginResponse> {
        return try {

            val response: HttpResponse = client.post("${ApiConstants.BASE_URL}${ApiConstants.Endpoints.LOGIN}") {
                contentType(ContentType.Application.Json)
                setBody(LoginRequest(username, password))
            }
            if (response.status == HttpStatusCode.OK) {
                val result = response.body<LoginResponse>()
                client.invalidateTokens()
                Result.success(result)
            } else {
                val result = response.body<LoginResponse>()
                Result.failure(Exception(result.Message))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error registering: ${e.localizedMessage}", e))
        }
    }
}
