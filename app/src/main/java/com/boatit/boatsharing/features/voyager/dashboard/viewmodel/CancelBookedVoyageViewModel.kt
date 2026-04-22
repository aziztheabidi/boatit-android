package com.boatit.boatsharing.features.voyager.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.features.voyager.dashboard.domain.usecase.CancelBookedVoyageUseCase
import com.boatit.boatsharing.features.voyager.dashboard.model.CancelBookedVoyageResponse
import com.boatit.boatsharing.features.voyager.dashboard.model.CancelBookedVoyages
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CancelBookedVoyageViewModel(
    private val cancelBookedVoyageUseCase: CancelBookedVoyageUseCase,
) : com.boatit.boatsharing.core.presentation.LegacyMviViewModel() {
    private val _nearbyPlaces = MutableStateFlow<NetworkResponse<CancelBookedVoyageResponse>>(NetworkResponse.Loading())
    val nearbyPlaces: StateFlow<NetworkResponse<CancelBookedVoyageResponse>> = _nearbyPlaces.asStateFlow()

    fun fetchNearbyPlaces(profile: CancelBookedVoyages) =
        viewModelScope.launch {
            _nearbyPlaces.value = NetworkResponse.Loading()
            when (val result = cancelBookedVoyageUseCase(profile).toResource()) {
                is Resource.Success -> {
                    _nearbyPlaces.value = NetworkResponse.Success(result.data)
                    resetNearbyPlaces()
                }

                is Resource.Error -> {
                    _nearbyPlaces.value = NetworkResponse.Error(result.error)
                }

                Resource.Loading -> {
                    _nearbyPlaces.value = NetworkResponse.Loading()
                }
            }
        }

    fun resetNearbyPlaces() {
        _nearbyPlaces.value = NetworkResponse.Loading()
    }
}
