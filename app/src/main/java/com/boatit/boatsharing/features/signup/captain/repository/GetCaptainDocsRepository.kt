package com.boatit.boatsharing.features.signup.captain.repository

import com.boatit.boatsharing.data.network.di.ApiConstants
import com.boatit.boatsharing.data.network.di.executeGetRequest
import com.boatit.boatsharing.features.signup.captain.model.GetCaptainDocumentResponse
import com.boatit.boatsharing.data.local.prefmanager.UserSessionStore
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.http.HttpStatusCode

class GetCaptainDocsRepository(
    private val httpClient: HttpClient,
    private val userSessionStore: UserSessionStore,
) {
    suspend fun GetCaptainDocs(): Result<GetCaptainDocumentResponse> {
        val userId = userSessionStore.currentUserId()
        return executeGetRequest(
            httpClient = httpClient,
            url = "${ApiConstants.BASE_URL}${ApiConstants.Endpoints.GET_CAPTAIN_DOCS}",
            requestConfig = {
                url {
                    parameters.append("UserId", userId)
                }
            },
            handleResponse = { response ->
                if (response.status == HttpStatusCode.OK) {
                    val result: GetCaptainDocumentResponse = response.body()
                    Result.success(result)
                } else {
                    Result.failure(Exception("API Error: ${response.status}"))
                }
            },
            onException = { e ->
                Result.failure(Exception("Network Error: ${e.localizedMessage}", e))
            },
        )
    }
}
