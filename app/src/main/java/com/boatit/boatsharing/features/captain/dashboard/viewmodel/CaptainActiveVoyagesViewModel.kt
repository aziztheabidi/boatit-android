package com.boatit.boatsharing.features.captain.dashboard.viewmodel

import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.core.presentation.BaseViewModel
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.features.captain.dashboard.model.VoyageData
import com.boatit.boatsharing.features.captain.domain.model.CaptainVoyageDomainModel
import com.boatit.boatsharing.features.captain.domain.usecase.FetchCaptainActiveVoyagesUseCase
import com.boatit.boatsharing.features.captain.voyages.model.CaptainCurrentVoyagesUiEffect
import com.boatit.boatsharing.features.captain.voyages.model.CaptainCurrentVoyagesUiEvent
import com.boatit.boatsharing.features.captain.voyages.model.CaptainCurrentVoyagesUiState
import com.boatit.boatsharing.features.captain.voyages.viewmodel.ICaptainCurrentVoyagesViewModel
import kotlinx.coroutines.launch

class CaptainActiveVoyagesViewModel(
    private val fetchCaptainActiveVoyagesUseCase: FetchCaptainActiveVoyagesUseCase,
) : BaseViewModel<CaptainCurrentVoyagesUiState, CaptainCurrentVoyagesUiEvent, CaptainCurrentVoyagesUiEffect>(
        CaptainCurrentVoyagesUiState(),
    ),
    ICaptainCurrentVoyagesViewModel {
    override fun onEvent(event: CaptainCurrentVoyagesUiEvent) {
        when (event) {
            CaptainCurrentVoyagesUiEvent.Initialize,
            CaptainCurrentVoyagesUiEvent.RefreshVoyages,
            -> voyages()
            is CaptainCurrentVoyagesUiEvent.SelectTab -> {
                updateState { copy(selectedTabIndex = event.index) }
            }
        }
    }

    override fun voyages() {
        viewModelScope.launch {
            updateState { copy(isLoading = true, errorMessage = null) }
            when (val result = fetchCaptainActiveVoyagesUseCase().toResource()) {
                is Resource.Success -> {
                    updateState {
                        copy(
                            pending = result.data.pending.map { it.toDto() },
                            accepted = result.data.accepted.map { it.toDto() },
                            started = result.data.started.map { it.toDto() },
                            isLoading = false,
                            errorMessage = null,
                        )
                    }
                }

                is Resource.Error -> {
                    val message = result.error.toMessage()
                    updateState { copy(isLoading = false, errorMessage = message) }
                    emitEffect(CaptainCurrentVoyagesUiEffect.ShowToast(message))
                }

                Resource.Loading -> {
                    updateState { copy(isLoading = true) }
                }
            }
        }
    }

    override fun resetNearbyPlaces() {
        updateState { copy(isLoading = true, errorMessage = null) }
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
