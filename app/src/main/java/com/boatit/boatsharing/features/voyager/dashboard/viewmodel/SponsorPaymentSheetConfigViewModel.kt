package com.boatit.boatsharing.features.voyager.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.features.captain.dashboard.model.DeclineRequest
import com.boatit.boatsharing.features.voyager.dashboard.domain.usecase.DeclineSponsorPaymentUseCase
import com.boatit.boatsharing.features.voyager.dashboard.domain.usecase.FetchSponsorPaymentSheetConfigUseCase
import com.boatit.boatsharing.features.voyager.dashboard.model.PaymentSheetConfigResponse
import com.boatit.boatsharing.features.voyager.dashboard.model.SponsorVoyagePaymentRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SponsorPaymentSheetConfigViewModel(
    private val fetchSponsorPaymentSheetConfigUseCase: FetchSponsorPaymentSheetConfigUseCase,
    private val declineSponsorPaymentUseCase: DeclineSponsorPaymentUseCase,
) : com.boatit.boatsharing.core.presentation.LegacyMviViewModel() {
    private val _paymentSheetConfigState = MutableStateFlow<NetworkResponse<PaymentSheetConfigResponse>>(NetworkResponse.Loading())
    val paymentSheetConfigState: StateFlow<NetworkResponse<PaymentSheetConfigResponse>> = _paymentSheetConfigState

    @Deprecated("Use paymentSheetConfigState")
    val loginState: StateFlow<NetworkResponse<PaymentSheetConfigResponse>> = paymentSheetConfigState

    private val _declinePaymentState = MutableStateFlow<NetworkResponse<PaymentSheetConfigResponse>>(NetworkResponse.Loading())
    val declinePaymentState: StateFlow<NetworkResponse<PaymentSheetConfigResponse>> = _declinePaymentState

    @Deprecated("Use declinePaymentState")
    val declineState: StateFlow<NetworkResponse<PaymentSheetConfigResponse>> = declinePaymentState

    fun loadPaymentSheetConfig(request: SponsorVoyagePaymentRequest) {
        viewModelScope.launch {
            _paymentSheetConfigState.value = NetworkResponse.Loading()
            when (val result = fetchSponsorPaymentSheetConfigUseCase(request).toResource()) {
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
    fun paymentConfig(id: SponsorVoyagePaymentRequest) {
        loadPaymentSheetConfig(id)
    }

    fun declineSponsorPayment(request: DeclineRequest) {
        viewModelScope.launch {
            _declinePaymentState.value = NetworkResponse.Loading()
            when (val result = declineSponsorPaymentUseCase(request).toResource()) {
                is Resource.Success -> {
                    _declinePaymentState.value = NetworkResponse.Success(result.data)
                }

                is Resource.Error -> {
                    _declinePaymentState.value = NetworkResponse.Error(result.error)
                }

                Resource.Loading -> {
                    _declinePaymentState.value = NetworkResponse.Loading()
                }
            }
        }
    }

    @Deprecated("Use declineSponsorPayment")
    fun paymentDecline(id: DeclineRequest) {
        declineSponsorPayment(id)
    }

    fun resetPaymentSheetState() {
        _paymentSheetConfigState.value = NetworkResponse.Loading()
        _declinePaymentState.value = NetworkResponse.Loading()
    }

    @Deprecated("Use resetPaymentSheetState")
    fun resetNearbyPlaces() {
        resetPaymentSheetState()
    }
}
