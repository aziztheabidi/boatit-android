package com.boatit.boatsharing.ui.login.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.network.networkreposne.NetworkResponse
import com.boatit.boatsharing.ui.login.model.LoginResponse
import com.boatit.boatsharing.ui.login.model.UserData
import com.boatit.boatsharing.ui.login.repository.LoginRepository
import com.boatit.boatsharing.utils.prefmanager.SharedPrefManager
import com.boatit.boatsharing.utils.prefmanager.TokenProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class LoginViewModel(
    private val repository: LoginRepository,
    private val sharedPrefManager: SharedPrefManager
) : ViewModel() {

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var isError by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    val isEmailValid: Boolean get() = email.contains("@") && email.contains(".")

    val isPasswordValid: Boolean get() = password.length >= 6

    val isFormValid: Boolean get() = email.isNotEmpty() && password.isNotEmpty() && isEmailValid && isPasswordValid

    private val _loginState = MutableStateFlow<NetworkResponse<LoginResponse>>(NetworkResponse.Loading())
    val loginState: StateFlow<NetworkResponse<LoginResponse>> = _loginState

    fun onEmailChange(value: String) {
        email = value
        clearError()
    }

    fun onPasswordChange(value: String) {
        password = value
        clearError()
    }

    private fun clearError() {
        isError = false
        errorMessage = null
    }

    fun login() {
        viewModelScope.launch {
            isLoading = true
            _loginState.value = NetworkResponse.Loading()
            val result = repository.login(email, password)
            result.onSuccess { response ->
                _loginState.value = NetworkResponse.Success(response)
                saveLoginData(response.obj!!)
            }.onFailure { error ->
                _loginState.value = NetworkResponse.Error(error.message ?: "Login failed")
                isError = true
                errorMessage = error.message ?: "Login failed"
            }
            isLoading = false
        }
    }

    private fun saveLoginData(userData: UserData) {
        sharedPrefManager.saveLoginData(userData)
    }

    fun resetNearbyPlaces() {
        _loginState.value = NetworkResponse.Loading()
    }

    fun getUserData(): UserData? = sharedPrefManager.getUserData()

    fun clearUserData() = sharedPrefManager.clearUserData()
}
