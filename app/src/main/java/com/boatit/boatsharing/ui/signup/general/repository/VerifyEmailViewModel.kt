package com.boatit.boatsharing.ui.signup.general.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.network.networkreposne.NetworkResponse
import com.boatit.boatsharing.network.networkreposne.NetworkResponse.Loading
import com.boatit.boatsharing.ui.signup.general.model.VerifyEmailResponse
import com.boatit.boatsharing.ui.signup.general.viewmodel.VerifyEmailRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class VerifyEmailViewModel(
    private val repository: VerifyEmailRepository
) : ViewModel() {

    private val _registrationState = MutableStateFlow<NetworkResponse<VerifyEmailResponse>>(NetworkResponse.Loading())
    val registrationState: StateFlow<NetworkResponse<VerifyEmailResponse>> = _registrationState

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun verifyEmail(email: String, otp: String) {
        _isLoading.value = true
        _errorMessage.value = null
        _registrationState.value = NetworkResponse.Loading()

        viewModelScope.launch {
            val result = repository.verifyEmail(email, otp)
            result.onSuccess { response ->
                _registrationState.value = NetworkResponse.Success(response)
                _isLoading.value = false
            }.onFailure { error ->
                _registrationState.value = NetworkResponse.Error(error.message ?: "Verification failed")
                _isLoading.value = false
                _errorMessage.value = error.message
            }
        }
    }
}



