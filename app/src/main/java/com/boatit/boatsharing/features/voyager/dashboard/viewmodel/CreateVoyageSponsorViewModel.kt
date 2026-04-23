package com.boatit.boatsharing.features.voyager.dashboard.viewmodel

import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.core.presentation.BaseViewModel
import com.boatit.boatsharing.core.presentation.UiState
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.features.chat.model.VoyagerInfo
import com.boatit.boatsharing.features.voyager.dashboard.domain.usecase.FetchActiveVoyagersUseCase
import com.boatit.boatsharing.features.voyager.dashboard.model.CreateVoyageSponsorUiEffect
import com.boatit.boatsharing.features.voyager.dashboard.model.CreateVoyageSponsorUiEvent
import com.boatit.boatsharing.features.voyager.dashboard.model.Sponsor
import kotlinx.coroutines.launch

data class CreateVoyageSponsorUiState(
    val voyagerUserId: String = "",
    val eventName: String = "",
    val voyageCategoryId: Int = 0,
    val pickupDockId: Int = 0,
    val dropOffDockId: Int = 0,
    val noOfVoyagers: Int = 0,
    val isImmediately: Boolean = true,
    val bookingDate: String = "",
    val startTime: String = "",
    val isStayOnWater: Boolean = false,
    val endTime: String = "",
    val perHourRate: Double = 0.0,
    val durationInHours: Double = 0.0,
    val totalCostAmount: Double = 0.0,
    val totalFare: String = "",
    val individualFare: String = "",
    val pickup: String = "",
    val dropOff: String = "",
    val sponsorCount: String = "0",
    val sponsorEntries: List<Sponsor> = emptyList(),
    val searchQuery: String = "",
    val followedVoyagers: List<VoyagerInfo> = emptyList(),
    val filteredFollowedVoyagers: List<VoyagerInfo> = emptyList(),
    val isVoyagersLoading: Boolean = false,
    val voyagersLoadError: String? = null,
    val splitPaymentEnabled: Boolean = false,
    val actionText: String = "Find Boat",
) : UiState

class CreateVoyageSponsorViewModel(
    private val fetchActiveVoyagersUseCase: FetchActiveVoyagersUseCase,
    private val draftStore: CreateVoyageDraftStore,
) : BaseViewModel<CreateVoyageSponsorUiState, CreateVoyageSponsorUiEvent, CreateVoyageSponsorUiEffect>(
        CreateVoyageSponsorUiState(),
    ),
    ICreateVoyageSponsorViewModel {
    override fun onEvent(event: CreateVoyageSponsorUiEvent) {
        when (event) {
            CreateVoyageSponsorUiEvent.Initialize -> {
                val draft = draftStore.state.value
                val splitEnabled = draft.splitPaymentEnabled || !draft.isImmediately
                updateState {
                    copy(
                        splitPaymentEnabled = splitEnabled,
                        actionText = if (splitEnabled) "Book Voyage" else "Find Boat",
                    )
                }
                syncBookingDraftData()
                syncDisplayData()
                loadFollowedVoyagers()
            }
            CreateVoyageSponsorUiEvent.RefreshDisplayData -> syncDisplayData()
            CreateVoyageSponsorUiEvent.LoadFollowedVoyagers -> loadFollowedVoyagers()
            is CreateVoyageSponsorUiEvent.UpdateSearchQuery -> {
                updateState {
                    copy(
                        searchQuery = event.query,
                        filteredFollowedVoyagers =
                            filterFollowedVoyagers(
                                voyagers = currentState.followedVoyagers,
                                query = event.query,
                            ),
                    )
                }
            }
            is CreateVoyageSponsorUiEvent.AddSponsor -> addSponsor(event.voyagerUserId, event.voyagerUserName)
            is CreateVoyageSponsorUiEvent.RemoveSponsor -> removeSponsor(event.voyagerUserId)
            is CreateVoyageSponsorUiEvent.ToggleSponsorSelection -> {
                if (currentState.sponsorEntries.any { it.VoyagerUserId == event.voyagerUserId }) {
                    removeSponsor(event.voyagerUserId)
                } else {
                    addSponsor(event.voyagerUserId, event.voyagerUserName)
                }
            }
            is CreateVoyageSponsorUiEvent.UpdateSponsorAmount -> {
                val updated =
                    currentState.sponsorEntries.map {
                        if (it.VoyagerUserId == event.voyagerUserId) it.copy(AmountToPay = event.amountToPay) else it
                    }
                val normalized = normalizeSponsorAmounts(updated)
                updateState { copy(sponsorEntries = normalized) }
                writeBackSponsors()
                syncDisplayData(updatedSponsors = normalized)
            }
        }
    }

    private fun addSponsor(
        voyagerUserId: String,
        voyagerUserName: String,
    ) {
        if (currentState.sponsorEntries.any { it.VoyagerUserId == voyagerUserId }) return

        val updated =
            currentState.sponsorEntries +
                Sponsor(
                    VoyagerUserId = voyagerUserId,
                    VoyagerUserName = voyagerUserName,
                    AmountToPay = 0.0,
                    Status = "",
                )
        val normalized = normalizeSponsorAmounts(updated)
        updateState { copy(sponsorEntries = normalized) }
        writeBackSponsors()
        syncDisplayData(updatedSponsors = normalized)
    }

    private fun removeSponsor(voyagerUserId: String) {
        val updated = currentState.sponsorEntries.filterNot { it.VoyagerUserId == voyagerUserId }
        val normalized = normalizeSponsorAmounts(updated)
        updateState { copy(sponsorEntries = normalized) }
        writeBackSponsors()
        syncDisplayData(updatedSponsors = normalized)
    }

    private fun writeBackSponsors() {
        draftStore.setSponsors(currentState.sponsorEntries)
    }

    private fun normalizeSponsorAmounts(sponsors: List<Sponsor>): List<Sponsor> {
        val splitAmount = calculateIndividualFareAmount(sponsors)
        return sponsors.map { sponsor ->
            sponsor.copy(AmountToPay = splitAmount)
        }
    }

    private fun calculateIndividualFareAmount(sponsors: List<Sponsor>): Double {
        if (sponsors.isEmpty()) return 0.0
        val totalCost = currentState.totalCostAmount
        return totalCost / sponsors.size
    }

    private fun loadFollowedVoyagers() {
        viewModelScope.launch {
            updateState { copy(isVoyagersLoading = true, voyagersLoadError = null) }
            when (val result = fetchActiveVoyagersUseCase().toResource()) {
                is Resource.Success -> {
                    val followed = result.data.obj.Followed
                    val currentQuery = currentState.searchQuery
                    updateState {
                        copy(
                            isVoyagersLoading = false,
                            voyagersLoadError = null,
                            followedVoyagers = followed,
                            filteredFollowedVoyagers = filterFollowedVoyagers(followed, currentQuery),
                        )
                    }
                }

                is Resource.Error -> {
                    updateState {
                        copy(
                            isVoyagersLoading = false,
                            voyagersLoadError = result.error.toMessage(),
                        )
                    }
                }

                Resource.Loading -> {
                    updateState { copy(isVoyagersLoading = true) }
                }
            }
        }
    }

    private fun filterFollowedVoyagers(
        voyagers: List<VoyagerInfo>,
        query: String,
    ): List<VoyagerInfo> {
        if (query.isBlank()) return voyagers
        return voyagers.filter { voyager ->
            voyager.FirstName.contains(query, ignoreCase = true)
        }
    }

    private fun syncBookingDraftData() {
        val draft = draftStore.state.value
        updateState {
            copy(
                voyagerUserId = draft.voyagerUserId,
                eventName = draft.eventName,
                voyageCategoryId = draft.voyageCategoryId,
                pickupDockId = draft.pickupDockId,
                dropOffDockId = draft.dropOffDockId,
                noOfVoyagers = draft.noOfVoyagers,
                isImmediately = draft.isImmediately,
                bookingDate = draft.bookingDate,
                startTime = draft.startTime,
                isStayOnWater = draft.isStayOnWater,
                endTime = draft.endTime,
                perHourRate = draft.perHourRate,
                durationInHours = draft.durationInHours,
                totalCostAmount = draft.totalCostAmount,
                pickup = draft.pickupDockName,
                dropOff = draft.dropOffDockName,
                sponsorEntries = if (draft.sponsorEntries.isNotEmpty()) draft.sponsorEntries else sponsorEntries,
            )
        }
    }

    private fun syncDisplayData(updatedSponsors: List<Sponsor>? = null) {
        val currentSponsors = updatedSponsors ?: currentState.sponsorEntries
        val individualAmount = calculateIndividualFareAmount(currentSponsors)
        updateState {
            copy(
                totalFare = totalCostAmount.toString(),
                individualFare = individualAmount.toString(),
                pickup = pickup,
                dropOff = dropOff,
                sponsorCount = currentSponsors.size.toString(),
                sponsorEntries = currentSponsors,
            )
        }
    }
}
