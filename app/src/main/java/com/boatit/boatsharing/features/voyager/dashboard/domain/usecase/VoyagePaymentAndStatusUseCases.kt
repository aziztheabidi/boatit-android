package com.boatit.boatsharing.features.voyager.dashboard.domain.usecase

import com.boatit.boatsharing.features.captain.dashboard.model.DeclineRequest
import com.boatit.boatsharing.features.voyager.dashboard.model.ActiveVoyageResponse
import com.boatit.boatsharing.features.voyager.dashboard.model.FutureBookedVoyages
import com.boatit.boatsharing.features.voyager.dashboard.model.PaymentConfirmationRequest
import com.boatit.boatsharing.features.voyager.dashboard.model.PaymentSheetConfigResponse
import com.boatit.boatsharing.features.voyager.dashboard.model.SponsorVoyagePaymentRequest
import com.boatit.boatsharing.features.voyager.dashboard.model.VoyagePaymentResponse

class ConfirmVoyagePaymentUseCase(
    private val confirmPayment: suspend (PaymentConfirmationRequest) -> Result<VoyagePaymentResponse>,
) {
    suspend operator fun invoke(request: PaymentConfirmationRequest): Result<VoyagePaymentResponse> {
        return confirmPayment(request)
    }
}

class ConfirmSponsorPaymentUseCase(
    private val confirmPayment: suspend (PaymentConfirmationRequest) -> Result<VoyagePaymentResponse>,
) {
    suspend operator fun invoke(request: PaymentConfirmationRequest): Result<VoyagePaymentResponse> {
        return confirmPayment(request)
    }
}

class FetchActiveVoyageUseCase(
    private val fetchActiveVoyage: suspend () -> Result<ActiveVoyageResponse>,
) {
    suspend operator fun invoke(): Result<ActiveVoyageResponse> {
        return fetchActiveVoyage()
    }
}

class FetchFutureVoyagesUseCase(
    private val fetchFutureVoyages: suspend () -> Result<FutureBookedVoyages>,
) {
    suspend operator fun invoke(): Result<FutureBookedVoyages> {
        return fetchFutureVoyages()
    }
}

class FetchPaymentSheetConfigUseCase(
    private val fetchPaymentSheetConfig: suspend (String) -> Result<PaymentSheetConfigResponse>,
) {
    suspend operator fun invoke(id: String): Result<PaymentSheetConfigResponse> {
        return fetchPaymentSheetConfig(id)
    }
}

class FetchSponsorPaymentSheetConfigUseCase(
    private val fetchSponsorPaymentSheetConfig: suspend (SponsorVoyagePaymentRequest) -> Result<PaymentSheetConfigResponse>,
) {
    suspend operator fun invoke(request: SponsorVoyagePaymentRequest): Result<PaymentSheetConfigResponse> {
        return fetchSponsorPaymentSheetConfig(request)
    }
}

class DeclineSponsorPaymentUseCase(
    private val declineSponsorPayment: suspend (DeclineRequest) -> Result<PaymentSheetConfigResponse>,
) {
    suspend operator fun invoke(request: DeclineRequest): Result<PaymentSheetConfigResponse> {
        return declineSponsorPayment(request)
    }
}
