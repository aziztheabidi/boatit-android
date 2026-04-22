package com.boatit.boatsharing.features.signup.captain.repository

import com.boatit.boatsharing.data.network.di.ApiConstants
import com.boatit.boatsharing.features.signup.captain.model.GetCaptainDocumentResponse
import com.boatit.boatsharing.data.local.prefmanager.UserSessionStore
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode

class GetCaptainDocsRepository(
    private val httpClient: HttpClient,
    private val userSessionStore: UserSessionStore,
) {
    suspend fun GetCaptainDocs(): Result<GetCaptainDocumentResponse> {
        return try {
            val userId = userSessionStore.currentUserId()
            val response: HttpResponse =
                httpClient.get("${ApiConstants.BASE_URL}${ApiConstants.Endpoints.GET_CAPTAIN_DOCS}") {
                    url {
                        parameters.append("UserId", userId)
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
