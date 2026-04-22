package com.boatit.boatsharing.features.captain.dashboard.viewmodel

import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.core.presentation.BaseViewModel
import com.boatit.boatsharing.core.presentation.UiEffect
import com.boatit.boatsharing.core.presentation.UiEvent
import com.boatit.boatsharing.core.presentation.UiState
import com.boatit.boatsharing.features.captain.dashboard.model.CaptainFeedbackRequest
import com.boatit.boatsharing.features.captain.domain.usecase.SubmitCaptainFeedbackUseCase
import kotlinx.coroutines.launch

data class CaptainFeedbackUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
) : UiState

sealed interface CaptainFeedbackUiEvent : UiEvent {
    data class Submit(val request: CaptainFeedbackRequest) : CaptainFeedbackUiEvent
}

sealed interface CaptainFeedbackUiEffect : UiEffect {
    data class ShowToast(val message: String) : CaptainFeedbackUiEffect

    data object NavigateToDashboard : CaptainFeedbackUiEffect
}

class CaptainFeedbackViewModel(
    private val submitCaptainFeedbackUseCase: SubmitCaptainFeedbackUseCase,
) : BaseViewModel<CaptainFeedbackUiState, CaptainFeedbackUiEvent, CaptainFeedbackUiEffect>(CaptainFeedbackUiState()) {
    override fun onEvent(event: CaptainFeedbackUiEvent) {
        when (event) {
            is CaptainFeedbackUiEvent.Submit -> submit(event.request)
        }
    }

    fun captainFeedbackFunc(request: CaptainFeedbackRequest) {
        onEvent(CaptainFeedbackUiEvent.Submit(request))
    }

    private fun submit(request: CaptainFeedbackRequest) {
        if (currentState.isLoading) return

        viewModelScope.launch {
            updateState { copy(isLoading = true, errorMessage = null) }

            when (val resource = submitCaptainFeedbackUseCase(request).toResource()) {
                is Resource.Success -> {
                    updateState { copy(isLoading = false, errorMessage = null) }
                    emitEffect(CaptainFeedbackUiEffect.ShowToast("Feedback Submitted"))
                    emitEffect(CaptainFeedbackUiEffect.NavigateToDashboard)
                }

                is Resource.Error -> {
                    val message = resource.error.toMessage()
                    updateState { copy(isLoading = false, errorMessage = message) }
                    emitEffect(CaptainFeedbackUiEffect.ShowToast(message))
                }

                Resource.Loading -> {
                    updateState { copy(isLoading = true) }
                }
            }
        }
    }
}
