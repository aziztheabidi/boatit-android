package com.boatit.boatsharing.ui.voyager.dashbaord.repository


import com.boatit.boatsharing.network.di.ApiConstants
import com.boatit.boatsharing.ui.signup.captain.model.GetCaptainProfileResponse
import com.boatit.boatsharing.ui.voyager.dashbaord.model.CalculateFair
import com.boatit.boatsharing.utils.AppConstants
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode

class CalculateFairRepository(private val httpClient: HttpClient) {
    suspend fun CalculateFairRepoFunc(FromDockId: String,ToDockId:String,DurationInHours:String): Result<CalculateFair> {
        return try {
            val userId = AppConstants.USER_ID
            val response: HttpResponse = httpClient.get("${ApiConstants.BASE_URL}${ApiConstants.Endpoints.CALCULATE_FAIR}") {
                url {
                    parameters.append("FromDockId", FromDockId)
                    parameters.append("ToDockId", ToDockId)
                    parameters.append("VoyageCategoryId", AppConstants.Cat_id.toString())
                    parameters.append("DurationInHours", DurationInHours)
                    parameters.append("NoOfVoyagers", AppConstants.No_Of_Voyagers.toString())
                }
            }
            if (response.status == HttpStatusCode.Created) {
                val result: CalculateFair = response.body()
                Result.success(result)
            } else {
                Result.failure(Exception("API Error: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network Error: ${e.localizedMessage}", e))
        }
    }
}

