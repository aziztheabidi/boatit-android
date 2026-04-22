package com.boatit.boatsharing.features.voyager.dashboard.viewmodel

import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.core.presentation.BaseViewModel
import com.boatit.boatsharing.core.presentation.UiEffect
import com.boatit.boatsharing.core.presentation.UiEvent
import com.boatit.boatsharing.core.presentation.UiState
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.features.voyager.dashboard.domain.usecase.ConfirmSponsorPaymentUseCase
import com.boatit.boatsharing.features.voyager.dashboard.model.PaymentConfirmationRequest
import com.boatit.boatsharing.features.voyager.dashboard.model.VoyagePaymentResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class SponsorPaymentConfirmationUiState(
    val isLoading: Boolean = false,
    val paymentResponse: VoyagePaymentResponse? = null,
    val errorMessage: String? = null,
) : UiState

sealed interface SponsorPaymentConfirmationUiEvent : UiEvent {
    data class ConfirmPayment(val request: PaymentConfirmationRequest) : SponsorPaymentConfirmationUiEvent

    data object Reset : SponsorPaymentConfirmationUiEvent
}

sealed interface SponsorPaymentConfirmationUiEffect : UiEffect {
    data class PaymentConfirmed(val response: VoyagePaymentResponse) : SponsorPaymentConfirmationUiEffect

    data class ShowToast(val message: String) : SponsorPaymentConfirmationUiEffect
}

class SponsorPaymentConfirmationViewModel(
    private val confirmSponsorPaymentUseCase: ConfirmSponsorPaymentUseCase,
) : BaseViewModel<SponsorPaymentConfirmationUiState, SponsorPaymentConfirmationUiEvent, SponsorPaymentConfirmationUiEffect>(
        SponsorPaymentConfirmationUiState(),
    ) {
    private val _loginState = MutableStateFlow<NetworkResponse<VoyagePaymentResponse>>(NetworkResponse.Loading())
    val loginState: StateFlow<NetworkResponse<VoyagePaymentResponse>> = _loginState

    override fun onEvent(event: SponsorPaymentConfirmationUiEvent) {
        when (event) {
            is SponsorPaymentConfirmationUiEvent.ConfirmPayment -> payment(event.request)
            SponsorPaymentConfirmationUiEvent.Reset -> resetNearbyPlaces()
        }
    }

    fun payment(request: PaymentConfirmationRequest) {
        viewModelScope.launch {
            updateState { copy(isLoading = true, errorMessage = null) }
            _loginState.value = NetworkResponse.Loading()

            when (val result = confirmSponsorPaymentUseCase(request).toResource()) {
                is Resource.Success -> {
                    updateState { copy(isLoading = false, paymentResponse = result.data, errorMessage = null) }
                    _loginState.value = NetworkResponse.Success(result.data)
                    emitEffect(SponsorPaymentConfirmationUiEffect.PaymentConfirmed(result.data))
                }

                is Resource.Error -> {
                    val message = result.error.toMessage()
                    updateState { copy(isLoading = false, errorMessage = message) }
                    _loginState.value = NetworkResponse.Error(result.error)
                    emitEffect(SponsorPaymentConfirmationUiEffect.ShowToast(message))
                }

                Resource.Loading -> {
                    updateState { copy(isLoading = true) }
                    _loginState.value = NetworkResponse.Loading()
                }
            }
        }
    }

    fun resetNearbyPlaces() {
        updateState { copy(isLoading = false, paymentResponse = null, errorMessage = null) }
        _loginState.value = NetworkResponse.Loading()
    }
}
