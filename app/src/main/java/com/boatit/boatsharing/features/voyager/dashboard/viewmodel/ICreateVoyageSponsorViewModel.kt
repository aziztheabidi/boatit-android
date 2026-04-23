package com.boatit.boatsharing.features.voyager.dashboard.viewmodel

import com.boatit.boatsharing.features.voyager.dashboard.model.CreateVoyageSponsorUiEffect
import com.boatit.boatsharing.features.voyager.dashboard.model.CreateVoyageSponsorUiEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface ICreateVoyageSponsorViewModel {
    val uiState: StateFlow<CreateVoyageSponsorUiState>
    val uiEffect: Flow<CreateVoyageSponsorUiEffect>

    fun onEvent(event: CreateVoyageSponsorUiEvent)
}
