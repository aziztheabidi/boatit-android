package com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.network.networkreposne.NetworkResponse
import com.boatit.boatsharing.ui.captain.availablitystatus.model.CaptainAvailabilityRequest
import com.boatit.boatsharing.ui.captain.availablitystatus.model.CaptainAvailabilityResponse
import com.boatit.boatsharing.ui.captain.availablitystatus.repository.UpdateStatusRepository
import com.boatit.boatsharing.ui.captain.dashbaord.model.AcceptVoyageRequest
import com.boatit.boatsharing.ui.captain.dashbaord.model.AcceptVoyageResponse
import com.boatit.boatsharing.ui.captain.dashbaord.model.VoyageStartRequest
import com.boatit.boatsharing.ui.captain.dashbaord.model.VoyageStartResponse
import com.boatit.boatsharing.ui.captain.dashbaord.repository.AcceptRequestRepository
import com.boatit.boatsharing.ui.captain.dashbaord.repository.StartVoyageRepository
import com.boatit.boatsharing.ui.captain.voyages.model.CaptainVoyages
import com.boatit.boatsharing.ui.captain.voyages.model.CaptainVoyagesResponse
import com.boatit.boatsharing.ui.captain.voyages.repository.CaptainVoyagesRepository
import com.boatit.boatsharing.ui.login.model.LoginResponse
import com.boatit.boatsharing.ui.login.model.UserData
import com.boatit.boatsharing.ui.login.repository.LoginRepository
import com.boatit.boatsharing.ui.voyager.dashbaord.model.FutureBookedVoyages
import com.boatit.boatsharing.ui.voyager.dashbaord.model.TravelNowObj
import com.boatit.boatsharing.ui.voyager.dashbaord.model.TravelNowResponse
import com.boatit.boatsharing.ui.voyager.dashbaord.model.VoyagerVoyagesResponse
import com.boatit.boatsharing.ui.voyager.dashbaord.repository.FutureVoyagesRepo
import com.boatit.boatsharing.ui.voyager.dashbaord.repository.TravelNowRepo
import com.boatit.boatsharing.ui.voyager.dashbaord.repository.VoyagerVoyagesRepository
import com.boatit.boatsharing.utils.prefmanager.SharedPrefManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class TravelNowViewModel(
    private val repository: TravelNowRepo,
    private val cancelVM: CancelBookedVoyageViewModel,
    private val confirmVM: ConfirmBookedVoyageViewModel
) : ViewModel() {

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

            val result = repository.voyages()
            result.onSuccess { response ->
                _state.update {
                    it.copy(
                        voyage = response.obj,
                        isLoading = false,
                        toastMessage = "Success"
                    )
                }
            }.onFailure { error ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        toastMessage = error.message ?: "Failed to load voyage"
                    )
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
            confirmVM.nearbyPlaces.collect { confirmState ->
                when (confirmState) {
                    is NetworkResponse.Success -> {
                        _state.update {
                            it.copy(toastMessage = "Voyage Confirmed")
                        }
                        confirmVM.resetNearbyPlaces()
                        loadVoyages()
                    }

                    is NetworkResponse.Error -> {
                        _state.update {
                            it.copy(toastMessage = confirmState.message ?: "Confirmation failed")
                        }
                        confirmVM.resetNearbyPlaces()
                    }

                    else -> {}
                }
            }
        }
    }

    fun clearToast() {
        _state.update { it.copy(toastMessage = null) }
    }
}


data class TravelNowUiState(
    val voyage: TravelNowObj? = null,
    val isLoading: Boolean = false,
    val toastMessage: String? = null
)


