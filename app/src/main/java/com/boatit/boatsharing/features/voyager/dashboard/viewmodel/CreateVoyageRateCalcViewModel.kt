package com.boatit.boatsharing.features.voyager.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import com.boatit.boatsharing.features.voyager.dashboard.model.CreateVoyageRateCalcUiEffect
import com.boatit.boatsharing.features.voyager.dashboard.model.CreateVoyageRateCalcUiEvent
import com.boatit.boatsharing.features.voyager.dashboard.model.Sponser
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

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
)

class CreateVoyageRateCalcViewModel(
    private val draftStore: CreateVoyageDraftStore,
) : com.boatit.boatsharing.core.presentation.LegacyMviViewModel(), ICreateVoyageRateCalcViewModel {
    private val _uiState = MutableStateFlow(CreateVoyageRateCalcUiState())
    override val uiState: StateFlow<CreateVoyageRateCalcUiState> = _uiState.asStateFlow()

    private val _uiEffects = MutableSharedFlow<CreateVoyageRateCalcUiEffect>(extraBufferCapacity = 1)
    override val uiEffects: SharedFlow<CreateVoyageRateCalcUiEffect> = _uiEffects

    override fun onEvent(event: CreateVoyageRateCalcUiEvent) {
        when (event) {
            CreateVoyageRateCalcUiEvent.Initialize -> initializeState()
            is CreateVoyageRateCalcUiEvent.EventNameChanged -> {
                _uiState.value = _uiState.value.copy(eventName = event.value)
            }
            is CreateVoyageRateCalcUiEvent.SplitPaymentToggled -> {
                _uiState.value = _uiState.value.copy(splitPaymentEnabled = event.enabled)
            }
            CreateVoyageRateCalcUiEvent.Proceed -> proceedToSponsor()
        }
    }

    private fun initializeState() {
        val draft = draftStore.state.value
        _uiState.value =
            _uiState.value.copy(
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

    private fun proceedToSponsor() {
        val state = _uiState.value
        if (state.eventName.isBlank()) return

        draftStore.updateRateCalc(
            eventName = state.eventName,
            splitPaymentEnabled = state.splitPaymentEnabled,
        )

        if (state.splitPaymentEnabled) {
            val voyagerUserId = draftStore.state.value.voyagerUserId
            draftStore.setSponsors(
                listOf(
                    Sponser(
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

        _uiEffects.tryEmit(
            CreateVoyageRateCalcUiEffect.NavigateToSponsor(state.splitPaymentEnabled),
        )
    }
}
