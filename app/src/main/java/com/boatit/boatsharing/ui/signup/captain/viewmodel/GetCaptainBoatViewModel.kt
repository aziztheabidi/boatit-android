package com.boatit.boatsharing.ui.signup.captain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.ui.signup.captain.model.GetCaptainBoatResponse
import com.boatit.boatsharing.ui.signup.captain.model.GetCaptainProfileResponse
import com.boatit.boatsharing.ui.signup.captain.repository.GetCaptainBoatRepository
import com.boatit.boatsharing.ui.signup.captain.repository.GetCaptainProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GetCaptainBoatViewModel(private val repository: GetCaptainBoatRepository) : ViewModel() {

    private val _registrationState = MutableStateFlow<NetworkResponse<GetCaptainBoatResponse>>(NetworkResponse.Loading())
    val registrationState: StateFlow<NetworkResponse<GetCaptainBoatResponse>> = _registrationState

    fun GetCaptainBoat() {
        viewModelScope.launch {
            _registrationState.value = NetworkResponse.Loading()
            val result = repository.GetCaptainBoat()
            result.onSuccess { placesResponse ->
                _registrationState.value = NetworkResponse.Success(placesResponse)
            }.onFailure { error ->
                _registrationState.value = NetworkResponse.Error(error.message ?: "Registration failed")
            }
        }
    }
}


