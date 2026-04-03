package com.boatit.boatsharing.ui.signup.captain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.ui.signup.captain.model.SaveCaptainBoatRequest
import com.boatit.boatsharing.ui.signup.captain.model.SaveCaptainBoatResponse
import com.boatit.boatsharing.ui.signup.captain.repository.CaptainBoatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CaptainBoatViewModel(private val repository: CaptainBoatRepository) : ViewModel() {

    private val _registrationState = MutableStateFlow<NetworkResponse<SaveCaptainBoatResponse>>(NetworkResponse.Loading())
    val registrationState: StateFlow<NetworkResponse<SaveCaptainBoatResponse>> = _registrationState

    fun saveProfile(profile: SaveCaptainBoatRequest) {
        viewModelScope.launch {
            _registrationState.value = NetworkResponse.Loading()
            val result = repository.CaptainBoat(profile)
            result.onSuccess { placesResponse ->
                _registrationState.value = NetworkResponse.Success(placesResponse)
            }.onFailure { error ->
                _registrationState.value = NetworkResponse.Error(error.message ?: "Registration failed")
            }
        }
    }
}


