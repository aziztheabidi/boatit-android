package com.boatit.boatsharing.features.voyager.dashboard.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.core.presentation.BaseViewModel
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.features.voyager.dashboard.domain.usecase.FindBoatUseCase
import com.boatit.boatsharing.features.voyager.dashboard.model.FindBoatRequest
import com.boatit.boatsharing.features.voyager.dashboard.model.FindBoatUiEffect
import com.boatit.boatsharing.features.voyager.dashboard.model.FindBoatUiEvent
import com.boatit.boatsharing.features.voyager.dashboard.model.FindBoatUiState
import kotlinx.coroutines.launch

class FindBoatViewModel(
    private val findBoatUseCase: FindBoatUseCase,
    private val draftStore: CreateVoyageDraftStore,
) : BaseViewModel<FindBoatUiState, FindBoatUiEvent, FindBoatUiEffect>(FindBoatUiState()),
    IFindBoatViewModel {
    override fun onEvent(event: FindBoatUiEvent) {
        when (event) {
            is FindBoatUiEvent.Initialize -> {
                updateState {
                    copy(
                        voyagerUserId = event.voyagerUserId,
                        pickupLocation = event.pickupLocation,
                        pickupDockId = event.pickupDockId,
                        dropOffLocation = event.dropOffLocation,
                        dropOffDockId = event.dropOffDockId,
                        passengerCount = event.passengerCount,
                        bookingDate = event.bookingDate,
                    )
                }
            }
            is FindBoatUiEvent.SetCategory -> {
                updateState {
                    copy(
                        category = event.category,
                        categoryId = event.categoryId,
                        isCategoryDropdownExpanded = false,
                    )
                }
            }
            is FindBoatUiEvent.SetPickupLocation -> {
                updateState {
                    copy(
                        pickupLocation = event.name,
                        pickupDockId = event.dockTypeId,
                        isPickupDropdownExpanded = false,
                    )
                }
            }
            is FindBoatUiEvent.SetDropOffLocation -> {
                updateState {
                    copy(
                        dropOffLocation = event.name,
                        dropOffDockId = event.dockTypeId,
                        isDropOffDropdownExpanded = false,
                    )
                }
            }
            is FindBoatUiEvent.SetPassengerCount -> {
                updateState { copy(passengerCount = event.passengerCount) }
            }
            is FindBoatUiEvent.SetCategoryOptions -> {
                updateState { copy(categoryOptions = event.options) }
            }
            is FindBoatUiEvent.SetDockOptions -> {
                updateState { copy(dockOptions = event.options) }
            }
            is FindBoatUiEvent.ToggleCategoryDropdown -> {
                updateState { copy(isCategoryDropdownExpanded = event.expanded) }
            }
            is FindBoatUiEvent.TogglePickupDropdown -> {
                updateState { copy(isPickupDropdownExpanded = event.expanded) }
            }
            is FindBoatUiEvent.ToggleDropOffDropdown -> {
                updateState { copy(isDropOffDropdownExpanded = event.expanded) }
            }
            is FindBoatUiEvent.InitializeSponsorUi -> {
                val splitEnabled = event.split || !event.travelNow
                updateState {
                    copy(
                        sponsorSplitPaymentEnabled = splitEnabled,
                        sponsorActionText = if (splitEnabled) "Book Voyage" else "Find Boat",
                    )
                }
            }
            is FindBoatUiEvent.SubmitFindBoatRequest -> submitFindBoatRequest(event.request)
            FindBoatUiEvent.ResetRequestState -> resetFindBoatRequestState()
            FindBoatUiEvent.DismissSponsorErrorDialog -> {
                updateState {
                    copy(
                        showSponsorErrorDialog = false,
                        sponsorErrorMessage = "",
                    )
                }
            }
            FindBoatUiEvent.DismissPassengerDialog -> {
                updateState { copy(showPassengerLimitDialog = false) }
            }
            FindBoatUiEvent.Submit -> validateAndProceed()
        }
    }

    private fun validateAndProceed() {
        val state = currentState
        val operator =
            when {
                state.category.contains("<=") -> "<="
                state.category.contains(">=") -> ">="
                else -> null
            }

        val categoryInt = state.category.filter { it.isDigit() }.toIntOrNull()
        val passengerInt = state.passengerCount.toIntOrNull()

        if (operator != null && categoryInt != null && passengerInt != null) {
            val isInvalid =
                when (operator) {
                    "<=" -> passengerInt > categoryInt
                    ">=" -> passengerInt < categoryInt
                    else -> false
                }

            if (isInvalid) {
                updateState { copy(showPassengerLimitDialog = true) }
                return
            }
        }

        if (
            state.category.isNotBlank() &&
            state.passengerCount.isNotBlank() &&
            state.pickupLocation.isNotBlank() &&
            state.dropOffLocation.isNotBlank()
        ) {
            draftStore.setDraft(
                draftStore.state.value.copy(
                    initialized = true,
                    voyagerUserId = state.voyagerUserId,
                    voyageCategoryId = state.categoryId ?: 0,
                    pickupDockId = state.pickupDockId ?: 0,
                    pickupDockName = state.pickupLocation,
                    dropOffDockId = state.dropOffDockId ?: 0,
                    dropOffDockName = state.dropOffLocation,
                    noOfVoyagers = state.passengerCount.toIntOrNull() ?: 0,
                    bookingDate = state.bookingDate,
                ),
            )
            emitEffect(FindBoatUiEffect.NavigateCreateVoyage)
        }
    }

    fun submitFindBoatRequest(request: FindBoatRequest) =
        viewModelScope.launch {
            updateState {
                copy(
                    isSubmitting = true,
                    showSponsorErrorDialog = false,
                    sponsorErrorMessage = "",
                    findBoatRequest = NetworkResponse.Loading(),
                )
            }
            when (val result = findBoatUseCase(request).toResource()) {
                is Resource.Success -> {
                    updateState {
                        copy(
                            isSubmitting = false,
                            findBoatRequest = NetworkResponse.Success(result.data),
                        )
                    }
                    emitEffect(FindBoatUiEffect.NavigateDashboardAfterFindBoat)
                }

                is Resource.Error -> {
                    val errorMessage = result.error.toMessage()
                    Log.e("viewModel", "Error fetching places: $errorMessage")
                    updateState {
                        copy(
                            isSubmitting = false,
                            showSponsorErrorDialog = true,
                            sponsorErrorMessage = errorMessage,
                            findBoatRequest = NetworkResponse.Error(result.error),
                        )
                    }
                    emitEffect(FindBoatUiEffect.ShowFindBoatError(errorMessage))
                }

                Resource.Loading -> {
                    updateState {
                        copy(
                            isSubmitting = true,
                            findBoatRequest = NetworkResponse.Loading(),
                        )
                    }
                }
            }
        }

    @Deprecated("Use submitFindBoatRequest")
    fun fetchNearbyPlaces(profile: FindBoatRequest) {
        submitFindBoatRequest(profile)
    }

    fun resetFindBoatRequestState() {
        updateState {
            copy(
                isSubmitting = false,
                findBoatRequest = NetworkResponse.Loading(),
            )
        }
    }

    @Deprecated("Use resetFindBoatRequestState")
    fun resetNearbyPlaces() {
        resetFindBoatRequestState()
    }
}
