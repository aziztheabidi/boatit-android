package com.boatit.boatsharing.ui.signup.business.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.network.networkreposne.NetworkResponse
import com.boatit.boatsharing.ui.signup.business.model.BusinessInfoResponse
import com.boatit.boatsharing.ui.signup.business.model.GetBusinessProfileResponse
import com.boatit.boatsharing.ui.signup.business.repository.GetBusinessInfoRepository
import com.boatit.boatsharing.ui.signup.business.repository.GetBusinessProfileRepository
import com.boatit.boatsharing.ui.signup.captain.model.GetCaptainProfileResponse
import com.boatit.boatsharing.ui.signup.captain.repository.GetCaptainProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GetBusinessInfoViewModel(private val repository: GetBusinessInfoRepository) : ViewModel() {

    private val _registrationState = MutableStateFlow<NetworkResponse<BusinessInfoResponse>>(NetworkResponse.Loading())
    val registrationState: StateFlow<NetworkResponse<BusinessInfoResponse>> = _registrationState

    fun GetBusinessProfile() {
        viewModelScope.launch {
            _registrationState.value = NetworkResponse.Loading()
            val result = repository.GetBusinessInfo()
            result.onSuccess { placesResponse ->
                _registrationState.value = NetworkResponse.Success(placesResponse)
            }.onFailure { error ->
                _registrationState.value = NetworkResponse.Error(error.message ?: "Registration failed")
            }
        }
    }
}


