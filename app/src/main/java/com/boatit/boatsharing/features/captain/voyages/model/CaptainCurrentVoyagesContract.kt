package com.boatit.boatsharing.features.captain.voyages.model

import com.boatit.boatsharing.core.presentation.UiEffect
import com.boatit.boatsharing.core.presentation.UiEvent
import com.boatit.boatsharing.core.presentation.UiState
import com.boatit.boatsharing.features.captain.dashboard.model.VoyageData

data class CaptainCurrentVoyagesUiState(
    val selectedTabIndex: Int = 0,
    val pending: List<VoyageData> = emptyList(),
    val accepted: List<VoyageData> = emptyList(),
    val started: List<VoyageData> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
) : UiState

sealed interface CaptainCurrentVoyagesUiEvent : UiEvent {
    data object Initialize : CaptainCurrentVoyagesUiEvent

    data object RefreshVoyages : CaptainCurrentVoyagesUiEvent

    data class SelectTab(val index: Int) : CaptainCurrentVoyagesUiEvent
}

sealed interface CaptainCurrentVoyagesUiEffect : UiEffect {
    data class ShowToast(val message: String) : CaptainCurrentVoyagesUiEffect
}
