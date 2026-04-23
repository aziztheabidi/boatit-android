package com.boatit.boatsharing.features.voyager.dashboard.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.core.presentation.BaseViewModel
import com.boatit.boatsharing.core.presentation.UiEffect
import com.boatit.boatsharing.core.presentation.UiEvent
import com.boatit.boatsharing.core.presentation.UiState
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.features.voyager.dashboard.domain.usecase.FetchActiveVoyageUseCase
import com.boatit.boatsharing.features.voyager.dashboard.model.ActiveVoyageResponse
import kotlinx.coroutines.launch

data class GetActiveVoyageUiState(
    val voyageResult: NetworkResponse<ActiveVoyageResponse> = NetworkResponse.Loading(),
) : UiState

sealed interface GetActiveVoyageUiEvent : UiEvent {
    data object FetchActiveVoyage : GetActiveVoyageUiEvent

    data object Reset : GetActiveVoyageUiEvent
}

sealed interface GetActiveVoyageUiEffect : UiEffect {
    data object NoOpEffect : GetActiveVoyageUiEffect
}

class GetActiveVoyageViewModel(
    private val fetchActiveVoyageUseCase: FetchActiveVoyageUseCase,
) : BaseViewModel<GetActiveVoyageUiState, GetActiveVoyageUiEvent, GetActiveVoyageUiEffect>(
        GetActiveVoyageUiState(),
    ) {
    override fun onEvent(event: GetActiveVoyageUiEvent) {
        when (event) {
            GetActiveVoyageUiEvent.FetchActiveVoyage -> voyages()
            GetActiveVoyageUiEvent.Reset -> resetNearbyPlaces()
        }
    }

    private fun voyages() {
        viewModelScope.launch {
            updateState { copy(voyageResult = NetworkResponse.Loading()) }
            when (val result = fetchActiveVoyageUseCase().toResource()) {
                is Resource.Success -> {
                    Log.e("popup_res", result.data.toString())
                    updateState { copy(voyageResult = NetworkResponse.Success(result.data)) }
                }

                is Resource.Error -> {
                    val message = result.error.toMessage()
                    Log.e("popup_res_err", message)
                    updateState { copy(voyageResult = NetworkResponse.Error(result.error)) }
                }

                Resource.Loading -> {
                    updateState { copy(voyageResult = NetworkResponse.Loading()) }
                }
            }
        }
    }

    private fun resetNearbyPlaces() {
        updateState { copy(voyageResult = NetworkResponse.Loading()) }
    }
}
