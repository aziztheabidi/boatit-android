package com.boatit.boatsharing.ui.signup.general.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.network.networkreposne.NetworkResponse
import com.boatit.boatsharing.ui.signup.general.model.GetVoyagerProfileResponse
import com.boatit.boatsharing.ui.signup.general.model.RegistrationResponse
import com.boatit.boatsharing.ui.signup.general.model.VoyagerProfileRequest
import com.boatit.boatsharing.ui.signup.general.model.VoyagerProfileResponse
import com.boatit.boatsharing.ui.signup.general.viewmodel.GetVoyagerProfileRepository
import com.boatit.boatsharing.ui.signup.general.viewmodel.VoyagerProfileRepository
import com.boatit.boatsharing.ui.voyager.dashboard.viewmodel.RegistrationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GetVoyagerProfileViewModel(private val repository: GetVoyagerProfileRepository) : ViewModel() {

    private val _registrationState = MutableStateFlow<NetworkResponse<GetVoyagerProfileResponse>>(NetworkResponse.Loading())
    val registrationState: StateFlow<NetworkResponse<GetVoyagerProfileResponse>> = _registrationState

    fun GetVoyagerProfile() {
        viewModelScope.launch {
            _registrationState.value = NetworkResponse.Loading()
            val result = repository.getVoyagerProfile()
            result.onSuccess { placesResponse ->
                _registrationState.value = NetworkResponse.Success(placesResponse)
            }.onFailure { error ->
                _registrationState.value = NetworkResponse.Error(error.message ?: "Registration failed")
            }
        }
    }
}


