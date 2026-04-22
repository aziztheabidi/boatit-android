package com.boatit.boatsharing.features.captain.voyages.viewmodel

import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.core.presentation.BaseViewModel
import com.boatit.boatsharing.core.presentation.UiEffect
import com.boatit.boatsharing.core.presentation.UiEvent
import com.boatit.boatsharing.core.presentation.UiState
import com.boatit.boatsharing.features.captain.domain.model.CaptainCompletedVoyageDomainModel
import com.boatit.boatsharing.features.captain.domain.usecase.FetchCaptainCompletedVoyagesUseCase
import kotlinx.coroutines.launch

data class CaptainVoyagesUiState(
    val isLoading: Boolean = false,
    val voyages: List<CaptainCompletedVoyageDomainModel> = emptyList(),
    val errorMessage: String? = null,
) : UiState

sealed interface CaptainVoyagesUiEvent : UiEvent {
    data object Refresh : CaptainVoyagesUiEvent
}

sealed interface CaptainVoyagesUiEffect : UiEffect {
    data class ShowToast(val message: String) : CaptainVoyagesUiEffect
}

class CaptainVoyagesViewModel(
    private val fetchCaptainCompletedVoyagesUseCase: FetchCaptainCompletedVoyagesUseCase,
) : BaseViewModel<CaptainVoyagesUiState, CaptainVoyagesUiEvent, CaptainVoyagesUiEffect>(CaptainVoyagesUiState()) {
    override fun onEvent(event: CaptainVoyagesUiEvent) {
        when (event) {
            CaptainVoyagesUiEvent.Refresh -> voyages()
        }
    }

    fun voyages() {
        if (currentState.isLoading) return

        viewModelScope.launch {
            updateState { copy(isLoading = true, errorMessage = null) }

            when (val result = fetchCaptainCompletedVoyagesUseCase().toResource()) {
                is Resource.Success -> {
                    updateState {
                        copy(
                            isLoading = false,
                            voyages = result.data.voyages,
                            errorMessage = null,
                        )
                    }
                }

                is Resource.Error -> {
                    val message = result.error.toMessage()
                    updateState { copy(isLoading = false, errorMessage = message) }
                    emitEffect(CaptainVoyagesUiEffect.ShowToast(message))
                }

                Resource.Loading -> updateState { copy(isLoading = true) }
            }
        }
    }
}
