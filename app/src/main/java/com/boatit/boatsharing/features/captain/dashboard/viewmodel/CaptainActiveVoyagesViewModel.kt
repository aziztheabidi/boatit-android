package com.boatit.boatsharing.features.captain.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.features.captain.dashboard.model.VoyageData
import com.boatit.boatsharing.features.captain.domain.model.CaptainVoyageDomainModel
import com.boatit.boatsharing.features.captain.domain.usecase.FetchCaptainActiveVoyagesUseCase
import com.boatit.boatsharing.features.captain.voyages.model.CaptainCurrentVoyagesUiEffect
import com.boatit.boatsharing.features.captain.voyages.model.CaptainCurrentVoyagesUiEvent
import com.boatit.boatsharing.features.captain.voyages.model.CaptainCurrentVoyagesUiState
import com.boatit.boatsharing.features.captain.voyages.viewmodel.ICaptainCurrentVoyagesViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CaptainActiveVoyagesViewModel(
    private val fetchCaptainActiveVoyagesUseCase: FetchCaptainActiveVoyagesUseCase,
) : com.boatit.boatsharing.core.presentation.LegacyMviViewModel(), ICaptainCurrentVoyagesViewModel {
    private val _uiState = MutableStateFlow(CaptainCurrentVoyagesUiState())
    override val uiState: StateFlow<CaptainCurrentVoyagesUiState> = _uiState

    private val _uiEffects = MutableSharedFlow<CaptainCurrentVoyagesUiEffect>(extraBufferCapacity = 1)
    override val uiEffects: SharedFlow<CaptainCurrentVoyagesUiEffect> = _uiEffects

    override fun onEvent(event: CaptainCurrentVoyagesUiEvent) {
        when (event) {
            CaptainCurrentVoyagesUiEvent.Initialize,
            CaptainCurrentVoyagesUiEvent.RefreshVoyages,
            -> voyages()
            is CaptainCurrentVoyagesUiEvent.SelectTab -> {
                _uiState.value = _uiState.value.copy(selectedTabIndex = event.index)
            }
        }
    }

    override fun voyages() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            when (val result = fetchCaptainActiveVoyagesUseCase().toResource()) {
                is Resource.Success -> {
                    _uiState.value =
                        _uiState.value.copy(
                            pending = result.data.pending.map { it.toDto() },
                            accepted = result.data.accepted.map { it.toDto() },
                            started = result.data.started.map { it.toDto() },
                            isLoading = false,
                            errorMessage = null,
                        )
                }

                is Resource.Error -> {
                    val message = result.error.toMessage()
                    _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = message)
                    emitUiEffect(CaptainCurrentVoyagesUiEffect.ShowToast(message))
                }

                Resource.Loading -> {
                    _uiState.value = _uiState.value.copy(isLoading = true)
                }
            }
        }
    }

    override fun resetNearbyPlaces() {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
    }

    private fun emitUiEffect(effect: CaptainCurrentVoyagesUiEffect) {
        if (!_uiEffects.tryEmit(effect)) {
            viewModelScope.launch {
                _uiEffects.emit(effect)
            }
        }
    }

    private fun CaptainVoyageDomainModel.toDto(): VoyageData {
        return VoyageData(
            Id = id,
            Name = name,
            VoyagerUserId = voyagerUserId,
            VoyagerName = voyagerName,
            VoyagerPhoneNumber = voyagerPhoneNumber,
            PickupDock = pickupDock,
            PickupDockLatitude = pickupDockLatitude,
            PickupDockLongitude = pickupDockLongitude,
            DropOffDock = dropOffDock,
            DropOffDockLatitude = dropOffDockLatitude,
            DropOffDockLongitude = dropOffDockLongitude,
            NoOfVoyager = noOfVoyager,
            BookingDateTime = bookingDateTime,
            AmountToPay = amountToPay,
            WaterStay = waterStay,
            Duration = duration,
        )
    }
}
