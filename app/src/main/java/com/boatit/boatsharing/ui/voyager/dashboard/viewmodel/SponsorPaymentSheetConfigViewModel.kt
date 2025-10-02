package com.boatit.boatsharing.ui.voyager.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.ui.captain.dashboard.model.DeclineRequest
import com.boatit.boatsharing.ui.voyager.dashboard.model.PaymentSheetConfigResponse
import com.boatit.boatsharing.ui.voyager.dashboard.model.SponsorVoyagePaymentRequest
import com.boatit.boatsharing.ui.voyager.dashboard.model.VoyagePaymentRequest
import com.boatit.boatsharing.ui.voyager.dashboard.model.VoyagePaymentResponse
import com.boatit.boatsharing.ui.voyager.dashboard.repository.PaymentRepository
import com.boatit.boatsharing.ui.voyager.dashboard.repository.PaymentSheetConfigRepository
import com.boatit.boatsharing.ui.voyager.dashboard.repository.SponsorPaymentSheetConfigRepository
import com.stripe.android.paymentsheet.PaymentSheetResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SponsorPaymentSheetConfigViewModel(private val repository: SponsorPaymentSheetConfigRepository) : ViewModel() {

    private val _loginState = MutableStateFlow<NetworkResponse<PaymentSheetConfigResponse>>(NetworkResponse.Loading())
    val loginState: StateFlow<NetworkResponse<PaymentSheetConfigResponse>> = _loginState

    private val _declineState = MutableStateFlow<NetworkResponse<PaymentSheetConfigResponse>>(NetworkResponse.Loading())
    val declineState: StateFlow<NetworkResponse<PaymentSheetConfigResponse>> = _declineState


    fun paymentConfig(id : SponsorVoyagePaymentRequest) {
        viewModelScope.launch {
            _loginState.value = NetworkResponse.Loading()
            val result = repository.SheetConfi(id)
            result.onSuccess { response ->
                _loginState.value = NetworkResponse.Success(response)
            }.onFailure { error ->
                _loginState.value = NetworkResponse.Error(error.message ?: "Login failed")
            }
        }
    }

    fun paymentDecline(id : DeclineRequest) {
        viewModelScope.launch {
            _declineState.value = NetworkResponse.Loading()
            val result = repository.paymentDecline(id)
            result.onSuccess { response ->
                _declineState.value = NetworkResponse.Success(response)
            }.onFailure { error ->
                _declineState.value = NetworkResponse.Error(error.message ?: "Login failed")
            }
        }
    }

    fun resetNearbyPlaces() {
        _loginState.value = NetworkResponse.Loading()
        _declineState.value = NetworkResponse.Loading()
    }
}