package com.boatit.boatsharing.features.voyager.dashboard.viewmodel

import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.core.presentation.BaseViewModel
import com.boatit.boatsharing.core.presentation.UiEffect
import com.boatit.boatsharing.core.presentation.UiEvent
import com.boatit.boatsharing.core.presentation.UiState
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.features.voyager.dashboard.domain.usecase.CancelBookedVoyageUseCase
import com.boatit.boatsharing.features.voyager.dashboard.model.CancelBookedVoyageResponse
import com.boatit.boatsharing.features.voyager.dashboard.model.CancelBookedVoyages
import kotlinx.coroutines.launch

data class CancelBookedVoyageUiState(
    val nearbyPlaces: NetworkResponse<CancelBookedVoyageResponse> = NetworkResponse.Loading(),
) : UiState

sealed interface CancelBookedVoyageUiEvent : UiEvent {
    data class Fetch(val profile: CancelBookedVoyages) : CancelBookedVoyageUiEvent

    data object Reset : CancelBookedVoyageUiEvent
}

sealed interface CancelBookedVoyageUiEffect : UiEffect {
    data object NoOpEffect : CancelBookedVoyageUiEffect
}

class CancelBookedVoyageViewModel(
    private val cancelBookedVoyageUseCase: CancelBookedVoyageUseCase,
) : BaseViewModel<CancelBookedVoyageUiState, CancelBookedVoyageUiEvent, CancelBookedVoyageUiEffect>(
        CancelBookedVoyageUiState(),
    ) {
    override fun onEvent(event: CancelBookedVoyageUiEvent) {
        when (event) {
            is CancelBookedVoyageUiEvent.Fetch -> fetchNearbyPlaces(event.profile)
            CancelBookedVoyageUiEvent.Reset -> resetNearbyPlaces()
        }
    }

    fun fetchNearbyPlaces(profile: CancelBookedVoyages) =
        viewModelScope.launch {
            updateState { copy(nearbyPlaces = NetworkResponse.Loading()) }
            when (val result = cancelBookedVoyageUseCase(profile).toResource()) {
                is Resource.Success -> {
                    updateState { copy(nearbyPlaces = NetworkResponse.Success(result.data)) }
                    resetNearbyPlaces()
                }

                is Resource.Error -> {
                    updateState { copy(nearbyPlaces = NetworkResponse.Error(result.error)) }
                }

                Resource.Loading -> {
                    updateState { copy(nearbyPlaces = NetworkResponse.Loading()) }
                }
            }
        }

    fun resetNearbyPlaces() {
        updateState { copy(nearbyPlaces = NetworkResponse.Loading()) }
    }
}
