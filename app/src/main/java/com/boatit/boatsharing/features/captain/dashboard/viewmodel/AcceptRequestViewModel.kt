package com.boatit.boatsharing.features.captain.dashboard.viewmodel

import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.core.presentation.BaseViewModel
import com.boatit.boatsharing.core.presentation.UiEffect
import com.boatit.boatsharing.core.presentation.UiEvent
import com.boatit.boatsharing.core.presentation.UiState
import com.boatit.boatsharing.features.captain.dashboard.model.AcceptVoyageRequest
import com.boatit.boatsharing.features.captain.domain.usecase.AcceptVoyageUseCase
import com.boatit.boatsharing.features.captain.domain.usecase.DeclineVoyageUseCase
import kotlinx.coroutines.launch

data class AcceptRequestUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
) : UiState

sealed interface AcceptRequestUiEvent : UiEvent {
    data class Accept(val request: AcceptVoyageRequest) : AcceptRequestUiEvent

    data class Decline(val request: AcceptVoyageRequest) : AcceptRequestUiEvent
}

sealed interface AcceptRequestUiEffect : UiEffect {
    data class Accepted(val voyageId: String, val message: String) : AcceptRequestUiEffect

    data class Declined(val voyageId: String, val message: String) : AcceptRequestUiEffect

    data class ShowToast(val message: String) : AcceptRequestUiEffect
}

class AcceptRequestViewModel(
    private val acceptVoyageUseCase: AcceptVoyageUseCase,
    private val declineVoyageUseCase: DeclineVoyageUseCase,
) : BaseViewModel<AcceptRequestUiState, AcceptRequestUiEvent, AcceptRequestUiEffect>(AcceptRequestUiState()) {
    override fun onEvent(event: AcceptRequestUiEvent) {
        when (event) {
            is AcceptRequestUiEvent.Accept -> accept(event.request)
            is AcceptRequestUiEvent.Decline -> decline(event.request)
        }
    }

    fun accept(request: AcceptVoyageRequest) {
        if (currentState.isLoading) return

        viewModelScope.launch {
            updateState { copy(isLoading = true, errorMessage = null) }

            when (val result = acceptVoyageUseCase(request).toResource()) {
                is Resource.Success -> {
                    updateState { copy(isLoading = false, errorMessage = null) }
                    emitEffect(
                        AcceptRequestUiEffect.Accepted(
                            voyageId = request.Id,
                            message = result.data.Message.ifBlank { "Voyage Accepted. Waiting For Payment" },
                        ),
                    )
                }

                is Resource.Error -> {
                    val message = result.error.toMessage()
                    updateState { copy(isLoading = false, errorMessage = message) }
                    emitEffect(AcceptRequestUiEffect.ShowToast(message))
                }

                Resource.Loading -> {
                    updateState { copy(isLoading = true) }
                }
            }
        }
    }

    fun decline(request: AcceptVoyageRequest) {
        if (currentState.isLoading) return

        viewModelScope.launch {
            updateState { copy(isLoading = true, errorMessage = null) }

            when (val result = declineVoyageUseCase(request).toResource()) {
                is Resource.Success -> {
                    updateState { copy(isLoading = false, errorMessage = null) }
                    emitEffect(
                        AcceptRequestUiEffect.Declined(
                            voyageId = request.Id,
                            message = result.data.Message.ifBlank { "Voyage declined" },
                        ),
                    )
                }

                is Resource.Error -> {
                    val message = result.error.toMessage()
                    updateState { copy(isLoading = false, errorMessage = message) }
                    emitEffect(AcceptRequestUiEffect.ShowToast(message))
                }

                Resource.Loading -> {
                    updateState { copy(isLoading = true) }
                }
            }
        }
    }

    fun resetNearbyPlaces() {
        updateState { copy(isLoading = false, errorMessage = null) }
    }
}
