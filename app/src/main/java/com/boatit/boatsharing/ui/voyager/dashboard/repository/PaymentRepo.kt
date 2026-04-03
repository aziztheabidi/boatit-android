package com.boatit.boatsharing.ui.voyager.dashboard.repository

import com.boatit.boatsharing.data.remote.api.VoyageApi
import com.boatit.boatsharing.data.remote.RemoteMapper
import com.boatit.boatsharing.ui.voyager.dashboard.model.VoyagePaymentRequest
import com.boatit.boatsharing.ui.voyager.dashboard.model.VoyagePaymentResponse

class PaymentRepository(private val api: VoyageApi) {
    suspend fun payment(profile: VoyagePaymentRequest): Result<VoyagePaymentResponse> {
        return try {
            RemoteMapper.toResult(api.payment(profile))
        } catch (e: Exception) {
            Result.failure(Exception("Network Error: ${e.localizedMessage}", e))
        }
    }
}
