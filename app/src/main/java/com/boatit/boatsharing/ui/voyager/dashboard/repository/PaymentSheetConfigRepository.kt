package com.boatit.boatsharing.ui.voyager.dashboard.repository

import com.boatit.boatsharing.data.remote.RemoteMapper
import com.boatit.boatsharing.data.remote.api.VoyageApi
import com.boatit.boatsharing.ui.voyager.dashboard.model.PaymentSheetConfigResponse
import com.boatit.boatsharing.ui.voyager.dashboard.model.VoyagePaymentRequest

class PaymentSheetConfigRepository(private val api: VoyageApi) {
    suspend fun SheetConfi(profile: VoyagePaymentRequest): Result<PaymentSheetConfigResponse> {
        return try {
            RemoteMapper.toResult(api.paymentSheetConfig(profile))
        } catch (e: Exception) {
            Result.failure(Exception("Network Error: ${e.localizedMessage}", e))
        }
    }
}
