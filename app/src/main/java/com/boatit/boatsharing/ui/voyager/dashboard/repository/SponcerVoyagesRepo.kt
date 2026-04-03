package com.boatit.boatsharing.ui.voyager.dashboard.repository

import com.boatit.boatsharing.network.di.ApiConstants
import com.boatit.boatsharing.ui.captain.availabilitystatus.model.CaptainAvailabilityRequest
import com.boatit.boatsharing.ui.captain.availabilitystatus.model.CaptainAvailabilityResponse
import com.boatit.boatsharing.ui.captain.dashboard.model.AcceptVoyageRequest
import com.boatit.boatsharing.ui.captain.dashboard.model.AcceptVoyageResponse
import com.boatit.boatsharing.ui.captain.dashboard.model.VoyageStartRequest
import com.boatit.boatsharing.ui.captain.dashboard.model.VoyageStartResponse
import com.boatit.boatsharing.ui.captain.voyages.model.CaptainVoyages
import com.boatit.boatsharing.ui.captain.voyages.model.CaptainVoyagesResponse
import com.boatit.boatsharing.ui.login.model.LoginResponse
import com.boatit.boatsharing.ui.login.model.LoginRequest
import com.boatit.boatsharing.ui.signup.captain.model.SaveCaptainBoatRequest
import com.boatit.boatsharing.ui.signup.captain.model.SaveCaptainBoatResponse
import com.boatit.boatsharing.ui.voyager.dashboard.model.SponsorPayments
import com.boatit.boatsharing.ui.voyager.dashboard.model.VoyagerVoyagesResponse
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

class SponcerVoyagesRepo(private val httpClient: HttpClient) {
    suspend fun voyages(): Result<SponsorPayments> {
        return try {
            val userId = AppConstants.USER_ID
            val response: HttpResponse = httpClient.get("${ApiConstants.BASE_URL}${ApiConstants.Endpoints.GET_SPONSOR_PAYMENTS}") {
                url {
                    parameters.append("UserId", userId.toString())
                }
            }
            if (response.status == HttpStatusCode.OK) {
                val result: SponsorPayments = response.body()
                Result.success(result)
            } else {
                val result: SponsorPayments = response.body()
                Result.failure(Exception("API Error: ${result.Message}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network Error: ${e.localizedMessage}", e))
        }
    }
}
