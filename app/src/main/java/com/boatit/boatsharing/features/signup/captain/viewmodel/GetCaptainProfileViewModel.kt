package com.boatit.boatsharing.features.signup.captain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.features.signup.captain.domain.usecase.FetchCaptainProfileUseCase
import com.boatit.boatsharing.features.signup.captain.model.GetCaptainProfileResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GetCaptainProfileViewModel(
    private val fetchCaptainProfileUseCase: FetchCaptainProfileUseCase,
) : com.boatit.boatsharing.core.presentation.LegacyMviViewModel() {
    private val _registrationState = MutableStateFlow<NetworkResponse<GetCaptainProfileResponse>>(NetworkResponse.Loading())
    val registrationState: StateFlow<NetworkResponse<GetCaptainProfileResponse>> = _registrationState

    fun GetCaptainProfile() {
        viewModelScope.launch {
            _registrationState.value = NetworkResponse.Loading()
            when (val result = fetchCaptainProfileUseCase().toResource()) {
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
