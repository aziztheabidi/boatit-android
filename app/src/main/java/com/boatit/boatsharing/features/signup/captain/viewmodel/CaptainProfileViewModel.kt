package com.boatit.boatsharing.features.signup.captain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.features.signup.captain.domain.usecase.SaveCaptainProfileUseCase
import com.boatit.boatsharing.features.signup.captain.model.CaptainProfileRequest
import com.boatit.boatsharing.features.signup.captain.model.CaptainProfileResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CaptainProfileViewModel(
    private val saveCaptainProfileUseCase: SaveCaptainProfileUseCase,
) : com.boatit.boatsharing.core.presentation.LegacyMviViewModel() {
    private val _registrationState = MutableStateFlow<NetworkResponse<CaptainProfileResponse>>(NetworkResponse.Loading())
    val registrationState: StateFlow<NetworkResponse<CaptainProfileResponse>> = _registrationState

    fun saveProfile(profile: CaptainProfileRequest) {
        viewModelScope.launch {
            _registrationState.value = NetworkResponse.Loading()
            when (val result = saveCaptainProfileUseCase(profile).toResource()) {
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
