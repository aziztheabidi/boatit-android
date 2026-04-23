package com.boatit.boatsharing.features.voyager.dashboard.viewmodel

import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.core.presentation.BaseViewModel
import com.boatit.boatsharing.core.presentation.UiEffect
import com.boatit.boatsharing.core.presentation.UiEvent
import com.boatit.boatsharing.core.presentation.UiState
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.features.voyager.dashboard.domain.usecase.FetchPaymentSheetConfigUseCase
import com.boatit.boatsharing.features.voyager.dashboard.model.PaymentSheetConfigResponse
import kotlinx.coroutines.launch

data class PaymentSheetConfigUiState(
    val paymentSheetConfigState: NetworkResponse<PaymentSheetConfigResponse> = NetworkResponse.Loading(),
) : UiState

sealed interface PaymentSheetConfigUiEvent : UiEvent {
    data class LoadPaymentSheetConfig(
        val id: String,
    ) : PaymentSheetConfigUiEvent

    data object ResetPaymentSheetState : PaymentSheetConfigUiEvent
}

sealed interface PaymentSheetConfigUiEffect : UiEffect {
    data object NoOpEffect : PaymentSheetConfigUiEffect
}

class PaymentSheetConfigViewModel(
    private val fetchPaymentSheetConfigUseCase: FetchPaymentSheetConfigUseCase,
) : BaseViewModel<PaymentSheetConfigUiState, PaymentSheetConfigUiEvent, PaymentSheetConfigUiEffect>(
        PaymentSheetConfigUiState(),
    ) {
    override fun onEvent(event: PaymentSheetConfigUiEvent) {
        when (event) {
            is PaymentSheetConfigUiEvent.LoadPaymentSheetConfig -> loadPaymentSheetConfig(event.id)
            PaymentSheetConfigUiEvent.ResetPaymentSheetState -> resetPaymentSheetState()
        }
    }

    private fun loadPaymentSheetConfig(id: String) {
        viewModelScope.launch {
            updateState { copy(paymentSheetConfigState = NetworkResponse.Loading()) }
            when (val result = fetchPaymentSheetConfigUseCase(id).toResource()) {
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

    private fun resetPaymentSheetState() {
        updateState { copy(paymentSheetConfigState = NetworkResponse.Loading()) }
    }
}
