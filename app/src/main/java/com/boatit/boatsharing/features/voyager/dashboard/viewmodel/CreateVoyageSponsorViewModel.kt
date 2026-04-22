package com.boatit.boatsharing.features.voyager.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.features.chat.model.VoyagerInfo
import com.boatit.boatsharing.features.voyager.dashboard.domain.usecase.FetchActiveVoyagersUseCase
import com.boatit.boatsharing.features.voyager.dashboard.model.CreateVoyageSponsorUiEvent
import com.boatit.boatsharing.features.voyager.dashboard.model.Sponser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    val sponsorEntries: List<Sponser> = emptyList(),
    val searchQuery: String = "",
    val followedVoyagers: List<VoyagerInfo> = emptyList(),
    val filteredFollowedVoyagers: List<VoyagerInfo> = emptyList(),
    val isVoyagersLoading: Boolean = false,
    val voyagersLoadError: String? = null,
    val splitPaymentEnabled: Boolean = false,
    val actionText: String = "Find Boat",
)

class CreateVoyageSponsorViewModel(
    private val fetchActiveVoyagersUseCase: FetchActiveVoyagersUseCase,
    private val draftStore: CreateVoyageDraftStore,
) : com.boatit.boatsharing.core.presentation.LegacyMviViewModel(), ICreateVoyageSponsorViewModel {
    private val _uiState = MutableStateFlow(CreateVoyageSponsorUiState())
    override val uiState: StateFlow<CreateVoyageSponsorUiState> = _uiState.asStateFlow()

    override fun onEvent(event: CreateVoyageSponsorUiEvent) {
        when (event) {
            CreateVoyageSponsorUiEvent.Initialize -> {
                val draft = draftStore.state.value
                val splitEnabled = draft.splitPaymentEnabled || !draft.isImmediately
                _uiState.value =
                    _uiState.value.copy(
                        splitPaymentEnabled = splitEnabled,
                        actionText = if (splitEnabled) "Book Voyage" else "Find Boat",
                    )
                syncBookingDraftData()
                syncDisplayData()
                loadFollowedVoyagers()
            }
            CreateVoyageSponsorUiEvent.RefreshDisplayData -> syncDisplayData()
            CreateVoyageSponsorUiEvent.LoadFollowedVoyagers -> loadFollowedVoyagers()
            is CreateVoyageSponsorUiEvent.UpdateSearchQuery -> {
                _uiState.value =
                    _uiState.value.copy(
                        searchQuery = event.query,
                        filteredFollowedVoyagers =
                            filterFollowedVoyagers(
                                voyagers = _uiState.value.followedVoyagers,
                                query = event.query,
                            ),
                    )
            }
            is CreateVoyageSponsorUiEvent.AddSponsor -> addSponsor(event.voyagerUserId, event.voyagerUserName)
            is CreateVoyageSponsorUiEvent.RemoveSponsor -> removeSponsor(event.voyagerUserId)
            is CreateVoyageSponsorUiEvent.ToggleSponsorSelection -> {
                if (_uiState.value.sponsorEntries.any { it.VoyagerUserId == event.voyagerUserId }) {
                    removeSponsor(event.voyagerUserId)
                } else {
                    addSponsor(event.voyagerUserId, event.voyagerUserName)
                }
            }
            is CreateVoyageSponsorUiEvent.UpdateSponsorAmount -> {
                val updated =
                    _uiState.value.sponsorEntries.map {
                        if (it.VoyagerUserId == event.voyagerUserId) it.copy(AmountToPay = event.amountToPay) else it
                    }
                val normalized = normalizeSponsorAmounts(updated)
                _uiState.value = _uiState.value.copy(sponsorEntries = normalized)
                writeBackSponsors()
                syncDisplayData(updatedSponsors = normalized)
            }
        }
    }

    private fun addSponsor(
        voyagerUserId: String,
        voyagerUserName: String,
    ) {
        if (_uiState.value.sponsorEntries.any { it.VoyagerUserId == voyagerUserId }) return

        val updated =
            _uiState.value.sponsorEntries +
                Sponser(
                    VoyagerUserId = voyagerUserId,
                    VoyagerUserName = voyagerUserName,
                    AmountToPay = 0.0,
                    Status = "",
                )
        val normalized = normalizeSponsorAmounts(updated)
        _uiState.value = _uiState.value.copy(sponsorEntries = normalized)
        writeBackSponsors()
        syncDisplayData(updatedSponsors = normalized)
    }

    private fun removeSponsor(voyagerUserId: String) {
        val updated = _uiState.value.sponsorEntries.filterNot { it.VoyagerUserId == voyagerUserId }
        val normalized = normalizeSponsorAmounts(updated)
        _uiState.value = _uiState.value.copy(sponsorEntries = normalized)
        writeBackSponsors()
        syncDisplayData(updatedSponsors = normalized)
    }

    private fun writeBackSponsors() {
        draftStore.setSponsors(_uiState.value.sponsorEntries)
    }

    private fun normalizeSponsorAmounts(sponsors: List<Sponser>): List<Sponser> {
        val splitAmount = calculateIndividualFareAmount(sponsors)
        return sponsors.map { sponsor ->
            sponsor.copy(AmountToPay = splitAmount)
        }
    }

    private fun calculateIndividualFareAmount(sponsors: List<Sponser>): Double {
        if (sponsors.isEmpty()) return 0.0
        val totalCost = _uiState.value.totalCostAmount
        return totalCost / sponsors.size
    }

    private fun loadFollowedVoyagers() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isVoyagersLoading = true, voyagersLoadError = null)
            when (val result = fetchActiveVoyagersUseCase().toResource()) {
                is Resource.Success -> {
                    val followed = result.data.obj.Followed
                    val currentQuery = _uiState.value.searchQuery
                    _uiState.value =
                        _uiState.value.copy(
                            isVoyagersLoading = false,
                            voyagersLoadError = null,
                            followedVoyagers = followed,
                            filteredFollowedVoyagers = filterFollowedVoyagers(followed, currentQuery),
                        )
                }

                is Resource.Error -> {
                    _uiState.value =
                        _uiState.value.copy(
                            isVoyagersLoading = false,
                            voyagersLoadError = result.error.toMessage(),
                        )
                }

                Resource.Loading -> {
                    _uiState.value = _uiState.value.copy(isVoyagersLoading = true)
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
        _uiState.value =
            _uiState.value.copy(
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
                sponsorEntries = if (draft.sponsorEntries.isNotEmpty()) draft.sponsorEntries else _uiState.value.sponsorEntries,
            )
    }

    private fun syncDisplayData(updatedSponsors: List<Sponser>? = null) {
        val currentSponsors = updatedSponsors ?: _uiState.value.sponsorEntries
        val individualAmount = calculateIndividualFareAmount(currentSponsors)
        _uiState.value =
            _uiState.value.copy(
                totalFare = _uiState.value.totalCostAmount.toString(),
                individualFare = individualAmount.toString(),
                pickup = _uiState.value.pickup,
                dropOff = _uiState.value.dropOff,
                sponsorCount = currentSponsors.size.toString(),
                sponsorEntries = currentSponsors,
            )
    }
}
