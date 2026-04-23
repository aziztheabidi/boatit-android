package com.boatit.boatsharing.features.voyager.dashboard.viewmodel

import com.boatit.boatsharing.core.presentation.BaseViewModel
import com.boatit.boatsharing.core.presentation.UiState
import com.boatit.boatsharing.features.voyager.dashboard.model.CreateVoyageRateCalcUiEffect
import com.boatit.boatsharing.features.voyager.dashboard.model.CreateVoyageRateCalcUiEvent
import com.boatit.boatsharing.features.voyager.dashboard.model.Sponsor

data class CreateVoyageRateCalcUiState(
    val eventName: String = "",
    val splitPaymentEnabled: Boolean = false,
    val bookingDate: String = "",
    val eventHours: String = "",
    val voyagerCount: String = "",
    val perHourRate: String = "",
    val estimatedCost: String = "",
    val pickup: String = "",
    val dropOff: String = "",
    val isTravelNow: Boolean = false,
) : UiState

class CreateVoyageRateCalcViewModel(
    private val draftStore: CreateVoyageDraftStore,
) : BaseViewModel<CreateVoyageRateCalcUiState, CreateVoyageRateCalcUiEvent, CreateVoyageRateCalcUiEffect>(
        CreateVoyageRateCalcUiState(),
    ),
    ICreateVoyageRateCalcViewModel {
    override fun onEvent(event: CreateVoyageRateCalcUiEvent) {
        when (event) {
            CreateVoyageRateCalcUiEvent.Initialize -> initializeState()
            is CreateVoyageRateCalcUiEvent.EventNameChanged -> {
                updateState { copy(eventName = event.value) }
            }
            is CreateVoyageRateCalcUiEvent.SplitPaymentToggled -> {
                updateState { copy(splitPaymentEnabled = event.enabled) }
            }
            CreateVoyageRateCalcUiEvent.Proceed -> proceedToSponsor()
        }
    }

    private fun initializeState() {
        val draft = draftStore.state.value
        updateState {
            copy(
                eventName = draft.eventName,
                splitPaymentEnabled = draft.splitPaymentEnabled,
                bookingDate = draft.bookingDate,
                eventHours = draft.durationInHours.toString(),
                voyagerCount = draft.noOfVoyagers.toString(),
                perHourRate = draft.perHourRate.toString(),
                estimatedCost = draft.estimatedCost.toString(),
                pickup = draft.pickupDockName,
                dropOff = draft.dropOffDockName,
                isTravelNow = draft.isImmediately,
            )
        }
    }

    private fun proceedToSponsor() {
        val state = currentState
        if (state.eventName.isBlank()) return

        draftStore.updateRateCalc(
            eventName = state.eventName,
            splitPaymentEnabled = state.splitPaymentEnabled,
        )

        if (state.splitPaymentEnabled) {
            val voyagerUserId = draftStore.state.value.voyagerUserId
            draftStore.setSponsors(
                listOf(
                    Sponsor(
                        VoyagerUserId = voyagerUserId,
                        VoyagerUserName = "",
                        AmountToPay = 0.0,
                        Status = "",
                    ),
                ),
            )
        } else {
            draftStore.setSponsors(emptyList())
        }

        emitEffect(
            CreateVoyageRateCalcUiEffect.NavigateToSponsor(state.splitPaymentEnabled),
        )
    }
}
