package com.boatit.boatsharing.ui.signup.general.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.network.networkreposne.NetworkResponse
import com.boatit.boatsharing.network.networkreposne.NetworkResponse.Loading
import com.boatit.boatsharing.ui.login.model.LoginResponse
import com.boatit.boatsharing.ui.login.model.UserData
import com.boatit.boatsharing.ui.signup.general.model.RegistrationResponse
import com.boatit.boatsharing.ui.signup.general.viewmodel.PasswordRepository
import com.boatit.boatsharing.utils.prefmanager.SharedPrefManager

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PasswordViewModel(
    private val repository: PasswordRepository,
    private val sharedPrefManager: SharedPrefManager
) : ViewModel() {

    private val _registrationState = MutableStateFlow<NetworkResponse<LoginResponse>>(NetworkResponse.Loading())
    val registrationState: StateFlow<NetworkResponse<LoginResponse>> = _registrationState

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
    }

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun passwordReg(password: String, token: String) {
        viewModelScope.launch {
            _registrationState.value = NetworkResponse.Loading()
            _isLoading.value = true
            val result = repository.passwordRepository(password, token)
            result.onSuccess { response ->
                _registrationState.value = NetworkResponse.Success(response)
                _isLoading.value = false
                saveLoginData(response.obj!!)
            }.onFailure { error ->
                _registrationState.value = NetworkResponse.Error(error.message ?: "Registration failed")
                _isLoading.value = false
            }
        }
    }

    private fun saveLoginData(userData: UserData) {
        sharedPrefManager.saveLoginData(userData)
    }

    fun resetState() {
        _registrationState.value = NetworkResponse.Loading()
    }
}



