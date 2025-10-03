package com.boatit.boatsharing.ui.signup.general.viewmodel


import android.util.Log
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
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

/**
 * Simple error response model for registration failures
 * Matches the actual server response structure
 */
@Serializable
data class RegistrationErrorResponse(
    @SerialName("Status")
    val status: Int,
    @SerialName("Message")
    val message: String,
    @SerialName("obj")
    val obj: RegistrationErrorObj? = null
)

@Serializable
data class RegistrationErrorObj(
    @SerialName("Email")
    val email: String = "",
    @SerialName("Password")
    val password: String = "",
    @SerialName("UserId")
    val userId: String = "",
    @SerialName("Username")
    val username: String = "",
    @SerialName("Role")
    val role: String = "",
    @SerialName("MissingStep")
    val missingStep: Int = 0,
    @SerialName("Accesstoken")
    val accesstoken: String = "",
    @SerialName("Refreshtoken")
    val refreshtoken: String = ""
)

class PasswordRepository(private val httpClient: HttpClient) {
    suspend fun passwordRepository(password: String, token: String): Result<LoginResponse> {
        Log.d("PasswordRepository", "Starting registration request with token: ${if (token.isNotEmpty()) "Present" else "Empty"}")
        Log.d("PasswordRepository", "Registration URL: ${ApiConstants.BASE_URL}${ApiConstants.Endpoints.REGISTER}")
        
        return try {
            AppConstants.JWT_TOKEN = token
            val passwordRequest = PasswordRequest(password)
            Log.d("PasswordRepository", "Registration request body: $passwordRequest")
            
            val response: HttpResponse = httpClient.post("${ApiConstants.BASE_URL}${ApiConstants.Endpoints.REGISTER}") {
                contentType(ContentType.Application.Json)
                setBody(passwordRequest)
            }
            
            Log.d("PasswordRepository", "Response status: ${response.status}")
            Log.d("PasswordRepository", "Response headers: ${response.headers}")
            
            if (response.status == HttpStatusCode.Created) {
                AppConstants.JWT_TOKEN = null
                val registrationResponse: LoginResponse = response.body()
                Log.d("PasswordRepository", "Registration successful: $registrationResponse")
                Result.success(registrationResponse)
            } else {
                // For error responses, try to parse as RegistrationErrorResponse first
                Log.e("PasswordRepository", "Registration failed with status ${response.status}")
                
                try {
                    val errorResult = response.body<RegistrationErrorResponse>()
                    Log.e("PasswordRepository", "Parsed error response: $errorResult")
                    Result.failure(Exception(errorResult.message))
                } catch (e: Exception) {
                    Log.e("PasswordRepository", "Could not parse error response as RegistrationErrorResponse: ${e.message}")
                    // If we can't parse as RegistrationErrorResponse, try LoginResponse
                    try {
                        val loginErrorResult = response.body<LoginResponse>()
                        Log.e("PasswordRepository", "Parsed as LoginResponse: $loginErrorResult")
                        Result.failure(Exception(loginErrorResult.message))
                    } catch (loginException: Exception) {
                        Log.e("PasswordRepository", "Could not parse error response as LoginResponse: ${loginException.message}")
                        // If all else fails, try to get raw text
                        try {
                            val responseText = response.body<String>()
                            Log.e("PasswordRepository", "Raw response body: '$responseText'")
                            if (responseText.isBlank()) {
                                // Handle empty response body (common with 401 Unauthorized)
                                Result.failure(Exception("Registration failed: Invalid or expired token (401 Unauthorized)"))
                            } else {
                                Result.failure(Exception("Registration failed with status ${response.status}: $responseText"))
                            }
                        } catch (textException: Exception) {
                            Log.e("PasswordRepository", "Could not get response body as text: ${textException.message}")
                            // Handle case where we can't read response body at all
                            Result.failure(Exception("Registration failed: Invalid or expired token (401 Unauthorized)"))
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("PasswordRepository", "Registration exception: ${e.message}", e)
            Result.failure(Exception("Error registering: ${e.localizedMessage}", e))
        }
    }
}

