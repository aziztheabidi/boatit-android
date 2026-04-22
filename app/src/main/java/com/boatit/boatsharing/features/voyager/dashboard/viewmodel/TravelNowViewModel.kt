package com.boatit.boatsharing.features.voyager.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.features.voyager.dashboard.domain.usecase.FetchTravelNowVoyagesUseCase
import com.boatit.boatsharing.features.voyager.dashboard.model.TravelNowObj
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TravelNowViewModel(
    private val fetchTravelNowVoyagesUseCase: FetchTravelNowVoyagesUseCase,
    private val cancelVM: CancelBookedVoyageViewModel,
    private val confirmVM: ConfirmBookedVoyageViewModel,
) : com.boatit.boatsharing.core.presentation.LegacyMviViewModel() {
    private val _state = MutableStateFlow(TravelNowUiState())
    val state: StateFlow<TravelNowUiState> = _state

    init {
        loadVoyages()
        observeCancelState()
        observeConfirmState()
    }

    fun loadVoyages() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, toastMessage = null) }

            when (val result = fetchTravelNowVoyagesUseCase().toResource()) {
                is Resource.Success -> {
                    val response = result.data
                    _state.update {
                        if (response.Status == 200) {
                            it.copy(
                                voyage = response.obj,
                                isLoading = false,
                                toastMessage = "Success",
                            )
                        } else {
                            it.copy(
                                voyage = TravelNowObj(),
                                isLoading = true,
                                toastMessage = "No data found",
                            )
                        }
                    }
                }

                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            voyage = TravelNowObj(),
                            isLoading = true,
                            toastMessage = "No data found",
                        )
                    }
                }

                Resource.Loading -> {
                    _state.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    private fun observeCancelState() {
        viewModelScope.launch {
            cancelVM.nearbyPlaces.collect { cancelState ->
                when (cancelState) {
                    is NetworkResponse.Success -> {
                        _state.update {
                            it.copy(toastMessage = "Cancelled")
                        }
                        cancelVM.resetNearbyPlaces()
                        loadVoyages()
                    }

                    is NetworkResponse.Error -> {
                        _state.update {
                            it.copy(toastMessage = cancelState.message ?: "Cancel failed")
                        }
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
            confirmVM.confirmationState.collect { confirmState ->
                when (confirmState) {
                    is NetworkResponse.Success -> {
                        _state.update {
                            it.copy(toastMessage = "Voyage Confirmed")
                        }
                        confirmVM.resetConfirmationState()
                        loadVoyages()
                    }

                    is NetworkResponse.Error -> {
                        _state.update {
                            it.copy(toastMessage = confirmState.message ?: "Confirmation failed")
                        }
                        confirmVM.resetConfirmationState()
                    }

                    else -> {}
                }
            }
        }
    }

    fun clearToast() {
        _state.update { it.copy(isLoading = false, toastMessage = null) }
    }
}

data class TravelNowUiState(
    val voyage: TravelNowObj? = null,
    val isLoading: Boolean = false,
    val toastMessage: String? = null,
)
