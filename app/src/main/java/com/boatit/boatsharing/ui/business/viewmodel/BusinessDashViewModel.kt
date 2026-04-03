package com.boatit.boatsharing.ui.business.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.ui.business.model.BusinessRequest
import com.boatit.boatsharing.ui.business.model.DeleteRequest
import com.boatit.boatsharing.ui.business.repository.BusinessDashboardRepository
import com.boatit.boatsharing.ui.signup.business.model.BusinessProfileRequest
import com.boatit.boatsharing.ui.signup.business.model.SaveBusinessInfoResponse
import com.boatit.boatsharing.ui.signup.business.model.SaveBusinessProfileResponse
import com.boatit.boatsharing.ui.signup.business.repository.BusinessProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BusinessDashViewModel(private val repository: BusinessDashboardRepository) : ViewModel() {

    private val _registrationState = MutableStateFlow<NetworkResponse<SaveBusinessInfoResponse>>(NetworkResponse.Loading())
    val registrationState: StateFlow<NetworkResponse<SaveBusinessInfoResponse>> = _registrationState

    fun saveBusinessProfile(profile: BusinessRequest) {
        viewModelScope.launch {
            _registrationState.value = NetworkResponse.Loading()
            val result = repository.BusinessInfo(profile)
            result.onSuccess { placesResponse ->
                _registrationState.value = NetworkResponse.Success(placesResponse)
            }.onFailure { error ->
                _registrationState.value = NetworkResponse.Error(error.message.toString())
            }
        }
    }

    fun deleteImage(profile: DeleteRequest) {
        viewModelScope.launch {
            _registrationState.value = NetworkResponse.Loading()
            val result = repository.Delete(profile)
            result.onSuccess { placesResponse ->
                _registrationState.value = NetworkResponse.Success(placesResponse)
            }.onFailure { error ->
                _registrationState.value = NetworkResponse.Error(error.message.toString())
            }
        }
    }

    fun resetNearbyPlaces() {
        _registrationState.value = NetworkResponse.Loading()
    }
}


