package com.boatit.boatsharing.ui.signup.captain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.network.networkreposne.NetworkResponse
import com.boatit.boatsharing.ui.signup.captain.model.CaptainProfileRequest
import com.boatit.boatsharing.ui.signup.captain.model.CaptainProfileResponse
import com.boatit.boatsharing.ui.signup.captain.model.GetCaptainProfileResponse
import com.boatit.boatsharing.ui.signup.captain.repository.CaptainProfileRepository
import com.boatit.boatsharing.ui.signup.general.model.VoyagerProfileRequest
import com.boatit.boatsharing.ui.signup.general.model.VoyagerProfileResponse
import com.boatit.boatsharing.ui.signup.general.viewmodel.VoyagerProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CaptainProfileViewModel(private val repository: CaptainProfileRepository) : ViewModel() {

    private val _registrationState = MutableStateFlow<NetworkResponse<CaptainProfileResponse>>(NetworkResponse.Loading())
    val registrationState: StateFlow<NetworkResponse<CaptainProfileResponse>> = _registrationState

    fun saveProfile(profile: CaptainProfileRequest) {
        viewModelScope.launch {
            _registrationState.value = NetworkResponse.Loading()
            val result = repository.CaptainProfile(profile)
            result.onSuccess { placesResponse ->
                _registrationState.value = NetworkResponse.Success(placesResponse)
            }.onFailure { error ->
                _registrationState.value = NetworkResponse.Error(error.message ?: "Registration failed")
            }
        }
    }
}


