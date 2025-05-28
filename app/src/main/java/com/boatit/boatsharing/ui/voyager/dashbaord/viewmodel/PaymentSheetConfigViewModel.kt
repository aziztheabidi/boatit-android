package com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.network.networkreposne.NetworkResponse
import com.boatit.boatsharing.ui.voyager.dashbaord.model.PaymentSheetConfigResponse
import com.boatit.boatsharing.ui.voyager.dashbaord.model.VoyagePaymentRequest
import com.boatit.boatsharing.ui.voyager.dashbaord.model.VoyagePaymentResponse
import com.boatit.boatsharing.ui.voyager.dashbaord.repository.PaymentRepository
import com.boatit.boatsharing.ui.voyager.dashbaord.repository.PaymentSheetConfigRepository
import com.stripe.android.paymentsheet.PaymentSheetResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PaymentSheetConfigViewModel(private val repository: PaymentSheetConfigRepository) : ViewModel() {

    private val _loginState = MutableStateFlow<NetworkResponse<PaymentSheetConfigResponse>>(NetworkResponse.Loading())
    val loginState: StateFlow<NetworkResponse<PaymentSheetConfigResponse>> = _loginState

    fun paymentConfig(id : String ) {
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

    fun resetNearbyPlaces() {
        _loginState.value = NetworkResponse.Loading()
    }
}
