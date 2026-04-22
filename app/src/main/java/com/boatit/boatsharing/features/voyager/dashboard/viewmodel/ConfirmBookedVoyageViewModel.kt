package com.boatit.boatsharing.features.voyager.dashboard.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.features.voyager.dashboard.domain.usecase.ConfirmBookedVoyageUseCase
import com.boatit.boatsharing.features.voyager.dashboard.model.ConfirmBookedVoyageResponse
import com.boatit.boatsharing.features.voyager.dashboard.model.ConfirmBookedVoyages
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ConfirmBookedVoyageViewModel(
    private val confirmBookedVoyageUseCase: ConfirmBookedVoyageUseCase,
) : com.boatit.boatsharing.core.presentation.LegacyMviViewModel() {
    private val _confirmationState = MutableStateFlow<NetworkResponse<ConfirmBookedVoyageResponse>>(NetworkResponse.Loading())
    val confirmationState: StateFlow<NetworkResponse<ConfirmBookedVoyageResponse>> = _confirmationState.asStateFlow()

    @Deprecated("Use confirmationState")
    val nearbyPlaces: StateFlow<NetworkResponse<ConfirmBookedVoyageResponse>> = confirmationState

    fun submitConfirmation(request: ConfirmBookedVoyages) =
        viewModelScope.launch {
            _confirmationState.value = NetworkResponse.Loading()
            when (val result = confirmBookedVoyageUseCase(request).toResource()) {
                is Resource.Success -> {
                    _confirmationState.value = NetworkResponse.Success(result.data)
                }

                is Resource.Error -> {
                    val mapped = mapConfirmBookedVoyageError(result.error)
                    runCatching {
                        Log.e("viewModel", "Error fetching places: ${mapped.toMessage()}")
                    }
                    _confirmationState.value = NetworkResponse.Error(mapped)
                }

                Resource.Loading -> {
                    _confirmationState.value = NetworkResponse.Loading()
                }
            }
        }

    @Deprecated("Use submitConfirmation")
    fun fetchNearbyPlaces(profile: ConfirmBookedVoyages) {
        submitConfirmation(profile)
    }

    fun resetConfirmationState() {
        _confirmationState.value = NetworkResponse.Loading()
    }

    @Deprecated("Use resetConfirmationState")
    fun resetNearbyPlaces() {
        resetConfirmationState()
    }
}
