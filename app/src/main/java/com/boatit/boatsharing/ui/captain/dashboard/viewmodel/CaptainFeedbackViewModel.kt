package com.boatit.boatsharing.ui.captain.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.ui.captain.dashboard.model.CaptainFeedbackRequest
import com.boatit.boatsharing.ui.captain.dashboard.model.CaptainFeedbackResponse
import com.boatit.boatsharing.ui.captain.dashboard.repository.CaptainFeedbackRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CaptainFeedbackViewModel(private val repository: CaptainFeedbackRepository) : ViewModel() {

    private val _loginState = MutableStateFlow<NetworkResponse<CaptainFeedbackResponse>>(NetworkResponse.Loading())
    val loginState: StateFlow<NetworkResponse<CaptainFeedbackResponse>> = _loginState

    fun captainFeedbackFunc(request: CaptainFeedbackRequest) {
        viewModelScope.launch {
            _loginState.value = NetworkResponse.Loading()
            val result = repository.status(request)
            result.onSuccess { response ->
                _loginState.value = NetworkResponse.Success(response)
            }.onFailure { error ->
                _loginState.value = NetworkResponse.Error(error.message ?: "Login failed")
            }
        }
    }
}
