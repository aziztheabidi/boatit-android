package com.boatit.boatsharing.features.voyager.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.features.voyager.dashboard.domain.usecase.FetchPaymentSheetConfigUseCase
import com.boatit.boatsharing.features.voyager.dashboard.model.PaymentSheetConfigResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PaymentSheetConfigViewModel(private val fetchPaymentSheetConfigUseCase: FetchPaymentSheetConfigUseCase) : com.boatit.boatsharing.core.presentation.LegacyMviViewModel() {
    private val _paymentSheetConfigState = MutableStateFlow<NetworkResponse<PaymentSheetConfigResponse>>(NetworkResponse.Loading())
    val paymentSheetConfigState: StateFlow<NetworkResponse<PaymentSheetConfigResponse>> = _paymentSheetConfigState

    @Deprecated("Use paymentSheetConfigState")
    val loginState: StateFlow<NetworkResponse<PaymentSheetConfigResponse>> = paymentSheetConfigState

    fun loadPaymentSheetConfig(id: String) {
        viewModelScope.launch {
            _paymentSheetConfigState.value = NetworkResponse.Loading()
            when (val result = fetchPaymentSheetConfigUseCase(id).toResource()) {
                is Resource.Success -> {
                    _paymentSheetConfigState.value = NetworkResponse.Success(result.data)
                }

                is Resource.Error -> {
                    _paymentSheetConfigState.value = NetworkResponse.Error(result.error)
                }

                Resource.Loading -> {
                    _paymentSheetConfigState.value = NetworkResponse.Loading()
                }
            }
        }
    }

    @Deprecated("Use loadPaymentSheetConfig")
    fun paymentConfig(id: String) {
        loadPaymentSheetConfig(id)
    }

    fun resetPaymentSheetState() {
        _paymentSheetConfigState.value = NetworkResponse.Loading()
    }

    @Deprecated("Use resetPaymentSheetState")
    fun resetNearbyPlaces() {
        resetPaymentSheetState()
    }
}
