package com.boatit.boatsharing.features.captain.dashboard.viewmodel

import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.core.presentation.BaseViewModel
import com.boatit.boatsharing.core.presentation.UiEffect
import com.boatit.boatsharing.core.presentation.UiEvent
import com.boatit.boatsharing.core.presentation.UiState
import com.boatit.boatsharing.features.captain.dashboard.model.VoyageCompleteRequest
import com.boatit.boatsharing.features.captain.domain.usecase.CancelVoyageUseCase
import kotlinx.coroutines.launch

data class CancelVoyageUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
) : UiState

sealed interface CancelVoyageUiEvent : UiEvent {
    data class Submit(val request: VoyageCompleteRequest) : CancelVoyageUiEvent
}

sealed interface CancelVoyageUiEffect : UiEffect {
    data class ShowToast(val message: String) : CancelVoyageUiEffect

    data object RefreshActiveVoyages : CancelVoyageUiEffect
}

class CancelVoyageViewModel(
    private val cancelVoyageUseCase: CancelVoyageUseCase,
) : BaseViewModel<CancelVoyageUiState, CancelVoyageUiEvent, CancelVoyageUiEffect>(CancelVoyageUiState()) {
    override fun onEvent(event: CancelVoyageUiEvent) {
        when (event) {
            is CancelVoyageUiEvent.Submit -> submit(event.request)
        }
    }

    fun cancelVoyage(request: VoyageCompleteRequest) {
        onEvent(CancelVoyageUiEvent.Submit(request))
    }

    @Deprecated("Use cancelVoyage")
    fun startvoyage(request: VoyageCompleteRequest) {
        cancelVoyage(request)
    }

    private fun submit(request: VoyageCompleteRequest) {
        if (currentState.isLoading) return

        viewModelScope.launch {
            updateState { copy(isLoading = true, errorMessage = null) }

            when (val result = cancelVoyageUseCase(request).toResource()) {
                is Resource.Success -> {
                    val message = result.data.Message.ifBlank { "Voyage cancelled." }
                    updateState { copy(isLoading = false, errorMessage = null) }
                    emitEffect(CancelVoyageUiEffect.ShowToast(message))
                    emitEffect(CancelVoyageUiEffect.RefreshActiveVoyages)
                }

                is Resource.Error -> {
                    val message = result.error.toMessage()
                    updateState { copy(isLoading = false, errorMessage = message) }
                    emitEffect(CancelVoyageUiEffect.ShowToast(message))
                }

                Resource.Loading -> {
                    updateState { copy(isLoading = true) }
                }
            }
        }
    }
}
