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
import com.boatit.boatsharing.features.voyager.dashboard.domain.usecase.ConfirmBookedVoyageUseCase
import com.boatit.boatsharing.features.voyager.dashboard.model.ConfirmBookedVoyageResponse
import com.boatit.boatsharing.features.voyager.dashboard.model.ConfirmBookedVoyages
import kotlinx.coroutines.launch

data class ConfirmBookedVoyageUiState(
    val confirmationState: NetworkResponse<ConfirmBookedVoyageResponse> = NetworkResponse.Loading(),
) : UiState

sealed interface ConfirmBookedVoyageUiEvent : UiEvent {
    data class Submit(val request: ConfirmBookedVoyages) : ConfirmBookedVoyageUiEvent

    data object Reset : ConfirmBookedVoyageUiEvent
}

sealed interface ConfirmBookedVoyageUiEffect : UiEffect {
    data object NoOpEffect : ConfirmBookedVoyageUiEffect
}

class ConfirmBookedVoyageViewModel(
    private val confirmBookedVoyageUseCase: ConfirmBookedVoyageUseCase,
) : BaseViewModel<ConfirmBookedVoyageUiState, ConfirmBookedVoyageUiEvent, ConfirmBookedVoyageUiEffect>(
        ConfirmBookedVoyageUiState(),
    ) {
    override fun onEvent(event: ConfirmBookedVoyageUiEvent) {
        when (event) {
            is ConfirmBookedVoyageUiEvent.Submit -> submitConfirmation(event.request)
            ConfirmBookedVoyageUiEvent.Reset -> resetConfirmationState()
        }
    }

    fun submitConfirmation(request: ConfirmBookedVoyages) =
        viewModelScope.launch {
            updateState { copy(confirmationState = NetworkResponse.Loading()) }
            when (val result = confirmBookedVoyageUseCase(request).toResource()) {
                is Resource.Success -> {
                    updateState { copy(confirmationState = NetworkResponse.Success(result.data)) }
                }

                is Resource.Error -> {
                    val mapped = mapConfirmBookedVoyageError(result.error)
                    runCatching {
                        Log.e("viewModel", "Error fetching places: ${mapped.toMessage()}")
                    }
                    updateState { copy(confirmationState = NetworkResponse.Error(mapped)) }
                }

                Resource.Loading -> {
                    updateState { copy(confirmationState = NetworkResponse.Loading()) }
                }
            }
        }

    @Deprecated("Use submitConfirmation")
    fun fetchNearbyPlaces(profile: ConfirmBookedVoyages) {
        submitConfirmation(profile)
    }

    fun resetConfirmationState() {
        updateState { copy(confirmationState = NetworkResponse.Loading()) }
    }

    @Deprecated("Use resetConfirmationState")
    fun resetNearbyPlaces() {
        resetConfirmationState()
    }
}
