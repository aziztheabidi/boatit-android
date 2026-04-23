package com.boatit.boatsharing.features.voyager.dashboard.viewmodel

import com.boatit.boatsharing.features.voyager.dashboard.model.Sponsor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class CreateVoyageDraftState(
    val initialized: Boolean = false,
    val voyagerUserId: String = "",
    val eventName: String = "",
    val voyageCategoryId: Int = 0,
    val pickupDockId: Int = 0,
    val pickupDockName: String = "",
    val dropOffDockId: Int = 0,
    val dropOffDockName: String = "",
    val noOfVoyagers: Int = 0,
    val isImmediately: Boolean = true,
    val splitPaymentEnabled: Boolean = false,
    val bookingDate: String = "",
    val startTime: String = "",
    val isStayOnWater: Boolean = false,
    val endTime: String = "",
    val perHourRate: Double = 0.0,
    val durationInHours: Double = 0.0,
    val estimatedCost: Double = 0.0,
    val totalCostAmount: Double = 0.0,
    val sponsorEntries: List<Sponsor> = emptyList(),
)

class CreateVoyageDraftStore {
    private val _state = MutableStateFlow(CreateVoyageDraftState())
    val state: StateFlow<CreateVoyageDraftState> = _state.asStateFlow()

    fun clear() {
        _state.value = CreateVoyageDraftState()
    }

    fun setDraft(draft: CreateVoyageDraftState) {
        _state.value = draft.copy(initialized = true)
    }

    fun updateRateCalc(
        eventName: String,
        splitPaymentEnabled: Boolean,
    ) {
        _state.value =
            _state.value.copy(
                initialized = true,
                eventName = eventName,
                splitPaymentEnabled = splitPaymentEnabled,
            )
    }

    fun setSponsors(sponsors: List<Sponsor>) {
        _state.value =
            _state.value.copy(
                initialized = true,
                sponsorEntries = sponsors,
            )
    }
}
