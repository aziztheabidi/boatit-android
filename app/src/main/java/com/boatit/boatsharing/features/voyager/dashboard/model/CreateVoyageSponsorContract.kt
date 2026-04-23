package com.boatit.boatsharing.features.voyager.dashboard.model

import com.boatit.boatsharing.core.presentation.UiEffect
import com.boatit.boatsharing.core.presentation.UiEvent

sealed interface CreateVoyageSponsorUiEvent : UiEvent {
    data object Initialize : CreateVoyageSponsorUiEvent

    data object RefreshDisplayData : CreateVoyageSponsorUiEvent

    data object LoadFollowedVoyagers : CreateVoyageSponsorUiEvent

    data class UpdateSearchQuery(val query: String) : CreateVoyageSponsorUiEvent

    data class AddSponsor(val voyagerUserId: String, val voyagerUserName: String) : CreateVoyageSponsorUiEvent

    data class RemoveSponsor(val voyagerUserId: String) : CreateVoyageSponsorUiEvent

    data class ToggleSponsorSelection(val voyagerUserId: String, val voyagerUserName: String) : CreateVoyageSponsorUiEvent

    data class UpdateSponsorAmount(val voyagerUserId: String, val amountToPay: Double) : CreateVoyageSponsorUiEvent
}

sealed interface CreateVoyageSponsorUiEffect : UiEffect {
    data object NoOpEffect : CreateVoyageSponsorUiEffect
}
