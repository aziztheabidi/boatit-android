package com.boatit.boatsharing.features.signup.business.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.features.signup.business.domain.usecase.FetchBusinessProfileUseCase
import com.boatit.boatsharing.features.signup.business.model.GetBusinessProfileResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GetBusinessProfileViewModel(
    private val fetchBusinessProfileUseCase: FetchBusinessProfileUseCase,
) : com.boatit.boatsharing.core.presentation.LegacyMviViewModel() {
    private val _registrationState = MutableStateFlow<NetworkResponse<GetBusinessProfileResponse>>(NetworkResponse.Loading())
    val registrationState: StateFlow<NetworkResponse<GetBusinessProfileResponse>> = _registrationState

    fun GetBusinessProfile() {
        viewModelScope.launch {
            _registrationState.value = NetworkResponse.Loading()
            when (val result = fetchBusinessProfileUseCase().toResource()) {
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
