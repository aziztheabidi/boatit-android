package com.boatit.boatsharing.ui.business.repository

import android.util.Log
import com.boatit.boatsharing.network.di.ApiConstants
import com.boatit.boatsharing.ui.business.model.DocksDropdownResponse
import com.boatit.boatsharing.ui.business.model.GetBusinessResponse
import com.boatit.boatsharing.ui.captain.availablitystatus.model.CaptainAvailabilityRequest
import com.boatit.boatsharing.ui.captain.availablitystatus.model.CaptainAvailabilityResponse
import com.boatit.boatsharing.ui.captain.dashbaord.model.AcceptVoyageRequest
import com.boatit.boatsharing.ui.captain.dashbaord.model.AcceptVoyageResponse
import com.boatit.boatsharing.ui.captain.dashbaord.model.VoyageStartRequest
import com.boatit.boatsharing.ui.captain.dashbaord.model.VoyageStartResponse
import com.boatit.boatsharing.ui.captain.voyages.model.CaptainVoyages
import com.boatit.boatsharing.ui.captain.voyages.model.CaptainVoyagesResponse
import com.boatit.boatsharing.ui.login.model.LoginResponse
import com.boatit.boatsharing.ui.login.model.LoginRequest
import com.boatit.boatsharing.ui.signup.captain.model.SaveCaptainBoatRequest
import com.boatit.boatsharing.ui.signup.captain.model.SaveCaptainBoatResponse
import com.boatit.boatsharing.ui.voyager.dashbaord.model.ActiveVoyageResponse
import com.boatit.boatsharing.ui.voyager.dashbaord.model.VoyagerVoyagesResponse
import com.boatit.boatsharing.utils.AppConstants
import com.boatit.boatsharing.utils.prefmanager.SharedPrefManager
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class GetBusinessDocksRepo(private val httpClient: HttpClient) {
    suspend fun voyages(): Result<DocksDropdownResponse> {
        return try {
            val response: HttpResponse = httpClient.get("${ApiConstants.BASE_URL}${ApiConstants.Endpoints.Zones}")
            if (response.status == HttpStatusCode.OK) {
                val result: DocksDropdownResponse = response.body()

                Result.success(result)
            } else {
                val result: DocksDropdownResponse = response.body()
                Result.failure(Exception("API Error: ${result.Message}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network Error: ${e.localizedMessage}", e))
        }
    }
}
