package com.boatit.boatsharing.features.voyager.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.features.voyager.dashboard.domain.usecase.FetchFutureVoyagesUseCase
import com.boatit.boatsharing.features.voyager.dashboard.model.FutureBookedVoyages
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FutureVoyagesViewModel(private val fetchFutureVoyagesUseCase: FetchFutureVoyagesUseCase) : com.boatit.boatsharing.core.presentation.LegacyMviViewModel() {
    private val _loginState = MutableStateFlow<NetworkResponse<FutureBookedVoyages>>(NetworkResponse.Loading())
    val loginState: StateFlow<NetworkResponse<FutureBookedVoyages>> = _loginState

    fun voyages() {
        viewModelScope.launch {
            _loginState.value = NetworkResponse.Loading()
            when (val result = fetchFutureVoyagesUseCase().toResource()) {
                is Resource.Success -> {
                    _loginState.value = NetworkResponse.Success(result.data)
                }

                is Resource.Error -> {
                    _loginState.value = NetworkResponse.Error(result.error)
                }

                Resource.Loading -> {
                    _loginState.value = NetworkResponse.Loading()
                }
            }
        }
    }

    fun resetNearbyPlaces() {
        _loginState.value = NetworkResponse.Loading()
    }
}
