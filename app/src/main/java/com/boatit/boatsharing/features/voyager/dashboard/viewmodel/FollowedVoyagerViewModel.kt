package com.boatit.boatsharing.features.voyager.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.features.voyager.dashboard.domain.usecase.FetchFollowedVoyagersUseCase
import com.boatit.boatsharing.features.voyager.dashboard.model.FollowedVoyagersResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FollowedVoyagerViewModel(private val fetchFollowedVoyagersUseCase: FetchFollowedVoyagersUseCase) : com.boatit.boatsharing.core.presentation.LegacyMviViewModel() {
    private val _registrationState = MutableStateFlow<NetworkResponse<FollowedVoyagersResponse>>(NetworkResponse.Loading())
    val registrationState: StateFlow<NetworkResponse<FollowedVoyagersResponse>> = _registrationState

    fun FollowedVoyagerFunc() {
        viewModelScope.launch {
            _registrationState.value = NetworkResponse.Loading()
            when (val result = fetchFollowedVoyagersUseCase().toResource()) {
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
