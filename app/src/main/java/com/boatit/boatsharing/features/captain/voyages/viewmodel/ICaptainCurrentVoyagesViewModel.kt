package com.boatit.boatsharing.features.captain.voyages.viewmodel

import com.boatit.boatsharing.features.captain.voyages.model.CaptainCurrentVoyagesUiEffect
import com.boatit.boatsharing.features.captain.voyages.model.CaptainCurrentVoyagesUiEvent
import com.boatit.boatsharing.features.captain.voyages.model.CaptainCurrentVoyagesUiState
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface ICaptainCurrentVoyagesViewModel {
    val uiState: StateFlow<CaptainCurrentVoyagesUiState>
    val uiEffects: SharedFlow<CaptainCurrentVoyagesUiEffect>

    fun onEvent(event: CaptainCurrentVoyagesUiEvent)

    fun voyages()

    fun resetNearbyPlaces()
}
