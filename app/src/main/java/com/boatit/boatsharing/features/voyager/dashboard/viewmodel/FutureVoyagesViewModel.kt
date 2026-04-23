package com.boatit.boatsharing.features.voyager.dashboard.viewmodel

import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.core.presentation.BaseViewModel
import com.boatit.boatsharing.core.presentation.UiEffect
import com.boatit.boatsharing.core.presentation.UiEvent
import com.boatit.boatsharing.core.presentation.UiState
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.features.voyager.dashboard.domain.usecase.FetchFutureVoyagesUseCase
import com.boatit.boatsharing.features.voyager.dashboard.model.FutureBookedVoyages
import kotlinx.coroutines.launch

data class FutureVoyagesUiState(
    val voyagesResult: NetworkResponse<FutureBookedVoyages> = NetworkResponse.Loading(),
) : UiState

sealed interface FutureVoyagesUiEvent : UiEvent {
    data object FetchVoyages : FutureVoyagesUiEvent

    data object Reset : FutureVoyagesUiEvent
}

sealed interface FutureVoyagesUiEffect : UiEffect {
    data object NoOpEffect : FutureVoyagesUiEffect
}

class FutureVoyagesViewModel(
    private val fetchFutureVoyagesUseCase: FetchFutureVoyagesUseCase,
) : BaseViewModel<FutureVoyagesUiState, FutureVoyagesUiEvent, FutureVoyagesUiEffect>(
        FutureVoyagesUiState(),
    ) {
    override fun onEvent(event: FutureVoyagesUiEvent) {
        when (event) {
            FutureVoyagesUiEvent.FetchVoyages -> voyages()
            FutureVoyagesUiEvent.Reset -> resetNearbyPlaces()
        }
    }

    private fun voyages() {
        viewModelScope.launch {
            updateState { copy(voyagesResult = NetworkResponse.Loading()) }
            when (val result = fetchFutureVoyagesUseCase().toResource()) {
                is Resource.Success -> {
                    updateState { copy(voyagesResult = NetworkResponse.Success(result.data)) }
                }

                is Resource.Error -> {
                    updateState { copy(voyagesResult = NetworkResponse.Error(result.error)) }
                }

                Resource.Loading -> {
                    updateState { copy(voyagesResult = NetworkResponse.Loading()) }
                }
            }
        }
    }

    private fun resetNearbyPlaces() {
        updateState { copy(voyagesResult = NetworkResponse.Loading()) }
    }
}
