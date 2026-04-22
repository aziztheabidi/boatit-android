package com.boatit.boatsharing.features.voyager.dashboard.viewmodel

import com.boatit.boatsharing.features.voyager.dashboard.model.CreateVoyageSponsorUiEvent
import kotlinx.coroutines.flow.StateFlow

interface ICreateVoyageSponsorViewModel {
    val uiState: StateFlow<CreateVoyageSponsorUiState>

    fun onEvent(event: CreateVoyageSponsorUiEvent)
}
