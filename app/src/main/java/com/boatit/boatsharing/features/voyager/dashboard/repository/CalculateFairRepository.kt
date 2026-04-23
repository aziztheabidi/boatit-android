@file:Suppress("ktlint:standard:filename")

package com.boatit.boatsharing.features.voyager.dashboard.repository

import android.util.Log
import com.boatit.boatsharing.data.network.di.ApiConstants
import com.boatit.boatsharing.data.network.di.ApiError
import com.boatit.boatsharing.data.network.di.executeGetRequest
import com.boatit.boatsharing.features.voyager.dashboard.model.CalculateFair
import com.google.gson.Gson
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.http.HttpStatusCode

class CalculateFairRepository(private val httpClient: HttpClient) {
    suspend fun calculateFair(
        durationInHours: String,
        fromDockId: Int,
        toDockId: Int,
        voyageCategoryId: Int,
        noOfVoyagers: Int,
    ): Result<CalculateFair> {
        return executeGetRequest(
            httpClient = httpClient,
            url = "${ApiConstants.BASE_URL}${ApiConstants.Endpoints.CALCULATE_FAIR}",
            requestConfig = {
                url {
                    parameters.append("FromDockId", fromDockId.toString())
                    parameters.append("ToDockId", toDockId.toString())
                    parameters.append("VoyageCategoryId", voyageCategoryId.toString())
                    parameters.append("DurationInHours", durationInHours)
                    parameters.append("NoOfVoyagers", noOfVoyagers.toString())
                }
            },
            handleResponse = { response ->
                if (response.status == HttpStatusCode.Created) {
                    val result: CalculateFair = response.body()
                    Result.success(result)
                } else {
                    Log.e("issue", response.body())

                    val bodyString = response.body() ?: ""
                    val apiError = Gson().fromJson(bodyString, ApiError::class.java)

                    Result.failure(Exception(apiError.Message))
                }
            },
            onException = { e ->
                Result.failure(Exception("Network Error: ${e.localizedMessage}", e))
            },
        )
    }
}
