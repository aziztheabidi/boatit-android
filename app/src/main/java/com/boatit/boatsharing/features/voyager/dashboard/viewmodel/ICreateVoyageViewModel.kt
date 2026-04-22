package com.boatit.boatsharing.features.voyager.dashboard.viewmodel

import com.boatit.boatsharing.features.voyager.dashboard.model.CreateVoyageContractState
import com.boatit.boatsharing.features.voyager.dashboard.model.CreateVoyageUiEffect
import com.boatit.boatsharing.features.voyager.dashboard.model.CreateVoyageUiEvent
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface ICreateVoyageViewModel {
    val uiState: StateFlow<CreateVoyageContractState>
    val uiEffects: SharedFlow<CreateVoyageUiEffect>

    fun onEvent(event: CreateVoyageUiEvent)
}
