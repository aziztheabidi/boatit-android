package com.boatit.boatsharing.features.captain.voyages.model

import com.boatit.boatsharing.features.captain.dashboard.model.VoyageData

data class CaptainCurrentVoyagesUiState(
    val selectedTabIndex: Int = 0,
    val pending: List<VoyageData> = emptyList(),
    val accepted: List<VoyageData> = emptyList(),
    val started: List<VoyageData> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
)

sealed interface CaptainCurrentVoyagesUiEvent {
    data object Initialize : CaptainCurrentVoyagesUiEvent

    data object RefreshVoyages : CaptainCurrentVoyagesUiEvent

    data class SelectTab(val index: Int) : CaptainCurrentVoyagesUiEvent
}

sealed interface CaptainCurrentVoyagesUiEffect {
    data class ShowToast(val message: String) : CaptainCurrentVoyagesUiEffect
}
