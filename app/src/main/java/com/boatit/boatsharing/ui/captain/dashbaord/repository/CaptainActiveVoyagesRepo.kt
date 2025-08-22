package com.boatit.boatsharing.ui.captain.dashbaord.repository

import CaptainActiveVoyagesResponse
import android.util.Log
import com.boatit.boatsharing.network.di.ApiConstants
import com.boatit.boatsharing.utils.AppConstants
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class CaptainActiveVoyagesRepository(private val httpClient: HttpClient) {
    suspend fun voyages(): Result<CaptainActiveVoyagesResponse> {
        return try {
            val userId = AppConstants.USER_ID
            val response: HttpResponse = httpClient.get("${ApiConstants.BASE_URL}${ApiConstants.Endpoints.GET_CAPTAIN_ACTIVE_VOYAGES}")
            if (response.status == HttpStatusCode.OK) {
                Log.e("captain_voyages_Repo", response.body())
                val result: CaptainActiveVoyagesResponse = response.body()
                Result.success(result)

            } else {
                val result: CaptainActiveVoyagesResponse = response.body()
                Result.failure(Exception("API Error: ${result.Message}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network Error: ${e.localizedMessage}", e))
        }
    }
}
