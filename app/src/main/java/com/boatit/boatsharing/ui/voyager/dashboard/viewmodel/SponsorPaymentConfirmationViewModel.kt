package com.boatit.boatsharing.ui.voyager.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.ui.voyager.dashboard.model.PaymentConfirmationRequest
import com.boatit.boatsharing.ui.voyager.dashboard.model.VoyagePaymentRequest
import com.boatit.boatsharing.ui.voyager.dashboard.model.VoyagePaymentResponse
import com.boatit.boatsharing.ui.voyager.dashboard.repository.PaymentRepository
import com.boatit.boatsharing.ui.voyager.dashboard.repository.SponsorPaymentConfirmationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class SponsorPaymentConfirmationViewModel(private val repository: SponsorPaymentConfirmationRepository) : ViewModel() {

    private val _loginState = MutableStateFlow<NetworkResponse<VoyagePaymentResponse>>(NetworkResponse.Loading())
    val loginState: StateFlow<NetworkResponse<VoyagePaymentResponse>> = _loginState

    fun payment(request: PaymentConfirmationRequest) {
        viewModelScope.launch {
            _loginState.value = NetworkResponse.Loading()
            val result = repository.payment(request)
            result.onSuccess { response ->
                _loginState.value = NetworkResponse.Success(response)
            }.onFailure { error ->
                _loginState.value = NetworkResponse.Error(error.message ?: "Login failed")
            }
        }
    }

    fun resetNearbyPlaces() {
        _loginState.value = NetworkResponse.Loading()
    }
}


