package com.boatit.boatsharing.features.voyager.dashboard.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.features.voyager.dashboard.domain.usecase.FindBoatUseCase
import com.boatit.boatsharing.features.voyager.dashboard.model.FindBoatRequest
import com.boatit.boatsharing.features.voyager.dashboard.model.FindBoatResponse
import com.boatit.boatsharing.features.voyager.dashboard.model.FindBoatUiEffect
import com.boatit.boatsharing.features.voyager.dashboard.model.FindBoatUiEvent
import com.boatit.boatsharing.features.voyager.dashboard.model.FindBoatUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FindBoatViewModel(
    private val findBoatUseCase: FindBoatUseCase,
    private val draftStore: CreateVoyageDraftStore,
) : com.boatit.boatsharing.core.presentation.LegacyMviViewModel(), IFindBoatViewModel {
    private val _uiState = MutableStateFlow(FindBoatUiState())
    override val uiState: StateFlow<FindBoatUiState> = _uiState.asStateFlow()

    private val _uiEffects = MutableSharedFlow<FindBoatUiEffect>(extraBufferCapacity = 1)
    override val uiEffects: SharedFlow<FindBoatUiEffect> = _uiEffects

    private val _nearbyPlaces = MutableStateFlow<NetworkResponse<FindBoatResponse>>(NetworkResponse.Loading())
    val nearbyPlaces: StateFlow<NetworkResponse<FindBoatResponse>> = _nearbyPlaces.asStateFlow()

    override fun onEvent(event: FindBoatUiEvent) {
        when (event) {
            is FindBoatUiEvent.Initialize -> {
                _uiState.value =
                    _uiState.value.copy(
                        voyagerUserId = event.voyagerUserId,
                        pickupLocation = event.pickupLocation,
                        pickupDockId = event.pickupDockId,
                        dropOffLocation = event.dropOffLocation,
                        dropOffDockId = event.dropOffDockId,
                        passengerCount = event.passengerCount,
                        bookingDate = event.bookingDate,
                    )
            }
            is FindBoatUiEvent.SetCategory -> {
                _uiState.value =
                    _uiState.value.copy(
                        category = event.category,
                        categoryId = event.categoryId,
                        isCategoryDropdownExpanded = false,
                    )
            }
            is FindBoatUiEvent.SetPickupLocation -> {
                _uiState.value =
                    _uiState.value.copy(
                        pickupLocation = event.name,
                        pickupDockId = event.dockTypeId,
                        isPickupDropdownExpanded = false,
                    )
            }
            is FindBoatUiEvent.SetDropOffLocation -> {
                _uiState.value =
                    _uiState.value.copy(
                        dropOffLocation = event.name,
                        dropOffDockId = event.dockTypeId,
                        isDropOffDropdownExpanded = false,
                    )
            }
            is FindBoatUiEvent.SetPassengerCount -> {
                _uiState.value = _uiState.value.copy(passengerCount = event.passengerCount)
            }
            is FindBoatUiEvent.SetCategoryOptions -> {
                _uiState.value = _uiState.value.copy(categoryOptions = event.options)
            }
            is FindBoatUiEvent.SetDockOptions -> {
                _uiState.value = _uiState.value.copy(dockOptions = event.options)
            }
            is FindBoatUiEvent.ToggleCategoryDropdown -> {
                _uiState.value = _uiState.value.copy(isCategoryDropdownExpanded = event.expanded)
            }
            is FindBoatUiEvent.TogglePickupDropdown -> {
                _uiState.value = _uiState.value.copy(isPickupDropdownExpanded = event.expanded)
            }
            is FindBoatUiEvent.ToggleDropOffDropdown -> {
                _uiState.value = _uiState.value.copy(isDropOffDropdownExpanded = event.expanded)
            }
            is FindBoatUiEvent.InitializeSponsorUi -> {
                val splitEnabled = event.split || !event.travelNow
                _uiState.value =
                    _uiState.value.copy(
                        sponsorSplitPaymentEnabled = splitEnabled,
                        sponsorActionText = if (splitEnabled) "Book Voyage" else "Find Boat",
                    )
            }
            is FindBoatUiEvent.SubmitFindBoatRequest -> submitFindBoatRequest(event.request)
            FindBoatUiEvent.ResetRequestState -> resetFindBoatRequestState()
            FindBoatUiEvent.DismissSponsorErrorDialog -> {
                _uiState.value =
                    _uiState.value.copy(
                        showSponsorErrorDialog = false,
                        sponsorErrorMessage = "",
                    )
            }
            FindBoatUiEvent.DismissPassengerDialog -> {
                _uiState.value = _uiState.value.copy(showPassengerLimitDialog = false)
            }
            FindBoatUiEvent.Submit -> validateAndProceed()
        }
    }

    private fun validateAndProceed() {
        val state = _uiState.value
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
                _uiState.value = state.copy(showPassengerLimitDialog = true)
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
            _uiEffects.tryEmit(FindBoatUiEffect.NavigateCreateVoyage)
        }
    }

    fun submitFindBoatRequest(request: FindBoatRequest) =
        viewModelScope.launch {
            _uiState.value =
                _uiState.value.copy(
                    isSubmitting = true,
                    showSponsorErrorDialog = false,
                    sponsorErrorMessage = "",
                )
            _nearbyPlaces.value = NetworkResponse.Loading()
            when (val result = findBoatUseCase(request).toResource()) {
                is Resource.Success -> {
                    _nearbyPlaces.value = NetworkResponse.Success(result.data)
                    _uiState.value = _uiState.value.copy(isSubmitting = false)
                    _uiEffects.tryEmit(FindBoatUiEffect.NavigateDashboardAfterFindBoat)
                }

                is Resource.Error -> {
                    val errorMessage = result.error.toMessage()
                    Log.e("viewModel", "Error fetching places: $errorMessage")
                    _nearbyPlaces.value = NetworkResponse.Error(result.error)
                    _uiState.value =
                        _uiState.value.copy(
                            isSubmitting = false,
                            showSponsorErrorDialog = true,
                            sponsorErrorMessage = errorMessage,
                        )
                    _uiEffects.tryEmit(FindBoatUiEffect.ShowFindBoatError(errorMessage))
                }

                Resource.Loading -> {
                    _nearbyPlaces.value = NetworkResponse.Loading()
                    _uiState.value = _uiState.value.copy(isSubmitting = true)
                }
            }
        }

    @Deprecated("Use submitFindBoatRequest")
    fun fetchNearbyPlaces(profile: FindBoatRequest) {
        submitFindBoatRequest(profile)
    }

    fun resetFindBoatRequestState() {
        _nearbyPlaces.value = NetworkResponse.Loading()
        _uiState.value = _uiState.value.copy(isSubmitting = false)
    }

    @Deprecated("Use resetFindBoatRequestState")
    fun resetNearbyPlaces() {
        resetFindBoatRequestState()
    }
}
