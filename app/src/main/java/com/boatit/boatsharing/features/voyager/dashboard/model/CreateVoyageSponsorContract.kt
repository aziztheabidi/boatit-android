package com.boatit.boatsharing.features.voyager.dashboard.model

sealed interface CreateVoyageSponsorUiEvent {
    data object Initialize : CreateVoyageSponsorUiEvent

    data object RefreshDisplayData : CreateVoyageSponsorUiEvent

    data object LoadFollowedVoyagers : CreateVoyageSponsorUiEvent

    data class UpdateSearchQuery(val query: String) : CreateVoyageSponsorUiEvent

    data class AddSponsor(val voyagerUserId: String, val voyagerUserName: String) : CreateVoyageSponsorUiEvent

    data class RemoveSponsor(val voyagerUserId: String) : CreateVoyageSponsorUiEvent

    data class ToggleSponsorSelection(val voyagerUserId: String, val voyagerUserName: String) : CreateVoyageSponsorUiEvent

    data class UpdateSponsorAmount(val voyagerUserId: String, val amountToPay: Double) : CreateVoyageSponsorUiEvent
}
