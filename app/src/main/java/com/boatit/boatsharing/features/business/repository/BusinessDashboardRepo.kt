package com.boatit.boatsharing.features.business.repository

import com.boatit.boatsharing.data.network.di.ApiConstants
import com.boatit.boatsharing.data.network.di.executePostRequest
import com.boatit.boatsharing.features.business.model.BusinessRequest
import com.boatit.boatsharing.features.business.model.DeleteRequest
import com.boatit.boatsharing.features.signup.business.model.SaveBusinessInfoResponse
import io.ktor.client.HttpClient
import io.ktor.http.HttpStatusCode

class BusinessDashboardRepository(private val httpClient: HttpClient) {
    suspend fun BusinessInfo(profile: BusinessRequest): Result<SaveBusinessInfoResponse> {
        return executePostRequest(
            httpClient = httpClient,
            url = "${ApiConstants.BASE_URL}${ApiConstants.Endpoints.SAVE_BUSINESS_DASH_PROFILE}",
            requestBody = profile,
            successStatus = HttpStatusCode.OK,
            onApiError = { _, status -> Exception("$status") },
            onException = { e -> Result.failure(Exception("${e.localizedMessage}", e)) },
        )
    }

    suspend fun Delete(profile: DeleteRequest): Result<SaveBusinessInfoResponse> {
        return executePostRequest(
            httpClient = httpClient,
            url = "${ApiConstants.BASE_URL}${ApiConstants.Endpoints.DELETE_BUSINESS_DASH_PROFILE}",
            requestBody = profile,
            successStatus = HttpStatusCode.OK,
            onApiError = { _, status -> Exception("$status") },
            onException = { e -> Result.failure(Exception("${e.localizedMessage}", e)) },
        )
    }
}
