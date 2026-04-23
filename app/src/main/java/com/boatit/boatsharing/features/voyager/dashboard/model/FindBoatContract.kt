package com.boatit.boatsharing.features.voyager.dashboard.model

import com.boatit.boatsharing.core.presentation.UiEffect
import com.boatit.boatsharing.core.presentation.UiEvent
import com.boatit.boatsharing.core.presentation.UiState
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse

data class FindBoatUiState(
    val voyagerUserId: String = "",
    val pickupLocation: String = "",
    val pickupDockId: Int? = null,
    val dropOffLocation: String = "",
    val dropOffDockId: Int? = null,
    val category: String = "",
    val categoryId: Int? = null,
    val passengerCount: String = "",
    val bookingDate: String = "",
    val isSubmitting: Boolean = false,
    val showSponsorErrorDialog: Boolean = false,
    val sponsorErrorMessage: String = "",
    val sponsorSplitPaymentEnabled: Boolean = false,
    val sponsorActionText: String = "Find Boat",
    val isCategoryDropdownExpanded: Boolean = false,
    val isPickupDropdownExpanded: Boolean = false,
    val isDropOffDropdownExpanded: Boolean = false,
    val showPassengerLimitDialog: Boolean = false,
    val categoryOptions: List<VoyageCategory> = emptyList(),
    val dockOptions: List<Place> = emptyList(),
    val findBoatRequest: NetworkResponse<FindBoatResponse> = NetworkResponse.Loading(),
) : UiState

sealed interface FindBoatUiEvent : UiEvent {
    data class Initialize(
        val voyagerUserId: String,
        val pickupLocation: String,
        val pickupDockId: Int?,
        val dropOffLocation: String,
        val dropOffDockId: Int?,
        val passengerCount: String,
        val bookingDate: String,
    ) : FindBoatUiEvent

    data class SetCategory(val category: String, val categoryId: Int?) : FindBoatUiEvent

    data class SetPickupLocation(val name: String, val dockTypeId: Int?) : FindBoatUiEvent

    data class SetDropOffLocation(val name: String, val dockTypeId: Int?) : FindBoatUiEvent

    data class SetPassengerCount(val passengerCount: String) : FindBoatUiEvent

    data class SetCategoryOptions(val options: List<VoyageCategory>) : FindBoatUiEvent

    data class SetDockOptions(val options: List<Place>) : FindBoatUiEvent

    data class ToggleCategoryDropdown(val expanded: Boolean) : FindBoatUiEvent

    data class TogglePickupDropdown(val expanded: Boolean) : FindBoatUiEvent

    data class ToggleDropOffDropdown(val expanded: Boolean) : FindBoatUiEvent

    data class InitializeSponsorUi(val split: Boolean, val travelNow: Boolean) : FindBoatUiEvent

    data class SubmitFindBoatRequest(val request: FindBoatRequest) : FindBoatUiEvent

    data object ResetRequestState : FindBoatUiEvent

    data object DismissSponsorErrorDialog : FindBoatUiEvent

    data object Submit : FindBoatUiEvent

    data object DismissPassengerDialog : FindBoatUiEvent
}

sealed interface FindBoatUiEffect : UiEffect {
    data object NavigateCreateVoyage : FindBoatUiEffect

    data object NavigateDashboardAfterFindBoat : FindBoatUiEffect

    data class ShowFindBoatError(val message: String) : FindBoatUiEffect
}
