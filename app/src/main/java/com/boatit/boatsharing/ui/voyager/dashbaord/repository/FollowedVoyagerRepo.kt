package com.boatit.boatsharing.ui.voyager.dashbaord.repository


import com.boatit.boatsharing.network.di.ApiConstants
import com.boatit.boatsharing.ui.voyager.dashbaord.model.FollowedVoyagersResponse
import com.boatit.boatsharing.utils.AppConstants
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode

class FollowedVoyagerRepository(private val httpClient: HttpClient) {
    suspend fun FollowedVoyagerRepoFunc(): Result<FollowedVoyagersResponse> {
        return try {
            val userId = AppConstants.USER_ID
            val response: HttpResponse = httpClient.get("${ApiConstants.BASE_URL}${ApiConstants.Endpoints.FOLLOWED_VOYAGER}") {
                url {
                    parameters.append("UserId", userId.toString())
                }
            }
            if (response.status == HttpStatusCode.OK) {
                val result: FollowedVoyagersResponse = response.body()
                Result.success(result)
            } else {
                Result.failure(Exception("API Error: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network Error: ${e.localizedMessage}", e))
        }
    }
}

