package com.boatit.boatsharing.ui.signup.general.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.network.networkresponse.NetworkResponse.Loading
import com.boatit.boatsharing.ui.login.model.LoginResponse
import com.boatit.boatsharing.ui.login.model.UserData
import com.boatit.boatsharing.ui.signup.general.model.RegistrationResponse
import com.boatit.boatsharing.ui.signup.general.viewmodel.PasswordRepository
import com.boatit.boatsharing.utils.prefmanager.SharedPrefManager

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PasswordViewModel(private val repository: PasswordRepository, private val sharedPrefManager: SharedPrefManager) : ViewModel() {

    private val _registrationState = MutableStateFlow<NetworkResponse<LoginResponse>>(Loading())
    val registrationState: StateFlow<NetworkResponse<LoginResponse>> = _registrationState

    fun passwordReg(password: String, token: String) {
        viewModelScope.launch {
            _registrationState.value = Loading()
            val result = repository.passwordRepository(password, token)
            result.onSuccess { placesResponse ->
                _registrationState.value = NetworkResponse.Success(placesResponse)
                saveLoginData(placesResponse.obj)
            }.onFailure { error ->
                _registrationState.value = NetworkResponse.Error(error.message ?: "Registration failed")
            }
        }
    }

    fun saveLoginData(userData: UserData) {
        sharedPrefManager.saveLoginData(userData)
    }
}


