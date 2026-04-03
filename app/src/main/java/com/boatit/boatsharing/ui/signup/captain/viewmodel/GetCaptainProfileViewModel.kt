package com.boatit.boatsharing.ui.signup.captain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.ui.signup.captain.model.GetCaptainProfileResponse
import com.boatit.boatsharing.ui.signup.captain.repository.GetCaptainProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GetCaptainProfileViewModel(private val repository: GetCaptainProfileRepository) : ViewModel() {

    private val _registrationState = MutableStateFlow<NetworkResponse<GetCaptainProfileResponse>>(NetworkResponse.Loading())
    val registrationState: StateFlow<NetworkResponse<GetCaptainProfileResponse>> = _registrationState

    fun GetCaptainProfile() {
        viewModelScope.launch {
            _registrationState.value = NetworkResponse.Loading()
            val result = repository.GetCaptainProfile()
            result.onSuccess { placesResponse ->
                _registrationState.value = NetworkResponse.Success(placesResponse)
            }.onFailure { error ->
                _registrationState.value = NetworkResponse.Error(error.message ?: "Registration failed")
            }
        }
    }
}


