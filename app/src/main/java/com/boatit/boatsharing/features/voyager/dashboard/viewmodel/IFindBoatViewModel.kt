package com.boatit.boatsharing.features.voyager.dashboard.viewmodel

import com.boatit.boatsharing.features.voyager.dashboard.model.FindBoatUiEffect
import com.boatit.boatsharing.features.voyager.dashboard.model.FindBoatUiEvent
import com.boatit.boatsharing.features.voyager.dashboard.model.FindBoatUiState
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface IFindBoatViewModel {
    val uiState: StateFlow<FindBoatUiState>
    val uiEffects: SharedFlow<FindBoatUiEffect>

    fun onEvent(event: FindBoatUiEvent)
}
