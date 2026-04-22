package com.boatit.boatsharing.features.voyager.dashboard.viewmodel

import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.core.presentation.BaseViewModel
import com.boatit.boatsharing.core.presentation.UiEffect
import com.boatit.boatsharing.core.presentation.UiEvent
import com.boatit.boatsharing.core.presentation.UiState
import com.boatit.boatsharing.features.voyager.dashboard.domain.model.SponsorVoyageDomainModel
import com.boatit.boatsharing.features.voyager.dashboard.domain.usecase.FetchSponsorPaymentsUseCase
import kotlinx.coroutines.launch

data class SponsorVoyagesUiState(
    val isLoading: Boolean = false,
    val voyages: List<SponsorVoyageDomainModel> = emptyList(),
    val errorMessage: String? = null,
) : UiState

sealed interface SponsorVoyagesUiEvent : UiEvent {
    data object Refresh : SponsorVoyagesUiEvent
}

sealed interface SponsorVoyagesUiEffect : UiEffect {
    data class ShowToast(val message: String) : SponsorVoyagesUiEffect
}

class SponsorVoyagesViewModel(
    private val fetchSponsorPaymentsUseCase: FetchSponsorPaymentsUseCase,
) : BaseViewModel<SponsorVoyagesUiState, SponsorVoyagesUiEvent, SponsorVoyagesUiEffect>(SponsorVoyagesUiState()) {
    override fun onEvent(event: SponsorVoyagesUiEvent) {
        when (event) {
            SponsorVoyagesUiEvent.Refresh -> voyages()
        }
    }

    fun voyages() {
        if (currentState.isLoading) return

        viewModelScope.launch {
            updateState { copy(isLoading = true, errorMessage = null) }

            when (val result = fetchSponsorPaymentsUseCase().toResource()) {
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
                    emitEffect(SponsorVoyagesUiEffect.ShowToast(message))
                }

                Resource.Loading -> {
                    updateState { copy(isLoading = true) }
                }
            }
        }
    }
}
