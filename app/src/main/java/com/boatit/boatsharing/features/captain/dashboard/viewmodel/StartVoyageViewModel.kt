package com.boatit.boatsharing.features.captain.dashboard.viewmodel

import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.core.presentation.BaseViewModel
import com.boatit.boatsharing.core.presentation.UiEffect
import com.boatit.boatsharing.core.presentation.UiEvent
import com.boatit.boatsharing.core.presentation.UiState
import com.boatit.boatsharing.features.captain.dashboard.model.VoyageStartRequest
import com.boatit.boatsharing.features.captain.domain.usecase.StartVoyageUseCase
import kotlinx.coroutines.launch

data class StartVoyageUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
) : UiState

sealed interface StartVoyageUiEvent : UiEvent {
    data class Submit(val request: VoyageStartRequest) : StartVoyageUiEvent
}

sealed interface StartVoyageUiEffect : UiEffect {
    data class ShowToast(val message: String) : StartVoyageUiEffect

    data object RefreshActiveVoyages : StartVoyageUiEffect
}

class StartVoyageViewModel(
    private val startVoyageUseCase: StartVoyageUseCase,
) : BaseViewModel<StartVoyageUiState, StartVoyageUiEvent, StartVoyageUiEffect>(StartVoyageUiState()) {
    override fun onEvent(event: StartVoyageUiEvent) {
        when (event) {
            is StartVoyageUiEvent.Submit -> submit(event.request)
        }
    }

    fun startVoyage(request: VoyageStartRequest) {
        onEvent(StartVoyageUiEvent.Submit(request))
    }

    @Deprecated("Use startVoyage")
    fun startvoyage(request: VoyageStartRequest) {
        startVoyage(request)
    }

    private fun submit(request: VoyageStartRequest) {
        if (currentState.isLoading) return

        viewModelScope.launch {
            updateState { copy(isLoading = true, errorMessage = null) }

            when (val result = startVoyageUseCase(request).toResource()) {
                is Resource.Success -> {
                    val message = result.data.Message.ifBlank { "Voyage Started." }
                    updateState { copy(isLoading = false, errorMessage = null) }
                    emitEffect(StartVoyageUiEffect.ShowToast(message))
                    emitEffect(StartVoyageUiEffect.RefreshActiveVoyages)
                }

                is Resource.Error -> {
                    val message = result.error.toMessage()
                    updateState { copy(isLoading = false, errorMessage = message) }
                    emitEffect(StartVoyageUiEffect.ShowToast(message))
                }

                Resource.Loading -> {
                    updateState { copy(isLoading = true) }
                }
            }
        }
    }

    fun resetUiState() {
        updateState { copy(isLoading = false, errorMessage = null) }
    }

    @Deprecated("Use resetUiState")
    fun resetNearbyPlaces() {
        resetUiState()
    }
}
