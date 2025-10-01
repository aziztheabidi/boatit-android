package com.boatit.boatsharing.ui.voyager.dashboard.repository


import android.util.Log
import com.boatit.boatsharing.network.di.ApiConstants
import com.boatit.boatsharing.network.di.ApiError
import com.boatit.boatsharing.ui.signup.captain.model.GetCaptainProfileResponse
import com.boatit.boatsharing.ui.voyager.dashboard.model.CalculateFair
import com.boatit.boatsharing.utils.AppConstants
import com.google.gson.Gson
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode

class CalculateFairRepository(private val httpClient: HttpClient) {
    suspend fun CalculateFairRepoFunc(DurationInHours:String): Result<CalculateFair> {
        return try {
            val userId = AppConstants.USER_ID
            val response: HttpResponse = httpClient.get("${ApiConstants.BASE_URL}${ApiConstants.Endpoints.CALCULATE_FAIR}") {
                url {
                    parameters.append("FromDockId", AppConstants.Pick_Up_Loc?.first.toString())
                    parameters.append("ToDockId", AppConstants.Drop_Off_Loc?.first.toString())
                    parameters.append("VoyageCategoryId", AppConstants.Cat_id.toString())
                    parameters.append("DurationInHours", DurationInHours)
                    parameters.append("NoOfVoyagers", AppConstants.No_Of_Voyagers.toString())
                }
            }
            if (response.status == HttpStatusCode.Created) {
                val result: CalculateFair = response.body()
                Result.success(result)
            } else {
                Log.e("issue",response.body())

                val bodyString = response.body() ?: ""
                val apiError = Gson().fromJson(bodyString, ApiError::class.java)

                Result.failure(Exception(apiError.Message))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network Error: ${e.localizedMessage}", e))
        }
    }
}

