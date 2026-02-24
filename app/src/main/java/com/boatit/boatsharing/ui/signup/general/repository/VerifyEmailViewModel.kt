package com.boatit.boatsharing.ui.signup.general.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.network.networkresponse.NetworkResponse.Loading
import com.boatit.boatsharing.ui.signup.general.model.VerifyEmailResponse
import com.boatit.boatsharing.ui.signup.general.viewmodel.VerifyEmailRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class VerifyEmailViewModel(private val repository: VerifyEmailRepository) : ViewModel() {

    private val _registrationState = MutableStateFlow<NetworkResponse<VerifyEmailResponse>>(Loading())
    val registrationState: StateFlow<NetworkResponse<VerifyEmailResponse>> = _registrationState

    fun VerifyEmail(email: String, otp: String) {
        viewModelScope.launch {
            _registrationState.value = Loading()
            val result = repository.verifyEmail(email, otp)
            result.onSuccess { placesResponse ->
                _registrationState.value = NetworkResponse.Success(placesResponse)
            }.onFailure { error ->
                _registrationState.value = NetworkResponse.Error(error.message ?: "Registration failed")
            }
        }
    }
}


