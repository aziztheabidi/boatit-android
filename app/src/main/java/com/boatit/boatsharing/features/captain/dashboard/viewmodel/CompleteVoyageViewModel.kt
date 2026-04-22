package com.boatit.boatsharing.features.captain.dashboard.viewmodel

import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.core.presentation.BaseViewModel
import com.boatit.boatsharing.core.presentation.UiEffect
import com.boatit.boatsharing.core.presentation.UiEvent
import com.boatit.boatsharing.core.presentation.UiState
import com.boatit.boatsharing.features.captain.dashboard.model.VoyageCompleteRequest
import com.boatit.boatsharing.features.captain.domain.usecase.CompleteVoyageUseCase
import kotlinx.coroutines.launch

data class CompleteVoyageUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
) : UiState

sealed interface CompleteVoyageUiEvent : UiEvent {
    data class Submit(val request: VoyageCompleteRequest) : CompleteVoyageUiEvent
}

sealed interface CompleteVoyageUiEffect : UiEffect {
    data class ShowToast(val message: String) : CompleteVoyageUiEffect

    data object NavigateToFeedback : CompleteVoyageUiEffect
}

class CompleteVoyageViewModel(
    private val completeVoyageUseCase: CompleteVoyageUseCase,
) : BaseViewModel<CompleteVoyageUiState, CompleteVoyageUiEvent, CompleteVoyageUiEffect>(CompleteVoyageUiState()) {
    override fun onEvent(event: CompleteVoyageUiEvent) {
        when (event) {
            is CompleteVoyageUiEvent.Submit -> submit(event.request)
        }
    }

    fun completeVoyage(request: VoyageCompleteRequest) {
        onEvent(CompleteVoyageUiEvent.Submit(request))
    }

    @Deprecated("Use completeVoyage")
    fun startvoyage(request: VoyageCompleteRequest) {
        completeVoyage(request)
    }

    private fun submit(request: VoyageCompleteRequest) {
        if (currentState.isLoading) return

        viewModelScope.launch {
            updateState { copy(isLoading = true, errorMessage = null) }

            when (val result = completeVoyageUseCase(request).toResource()) {
                is Resource.Success -> {
                    updateState { copy(isLoading = false, errorMessage = null) }
                    emitEffect(CompleteVoyageUiEffect.ShowToast("Voyage Successfully Completed"))
                    emitEffect(CompleteVoyageUiEffect.NavigateToFeedback)
                }

                is Resource.Error -> {
                    val message = result.error.toMessage()
                    updateState { copy(isLoading = false, errorMessage = message) }
                    emitEffect(CompleteVoyageUiEffect.ShowToast(message))
                }

                Resource.Loading -> {
                    updateState { copy(isLoading = true) }
                }
            }
        }
    }
}
