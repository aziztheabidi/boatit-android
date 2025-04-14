package com.boatit.boatsharing.ui.forgotpassword.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.network.networkreposne.NetworkResponse
import com.boatit.boatsharing.ui.forgotpassword.repository.ForgotPassRepository
import com.boatit.boatsharing.ui.forgotpassword.view.ForgotPassResponse
import com.boatit.boatsharing.ui.login.model.LoginResponse
import com.boatit.boatsharing.ui.login.repository.LoginRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class ForgotPassViewModel(private val repository: ForgotPassRepository) : ViewModel() {

    private val _loginState = MutableStateFlow<NetworkResponse<ForgotPassResponse>>(NetworkResponse.Loading())
    val loginState: StateFlow<NetworkResponse<ForgotPassResponse>> = _loginState

    fun forgotPass(email: String) {
        viewModelScope.launch {
            _loginState.value = NetworkResponse.Loading()
            val result = repository.forgotPassResp(email)
            result.onSuccess { response ->
                _loginState.value = NetworkResponse.Success(response)
            }.onFailure { error ->
                _loginState.value = NetworkResponse.Error(error.message ?: "Login failed")
            }
        }
    }
}


