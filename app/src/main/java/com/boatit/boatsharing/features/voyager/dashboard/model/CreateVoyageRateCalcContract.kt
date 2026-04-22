package com.boatit.boatsharing.features.voyager.dashboard.model

import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.CreateVoyageRateCalcUiState

sealed interface CreateVoyageRateCalcUiEvent {
    data object Initialize : CreateVoyageRateCalcUiEvent

    data class EventNameChanged(val value: String) : CreateVoyageRateCalcUiEvent

    data class SplitPaymentToggled(val enabled: Boolean) : CreateVoyageRateCalcUiEvent

    data object Proceed : CreateVoyageRateCalcUiEvent
}

sealed interface CreateVoyageRateCalcUiEffect {
    data class NavigateToSponsor(val splitPayment: Boolean) : CreateVoyageRateCalcUiEffect
}

typealias CreateVoyageRateCalcContractState = CreateVoyageRateCalcUiState
