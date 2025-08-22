package com.boatit.boatsharing.ui.signup.general.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.network.networkreposne.NetworkResponse
import com.boatit.boatsharing.ui.login.model.UserData
import com.boatit.boatsharing.ui.signup.general.model.RegistrationResponse
import com.boatit.boatsharing.ui.signup.general.model.VoyagerProfileRequest
import com.boatit.boatsharing.ui.signup.general.model.VoyagerProfileResponse
import com.boatit.boatsharing.ui.signup.general.viewmodel.VoyagerProfileRepository
import com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel.RegistrationRepository
import com.boatit.boatsharing.utils.prefmanager.SharedPrefManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class VoyagerProfileViewModel(private val repository: VoyagerProfileRepository,
    private val sharedPrefManager: SharedPrefManager
) : ViewModel() {

    private val _registrationState = MutableStateFlow<NetworkResponse<VoyagerProfileResponse>>(NetworkResponse.Loading())
    val registrationState: StateFlow<NetworkResponse<VoyagerProfileResponse>> = _registrationState

    fun saveProfile(profile: VoyagerProfileRequest) {
        viewModelScope.launch {
            _registrationState.value = NetworkResponse.Loading()
            val result = repository.saveVoyagerProfile(profile)
            result.onSuccess { placesResponse ->
                _registrationState.value = NetworkResponse.Success(placesResponse)
                saveLoginData(0)
            }.onFailure { error ->
                _registrationState.value = NetworkResponse.Error(error.message ?: "Registration failed")
            }
        }
    }

    private fun saveLoginData(userData: Int) {
        sharedPrefManager.saveMissingStep(userData)
    }
}


