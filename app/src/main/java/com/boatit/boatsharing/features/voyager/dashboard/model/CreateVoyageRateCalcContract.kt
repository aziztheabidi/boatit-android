package com.boatit.boatsharing.features.voyager.dashboard.model

import com.boatit.boatsharing.core.presentation.UiEffect
import com.boatit.boatsharing.core.presentation.UiEvent
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.CreateVoyageRateCalcUiState

sealed interface CreateVoyageRateCalcUiEvent : UiEvent {
    data object Initialize : CreateVoyageRateCalcUiEvent

    data class EventNameChanged(val value: String) : CreateVoyageRateCalcUiEvent

    data class SplitPaymentToggled(val enabled: Boolean) : CreateVoyageRateCalcUiEvent

    data object Proceed : CreateVoyageRateCalcUiEvent
}

sealed interface CreateVoyageRateCalcUiEffect : UiEffect {
    data class NavigateToSponsor(val splitPayment: Boolean) : CreateVoyageRateCalcUiEffect
}

typealias CreateVoyageRateCalcContractState = CreateVoyageRateCalcUiState
