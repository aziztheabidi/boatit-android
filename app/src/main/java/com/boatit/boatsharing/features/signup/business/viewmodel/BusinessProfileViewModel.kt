package com.boatit.boatsharing.features.signup.business.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.features.signup.business.domain.usecase.SaveBusinessProfileUseCase
import com.boatit.boatsharing.features.signup.business.model.BusinessProfileRequest
import com.boatit.boatsharing.features.signup.business.model.SaveBusinessProfileResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BusinessProfileViewModel(
    private val saveBusinessProfileUseCase: SaveBusinessProfileUseCase,
) : com.boatit.boatsharing.core.presentation.LegacyMviViewModel() {
    private val _registrationState = MutableStateFlow<NetworkResponse<SaveBusinessProfileResponse>>(NetworkResponse.Loading())
    val registrationState: StateFlow<NetworkResponse<SaveBusinessProfileResponse>> = _registrationState

    fun saveBusinessProfile(profile: BusinessProfileRequest) {
        viewModelScope.launch {
            _registrationState.value = NetworkResponse.Loading()
            when (val result = saveBusinessProfileUseCase(profile).toResource()) {
                is Resource.Success -> {
                    _registrationState.value = NetworkResponse.Success(result.data)
                }

                is Resource.Error -> {
                    _registrationState.value = NetworkResponse.Error(result.error)
                }

                Resource.Loading -> {
                    _registrationState.value = NetworkResponse.Loading()
                }
            }
        }
    }
}
