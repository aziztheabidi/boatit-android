package com.boatit.boatsharing.features.voyager.dashboard.viewmodel

import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.core.presentation.BaseViewModel
import com.boatit.boatsharing.core.presentation.UiEffect
import com.boatit.boatsharing.core.presentation.UiEvent
import com.boatit.boatsharing.core.presentation.UiState
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.features.voyager.dashboard.domain.usecase.ConfirmVoyagePaymentUseCase
import com.boatit.boatsharing.features.voyager.dashboard.model.PaymentConfirmationRequest
import com.boatit.boatsharing.features.voyager.dashboard.model.VoyagePaymentResponse
import kotlinx.coroutines.launch

data class PaymentUiState(
    val isLoading: Boolean = false,
    val paymentResponse: VoyagePaymentResponse? = null,
    val errorMessage: String? = null,
    /** Same contract as the former `loginState` flow: payment gateway [NetworkResponse]. */
    val networkState: NetworkResponse<VoyagePaymentResponse> = NetworkResponse.Loading(),
) : UiState

sealed interface PaymentUiEvent : UiEvent {
    data class ConfirmPayment(val request: PaymentConfirmationRequest) : PaymentUiEvent

    data object Reset : PaymentUiEvent
}

sealed interface PaymentUiEffect : UiEffect {
    data class PaymentConfirmed(val response: VoyagePaymentResponse) : PaymentUiEffect

    data class ShowToast(val message: String) : PaymentUiEffect
}

class PaymentViewModel(
    private val confirmVoyagePaymentUseCase: ConfirmVoyagePaymentUseCase,
) : BaseViewModel<PaymentUiState, PaymentUiEvent, PaymentUiEffect>(PaymentUiState()) {
    override fun onEvent(event: PaymentUiEvent) {
        when (event) {
            is PaymentUiEvent.ConfirmPayment -> payment(event.request)
            PaymentUiEvent.Reset -> resetNearbyPlaces()
        }
    }

    fun payment(request: PaymentConfirmationRequest) {
        viewModelScope.launch {
            updateState {
                copy(
                    isLoading = true,
                    errorMessage = null,
                    networkState = NetworkResponse.Loading(),
                )
            }

            when (val result = confirmVoyagePaymentUseCase(request).toResource()) {
                is Resource.Success -> {
                    updateState {
                        copy(
                            isLoading = false,
                            paymentResponse = result.data,
                            errorMessage = null,
                            networkState = NetworkResponse.Success(result.data),
                        )
                    }
                    emitEffect(PaymentUiEffect.PaymentConfirmed(result.data))
                }

                is Resource.Error -> {
                    val message = result.error.toMessage()
                    updateState {
                        copy(
                            isLoading = false,
                            errorMessage = message,
                            networkState = NetworkResponse.Error(result.error),
                        )
                    }
                    emitEffect(PaymentUiEffect.ShowToast(message))
                }

                Resource.Loading -> {
                    updateState {
                        copy(
                            isLoading = true,
                            networkState = NetworkResponse.Loading(),
                        )
                    }
                }
            }
        }
    }

    fun resetNearbyPlaces() {
        updateState {
            copy(
                isLoading = false,
                paymentResponse = null,
                errorMessage = null,
                networkState = NetworkResponse.Loading(),
            )
        }
    }
}
