package com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.network.networkreposne.NetworkResponse
import com.boatit.boatsharing.ui.captain.availablitystatus.model.CaptainAvailabilityRequest
import com.boatit.boatsharing.ui.captain.availablitystatus.model.CaptainAvailabilityResponse
import com.boatit.boatsharing.ui.captain.availablitystatus.repository.UpdateStatusRepository
import com.boatit.boatsharing.ui.captain.dashbaord.model.AcceptVoyageRequest
import com.boatit.boatsharing.ui.captain.dashbaord.model.AcceptVoyageResponse
import com.boatit.boatsharing.ui.captain.dashbaord.repository.AcceptRequestRepository
import com.boatit.boatsharing.ui.login.model.LoginResponse
import com.boatit.boatsharing.ui.login.model.UserData
import com.boatit.boatsharing.ui.login.repository.LoginRepository
import com.boatit.boatsharing.ui.voyager.dashbaord.model.VoyagePaymentRequest
import com.boatit.boatsharing.ui.voyager.dashbaord.model.VoyagePaymentResponse
import com.boatit.boatsharing.ui.voyager.dashbaord.repository.PaymentRepository
import com.boatit.boatsharing.utils.prefmanager.SharedPrefManager
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


