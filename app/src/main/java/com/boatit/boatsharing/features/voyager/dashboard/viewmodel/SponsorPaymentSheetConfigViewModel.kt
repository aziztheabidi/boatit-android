package com.boatit.boatsharing.features.voyager.dashboard.viewmodel

import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.core.presentation.BaseViewModel
import com.boatit.boatsharing.core.presentation.UiEffect
import com.boatit.boatsharing.core.presentation.UiEvent
import com.boatit.boatsharing.core.presentation.UiState
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.features.captain.dashboard.model.DeclineRequest
import com.boatit.boatsharing.features.voyager.dashboard.domain.usecase.DeclineSponsorPaymentUseCase
import com.boatit.boatsharing.features.voyager.dashboard.domain.usecase.FetchSponsorPaymentSheetConfigUseCase
import com.boatit.boatsharing.features.voyager.dashboard.model.PaymentSheetConfigResponse
import com.boatit.boatsharing.features.voyager.dashboard.model.SponsorVoyagePaymentRequest
import kotlinx.coroutines.launch

data class SponsorPaymentSheetConfigUiState(
    val paymentSheetConfigState: NetworkResponse<PaymentSheetConfigResponse> = NetworkResponse.Loading(),
    val declinePaymentState: NetworkResponse<PaymentSheetConfigResponse> = NetworkResponse.Loading(),
) : UiState

sealed interface SponsorPaymentSheetConfigUiEvent : UiEvent {
    data class LoadPaymentSheetConfig(val request: SponsorVoyagePaymentRequest) : SponsorPaymentSheetConfigUiEvent

    data class DeclineSponsorPayment(val request: DeclineRequest) : SponsorPaymentSheetConfigUiEvent

    data object Reset : SponsorPaymentSheetConfigUiEvent
}

sealed interface SponsorPaymentSheetConfigUiEffect : UiEffect {
    data object NoOpEffect : SponsorPaymentSheetConfigUiEffect
}

class SponsorPaymentSheetConfigViewModel(
    private val fetchSponsorPaymentSheetConfigUseCase: FetchSponsorPaymentSheetConfigUseCase,
    private val declineSponsorPaymentUseCase: DeclineSponsorPaymentUseCase,
) : BaseViewModel<SponsorPaymentSheetConfigUiState, SponsorPaymentSheetConfigUiEvent, SponsorPaymentSheetConfigUiEffect>(
        SponsorPaymentSheetConfigUiState(),
    ) {
    override fun onEvent(event: SponsorPaymentSheetConfigUiEvent) {
        when (event) {
            is SponsorPaymentSheetConfigUiEvent.LoadPaymentSheetConfig -> loadPaymentSheetConfig(event.request)
            is SponsorPaymentSheetConfigUiEvent.DeclineSponsorPayment -> declineSponsorPayment(event.request)
            SponsorPaymentSheetConfigUiEvent.Reset -> resetPaymentSheetState()
        }
    }

    fun loadPaymentSheetConfig(request: SponsorVoyagePaymentRequest) {
        viewModelScope.launch {
            updateState { copy(paymentSheetConfigState = NetworkResponse.Loading()) }
            when (val result = fetchSponsorPaymentSheetConfigUseCase(request).toResource()) {
                is Resource.Success -> {
                    updateState { copy(paymentSheetConfigState = NetworkResponse.Success(result.data)) }
                }

                is Resource.Error -> {
                    updateState { copy(paymentSheetConfigState = NetworkResponse.Error(result.error)) }
                }

                Resource.Loading -> {
                    updateState { copy(paymentSheetConfigState = NetworkResponse.Loading()) }
                }
            }
        }
    }

    @Deprecated("Use loadPaymentSheetConfig")
    fun paymentConfig(id: SponsorVoyagePaymentRequest) {
        loadPaymentSheetConfig(id)
    }

    fun declineSponsorPayment(request: DeclineRequest) {
        viewModelScope.launch {
            updateState { copy(declinePaymentState = NetworkResponse.Loading()) }
            when (val result = declineSponsorPaymentUseCase(request).toResource()) {
                is Resource.Success -> {
                    updateState { copy(declinePaymentState = NetworkResponse.Success(result.data)) }
                }

                is Resource.Error -> {
                    updateState { copy(declinePaymentState = NetworkResponse.Error(result.error)) }
                }

                Resource.Loading -> {
                    updateState { copy(declinePaymentState = NetworkResponse.Loading()) }
                }
            }
        }
    }

    @Deprecated("Use declineSponsorPayment")
    fun paymentDecline(id: DeclineRequest) {
        declineSponsorPayment(id)
    }

    fun resetPaymentSheetState() {
        updateState {
            copy(
                paymentSheetConfigState = NetworkResponse.Loading(),
                declinePaymentState = NetworkResponse.Loading(),
            )
        }
    }

    @Deprecated("Use resetPaymentSheetState")
    fun resetNearbyPlaces() {
        resetPaymentSheetState()
    }
}
