package com.boatit.boatsharing.features.voyager.dashboard.viewmodel

import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.core.presentation.BaseViewModel
import com.boatit.boatsharing.core.presentation.UiEffect
import com.boatit.boatsharing.core.presentation.UiEvent
import com.boatit.boatsharing.core.presentation.UiState
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.features.voyager.dashboard.domain.usecase.FetchTravelNowVoyagesUseCase
import com.boatit.boatsharing.features.voyager.dashboard.model.TravelNowObj
import kotlinx.coroutines.launch

data class TravelNowUiState(
    val voyage: TravelNowObj? = null,
    val isLoading: Boolean = false,
    val toastMessage: String? = null,
) : UiState

sealed interface TravelNowUiEvent : UiEvent {
    data object LoadVoyages : TravelNowUiEvent

    data object ClearToast : TravelNowUiEvent
}

sealed interface TravelNowUiEffect : UiEffect {
    data class ShowToast(val message: String) : TravelNowUiEffect
}

class TravelNowViewModel(
    private val fetchTravelNowVoyagesUseCase: FetchTravelNowVoyagesUseCase,
    private val cancelVM: CancelBookedVoyageViewModel,
    private val confirmVM: ConfirmBookedVoyageViewModel,
) : BaseViewModel<TravelNowUiState, TravelNowUiEvent, TravelNowUiEffect>(TravelNowUiState()) {
    init {
        onEvent(TravelNowUiEvent.LoadVoyages)
        observeCancelState()
        observeConfirmState()
    }

    override fun onEvent(event: TravelNowUiEvent) {
        when (event) {
            TravelNowUiEvent.LoadVoyages -> loadVoyages()
            TravelNowUiEvent.ClearToast -> updateState { copy(isLoading = false, toastMessage = null) }
        }
    }

    fun loadVoyages() {
        viewModelScope.launch {
            updateState { copy(isLoading = true, toastMessage = null) }

            when (val result = fetchTravelNowVoyagesUseCase().toResource()) {
                is Resource.Success -> {
                    val response = result.data
                    updateState {
                        if (response.Status == 200) {
                            copy(
                                voyage = response.obj,
                                isLoading = false,
                                toastMessage = "Success",
                            )
                        } else {
                            copy(
                                voyage = TravelNowObj(),
                                isLoading = true,
                                toastMessage = "No data found",
                            )
                        }
                    }
                }

                is Resource.Error -> {
                    updateState {
                        copy(
                            voyage = TravelNowObj(),
                            isLoading = true,
                            toastMessage = "No data found",
                        )
                    }
                }

                Resource.Loading -> {
                    updateState { copy(isLoading = true) }
                }
            }
        }
    }

    private fun observeCancelState() {
        viewModelScope.launch {
            cancelVM.uiState.collect { cancelStateHolder ->
                when (val cancelState = cancelStateHolder.nearbyPlaces) {
                    is NetworkResponse.Success -> {
                        emitEffect(TravelNowUiEffect.ShowToast("Cancelled"))
                        cancelVM.resetNearbyPlaces()
                        loadVoyages()
                    }

                    is NetworkResponse.Error -> {
                        emitEffect(
                            TravelNowUiEffect.ShowToast(cancelState.message ?: "Cancel failed"),
                        )
                        cancelVM.resetNearbyPlaces()
                        loadVoyages()
                    }

                    else -> {}
                }
            }
        }
    }

    private fun observeConfirmState() {
        viewModelScope.launch {
            confirmVM.uiState.collect { holder ->
                when (val confirmState = holder.confirmationState) {
                    is NetworkResponse.Success -> {
                        emitEffect(TravelNowUiEffect.ShowToast("Voyage Confirmed"))
                        confirmVM.resetConfirmationState()
                        loadVoyages()
                    }

                    is NetworkResponse.Error -> {
                        emitEffect(
                            TravelNowUiEffect.ShowToast(confirmState.message ?: "Confirmation failed"),
                        )
                        confirmVM.resetConfirmationState()
                    }

                    else -> {}
                }
            }
        }
    }

    fun clearToast() {
        onEvent(TravelNowUiEvent.ClearToast)
    }
}
