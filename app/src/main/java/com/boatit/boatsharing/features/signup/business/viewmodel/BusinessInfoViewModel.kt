package com.boatit.boatsharing.features.signup.business.viewmodel

import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.core.presentation.BaseViewModel
import com.boatit.boatsharing.core.presentation.UiEffect
import com.boatit.boatsharing.core.presentation.UiEvent
import com.boatit.boatsharing.core.presentation.UiState
import com.boatit.boatsharing.data.local.prefmanager.UserSessionStore
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.features.signup.business.domain.usecase.SaveBusinessInfoUseCase
import com.boatit.boatsharing.features.signup.business.model.BusinessInfoRequest
import com.boatit.boatsharing.features.signup.business.model.BusinessInfoResponse
import com.boatit.boatsharing.features.signup.business.model.SaveBusinessInfoResponse
import kotlinx.coroutines.launch

data class BusinessInfoUiState(
    val businessName: String = "",
    val businessType: String = "",
    val businessAddress: String = "",
    val businessPhoneNo: String = "",
    val establishmentYear: String = "",
    val businessTime: String = "",
    val bookingDate: String = "",
    val showDialog: Boolean = false,
    val registrationState: NetworkResponse<SaveBusinessInfoResponse> = NetworkResponse.Loading(),
) : UiState

sealed interface BusinessInfoUiEvent : UiEvent {
    data class BusinessNameChanged(val value: String) : BusinessInfoUiEvent

    data class BusinessTypeChanged(val value: String) : BusinessInfoUiEvent

    data class BusinessAddressChanged(val value: String) : BusinessInfoUiEvent

    data class BusinessPhoneChanged(val value: String) : BusinessInfoUiEvent

    data class EstablishmentYearChanged(val value: String) : BusinessInfoUiEvent

    data class BusinessTimeChanged(val value: String) : BusinessInfoUiEvent

    data class BookingDateChanged(val value: String) : BusinessInfoUiEvent

    data class ShowDialog(val show: Boolean) : BusinessInfoUiEvent

    data class LoadInitial(val data: BusinessInfoResponse?) : BusinessInfoUiEvent

    data object SaveProfile : BusinessInfoUiEvent
}

sealed interface BusinessInfoUiEffect : UiEffect {
    data object NoOpEffect : BusinessInfoUiEffect
}

class BusinessInfoViewModel(
    private val saveBusinessInfoUseCase: SaveBusinessInfoUseCase,
    private val userSessionStore: UserSessionStore,
) : BaseViewModel<BusinessInfoUiState, BusinessInfoUiEvent, BusinessInfoUiEffect>(BusinessInfoUiState()) {
    override fun onEvent(event: BusinessInfoUiEvent) {
        when (event) {
            is BusinessInfoUiEvent.BusinessNameChanged -> updateState { copy(businessName = event.value) }
            is BusinessInfoUiEvent.BusinessTypeChanged -> updateState { copy(businessType = event.value) }
            is BusinessInfoUiEvent.BusinessAddressChanged -> updateState { copy(businessAddress = event.value) }
            is BusinessInfoUiEvent.BusinessPhoneChanged -> updateState { copy(businessPhoneNo = event.value) }
            is BusinessInfoUiEvent.EstablishmentYearChanged -> updateState { copy(establishmentYear = event.value) }
            is BusinessInfoUiEvent.BusinessTimeChanged -> updateState { copy(businessTime = event.value) }
            is BusinessInfoUiEvent.BookingDateChanged -> updateState { copy(bookingDate = event.value) }
            is BusinessInfoUiEvent.ShowDialog -> updateState { copy(showDialog = event.show) }
            is BusinessInfoUiEvent.LoadInitial -> loadInitialData(event.data)
            BusinessInfoUiEvent.SaveProfile -> saveBusinessProfile()
        }
    }

    fun loadInitialData(data: BusinessInfoResponse?) {
        updateState {
            copy(
                businessName = data?.obj?.Name.orEmpty(),
                businessType = data?.obj?.Type.orEmpty(),
                businessAddress = data?.obj?.Address.orEmpty(),
                businessPhoneNo = data?.obj?.PhoneNumber.orEmpty(),
                establishmentYear = data?.obj?.YearOfEstablishment.toString().orEmpty(),
                businessTime = data?.obj?.Time.orEmpty(),
                bookingDate = data?.obj?.Description.orEmpty(),
            )
        }
    }

    fun saveBusinessProfile() {
        val s = currentState
        viewModelScope.launch {
            updateState { copy(registrationState = NetworkResponse.Loading()) }
            when (
                val result =
                    saveBusinessInfoUseCase(
                        BusinessInfoRequest(
                            UserId = userSessionStore.currentUserId(),
                            Name = s.businessName,
                            Type = s.businessType,
                            Address = s.businessAddress,
                            PhoneNumber = s.businessPhoneNo,
                            YearOfEstablishment = s.establishmentYear,
                            Time = s.businessTime,
                        ),
                    ).toResource()
            ) {
                is Resource.Success -> {
                    updateState { copy(registrationState = NetworkResponse.Success(result.data)) }
                }

                is Resource.Error -> {
                    updateState { copy(registrationState = NetworkResponse.Error(result.error)) }
                }

                Resource.Loading -> {
                    updateState { copy(registrationState = NetworkResponse.Loading()) }
                }
            }
        }
    }
}
