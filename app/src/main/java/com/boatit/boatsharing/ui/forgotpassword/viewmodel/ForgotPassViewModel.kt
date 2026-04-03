package com.boatit.boatsharing.ui.forgotpassword.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.ui.forgotpassword.repository.ForgotPassRepository
import com.boatit.boatsharing.ui.forgotpassword.view.ForgotPassResponse
import com.boatit.boatsharing.ui.login.model.LoginResponse
import com.boatit.boatsharing.ui.login.repository.LoginRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class ForgotPassViewModel(private val repository: ForgotPassRepository) : ViewModel() {

    var email by mutableStateOf("")
    var isError by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var isLoading by mutableStateOf(false)
    var isNetworkError by mutableStateOf(false)

    val isEmailValid: Boolean
        get() = email.contains("@") && email.contains(".")

    val isFormValid: Boolean
        get() = email.isNotEmpty() && isEmailValid

    private val _loginState = MutableStateFlow<NetworkResponse<ForgotPassResponse>>(NetworkResponse.Loading())
    val loginState: StateFlow<NetworkResponse<ForgotPassResponse>> = _loginState

    fun forgotPass() {
        viewModelScope.launch {
            isLoading = true
            _loginState.value = NetworkResponse.Loading()
            val result = repository.forgotPassResp(email)
            result.onSuccess { response ->
                isLoading = false
                isNetworkError = false
                _loginState.value = NetworkResponse.Success(response)
            }.onFailure { error ->
                isLoading = false
                isNetworkError = true
                errorMessage = error.message ?: "Network error, please try again."
                isError = true
                _loginState.value = NetworkResponse.Error(errorMessage!!)
            }
        }
    }

    fun clearError() {
        errorMessage = null
        isError = false
    }
}

