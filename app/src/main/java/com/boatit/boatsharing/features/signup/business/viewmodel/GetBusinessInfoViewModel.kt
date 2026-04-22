package com.boatit.boatsharing.features.signup.business.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.features.signup.business.domain.usecase.FetchBusinessInfoUseCase
import com.boatit.boatsharing.features.signup.business.model.BusinessInfoResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GetBusinessInfoViewModel(
    private val fetchBusinessInfoUseCase: FetchBusinessInfoUseCase,
) : com.boatit.boatsharing.core.presentation.LegacyMviViewModel() {
    private val _registrationState = MutableStateFlow<NetworkResponse<BusinessInfoResponse>>(NetworkResponse.Loading())
    val registrationState: StateFlow<NetworkResponse<BusinessInfoResponse>> = _registrationState

    fun GetBusinessProfile() {
        viewModelScope.launch {
            _registrationState.value = NetworkResponse.Loading()
            when (val result = fetchBusinessInfoUseCase().toResource()) {
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
