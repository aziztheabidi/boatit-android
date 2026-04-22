package com.boatit.boatsharing.features.voyager.dashboard.model

import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.CreateVoyageUiState

sealed interface CreateVoyageUiEvent {
    data class DobChanged(val value: String) : CreateVoyageUiEvent

    data class StartTimeChanged(val value: String) : CreateVoyageUiEvent

    data class EndTimeChanged(val value: String) : CreateVoyageUiEvent

    data class TravelNowToggled(val enabled: Boolean) : CreateVoyageUiEvent

    data class SpendTimeToggled(val enabled: Boolean) : CreateVoyageUiEvent

    data class ShowDatePicker(val show: Boolean) : CreateVoyageUiEvent

    data class ShowStartTimePicker(val show: Boolean) : CreateVoyageUiEvent

    data class ShowEndTimePicker(val show: Boolean) : CreateVoyageUiEvent

    data object ClearError : CreateVoyageUiEvent

    data object CalculateFare : CreateVoyageUiEvent

    data object ResetRequestState : CreateVoyageUiEvent
}

sealed interface CreateVoyageUiEffect {
    data object NavigateToRateCalculation : CreateVoyageUiEffect
}

typealias CreateVoyageContractState = CreateVoyageUiState
