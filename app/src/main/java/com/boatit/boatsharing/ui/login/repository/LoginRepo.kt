package com.boatit.boatsharing.ui.login.repository

import android.util.Log
import com.boatit.boatsharing.network.di.ApiConstants
import com.boatit.boatsharing.ui.login.model.LoginResponse
import com.boatit.boatsharing.ui.login.model.LoginRequest
import com.boatit.boatsharing.utils.prefmanager.SharedPrefManager
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

/**
 * Simple error response model for login failures
 * Matches the actual server response structure
 */
@Serializable
data class LoginErrorResponse(
    @SerialName("Status")
    val status: Int,
    @SerialName("Message")
    val message: String,
    @SerialName("obj")
    val obj: LoginErrorObj? = null
)

@Serializable
data class LoginErrorObj(
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

class LoginRepository(private val client: HttpClient) {
    suspend fun login(username: String, password: String): Result<LoginResponse> {
        Log.d("LoginRepository", "Starting login request for username: $username")
        Log.d("LoginRepository", "Login URL: ${ApiConstants.BASE_URL}${ApiConstants.Endpoints.LOGIN}")
        
        return try {
            val loginRequest = LoginRequest(username, password)
            Log.d("LoginRepository", "Login request body: $loginRequest")
            
            val response: HttpResponse = client.post("${ApiConstants.BASE_URL}${ApiConstants.Endpoints.LOGIN}") {
                contentType(ContentType.Application.Json)
                setBody(loginRequest)
            }
            
            Log.d("LoginRepository", "Response status: ${response.status}")
            Log.d("LoginRepository", "Response headers: ${response.headers}")
            
            if (response.status == HttpStatusCode.OK) {
                val result = response.body<LoginResponse>()
                Log.d("LoginRepository", "Login successful: $result")
                Result.success(result)
            } else {
                // For error responses, try to parse as LoginErrorResponse first
                Log.e("LoginRepository", "Login failed with status ${response.status}")
                
                try {
                    val errorResult = response.body<LoginErrorResponse>()
                    Log.e("LoginRepository", "Parsed error response: $errorResult")
                    Result.failure(Exception(errorResult.message))
                } catch (e: Exception) {
                    Log.e("LoginRepository", "Could not parse error response as LoginErrorResponse: ${e.message}")
                    // If we can't parse as LoginErrorResponse, try LoginResponse
                    try {
                        val loginErrorResult = response.body<LoginResponse>()
                        Log.e("LoginRepository", "Parsed as LoginResponse: $loginErrorResult")
                        Result.failure(Exception(loginErrorResult.message))
                    } catch (loginException: Exception) {
                        Log.e("LoginRepository", "Could not parse error response as LoginResponse: ${loginException.message}")
                        // If all else fails, try to get raw text
                        try {
                            val responseText = response.body<String>()
                            Log.e("LoginRepository", "Raw response body: $responseText")
                            Result.failure(Exception("Login failed with status ${response.status}: $responseText"))
                        } catch (textException: Exception) {
                            Log.e("LoginRepository", "Could not get response body as text: ${textException.message}")
                            Result.failure(Exception("Login failed with status ${response.status}"))
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("LoginRepository", "Login exception: ${e.message}", e)
            Result.failure(Exception("Error registering: ${e.localizedMessage}", e))
        }
    }
}
