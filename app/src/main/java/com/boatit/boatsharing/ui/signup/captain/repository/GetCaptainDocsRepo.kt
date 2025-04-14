package com.boatit.boatsharing.ui.signup.captain.repository

import com.boatit.boatsharing.network.di.ApiConstants
import com.boatit.boatsharing.ui.signup.captain.model.GetCaptainBoatResponse
import com.boatit.boatsharing.ui.signup.captain.model.GetCaptainDocumentResponse
import com.boatit.boatsharing.ui.signup.captain.model.SaveCaptainBoatRequest
import com.boatit.boatsharing.ui.signup.captain.model.SaveCaptainBoatResponse
import com.boatit.boatsharing.utils.AppConstants
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class GetCaptainDocsRepository(private val httpClient: HttpClient) {
    suspend fun GetCaptainDocs(): Result<GetCaptainDocumentResponse> {
        return try {
            val userId = AppConstants.USER_ID
            val response: HttpResponse = httpClient.get("${ApiConstants.BASE_URL}${ApiConstants.Endpoints.GET_CAPTAIN_DOCS}") {
                url {
                    parameters.append("UserId", userId.toString())
                }
            }
            if (response.status == HttpStatusCode.OK) {
                val result: GetCaptainDocumentResponse = response.body()
                Result.success(result)
            } else {
                Result.failure(Exception("API Error: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network Error: ${e.localizedMessage}", e))
        }
    }
}

