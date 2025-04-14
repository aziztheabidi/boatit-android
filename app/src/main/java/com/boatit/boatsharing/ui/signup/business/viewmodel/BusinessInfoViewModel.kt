package com.boatit.boatsharing.ui.signup.business.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.network.networkreposne.NetworkResponse
import com.boatit.boatsharing.ui.signup.business.model.BusinessInfoRequest
import com.boatit.boatsharing.ui.signup.business.model.SaveBusinessInfoResponse
import com.boatit.boatsharing.ui.signup.business.repository.BusinessInfoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BusinessInfoViewModel(private val repository: BusinessInfoRepository) : ViewModel() {

    private val _registrationState = MutableStateFlow<NetworkResponse<SaveBusinessInfoResponse>>(NetworkResponse.Loading())
    val registrationState: StateFlow<NetworkResponse<SaveBusinessInfoResponse>> = _registrationState

    fun saveBusinessProfile(profile: BusinessInfoRequest) {
        viewModelScope.launch {
            _registrationState.value = NetworkResponse.Loading()
            val result = repository.BusinessInfo(profile)
            result.onSuccess { placesResponse ->
                _registrationState.value = NetworkResponse.Success(placesResponse)
            }.onFailure { error ->
                _registrationState.value = NetworkResponse.Error(error.message ?: "Registration failed")
            }
        }
    }
}


