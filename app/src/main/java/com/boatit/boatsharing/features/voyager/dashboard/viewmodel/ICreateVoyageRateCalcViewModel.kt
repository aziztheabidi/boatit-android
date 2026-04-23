package com.boatit.boatsharing.features.voyager.dashboard.viewmodel

import com.boatit.boatsharing.features.voyager.dashboard.model.CreateVoyageRateCalcUiEffect
import com.boatit.boatsharing.features.voyager.dashboard.model.CreateVoyageRateCalcUiEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface ICreateVoyageRateCalcViewModel {
    val uiState: StateFlow<CreateVoyageRateCalcUiState>
    val uiEffect: Flow<CreateVoyageRateCalcUiEffect>

    fun onEvent(event: CreateVoyageRateCalcUiEvent)
}
