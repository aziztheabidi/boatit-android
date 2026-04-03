package com.boatit.boatsharing.ui.voyager.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.ui.voyager.dashboard.model.VoyagePaymentRequest
import com.boatit.boatsharing.ui.voyager.dashboard.model.VoyagePaymentResponse
import com.boatit.boatsharing.ui.voyager.dashboard.repository.PaymentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class PaymentViewModel(private val repository: PaymentRepository) : ViewModel() {

    private val _loginState = MutableStateFlow<NetworkResponse<VoyagePaymentResponse>>(NetworkResponse.Loading())
    val loginState: StateFlow<NetworkResponse<VoyagePaymentResponse>> = _loginState

    fun payment(request: VoyagePaymentRequest) {
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


