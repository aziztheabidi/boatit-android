package com.boatit.boatsharing.ui.voyager.dashbaord.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.network.networkreposne.NetworkResponse
import com.boatit.boatsharing.ui.signup.general.model.RegistrationResponse
import com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel.RegistrationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegistrationViewModel(private val repository: RegistrationRepository) : ViewModel() {

    private val _registrationState = MutableStateFlow<NetworkResponse<RegistrationResponse>>(NetworkResponse.Loading())
    val registrationState: StateFlow<NetworkResponse<RegistrationResponse>> = _registrationState

    fun registerUser(username: String, phoneNumber: String, email: String) {
        viewModelScope.launch {
            _registrationState.value = NetworkResponse.Loading()
            val result = repository.tempRegister(username, phoneNumber, email)
            result.onSuccess { placesResponse ->
                _registrationState.value = NetworkResponse.Success(placesResponse)
            }.onFailure { error ->
                _registrationState.value = NetworkResponse.Error(error.message ?: "Registration failed")
            }
        }
    }
}


