package com.boatit.boatsharing.features.signup.business.repository

import com.boatit.boatsharing.data.network.di.ApiConstants
import com.boatit.boatsharing.features.signup.business.model.BusinessInfoResponse
import com.boatit.boatsharing.data.local.prefmanager.UserSessionStore
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode

class GetBusinessInfoRepository(
    private val httpClient: HttpClient,
    private val userSessionStore: UserSessionStore,
) {
    suspend fun GetBusinessInfo(): Result<BusinessInfoResponse> {
        return try {
            val userId = userSessionStore.currentUserId()
            val response: HttpResponse =
                httpClient.get("${ApiConstants.BASE_URL}${ApiConstants.Endpoints.GET_BUSINESS_INFO_PROFILE}") {
                    url {
                        parameters.append("UserId", userId)
                    }
                }
            if (response.status == HttpStatusCode.OK) {
                val result: BusinessInfoResponse = response.body()
                Result.success(result)
            } else {
                Result.failure(Exception("API Error: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network Error: ${e.localizedMessage}", e))
        }
    }
}
