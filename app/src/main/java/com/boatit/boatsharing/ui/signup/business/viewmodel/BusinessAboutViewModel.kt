package com.boatit.boatsharing.ui.signup.business.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.ui.signup.business.model.SaveBusinessAboutRequest
import com.boatit.boatsharing.ui.signup.business.model.SaveBusinessAboutResponse
import com.boatit.boatsharing.ui.signup.business.repository.BusinessAboutRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BusinessAboutViewModel(private val repository: BusinessAboutRepository) : ViewModel() {

    private val _registrationState = MutableStateFlow<NetworkResponse<SaveBusinessAboutResponse>>(NetworkResponse.Loading())
    val registrationState: StateFlow<NetworkResponse<SaveBusinessAboutResponse>> = _registrationState

    fun saveBusinessAbout(profile: SaveBusinessAboutRequest) {
        viewModelScope.launch {
            _registrationState.value = NetworkResponse.Loading()
            val result = repository.BusinessAbout(profile)
            result.onSuccess { placesResponse ->
                _registrationState.value = NetworkResponse.Success(placesResponse)
            }.onFailure { error ->
                _registrationState.value = NetworkResponse.Error(error.message ?: "Registration failed")
            }
        }
    }
}


