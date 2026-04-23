package com.boatit.boatsharing.features.signup.business.viewmodel

import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.core.presentation.BaseViewModel
import com.boatit.boatsharing.core.presentation.UiEffect
import com.boatit.boatsharing.core.presentation.UiEvent
import com.boatit.boatsharing.core.presentation.UiState
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.features.signup.business.domain.usecase.SaveBusinessProfileUseCase
import com.boatit.boatsharing.features.signup.business.model.BusinessProfileRequest
import com.boatit.boatsharing.features.signup.business.model.SaveBusinessProfileResponse
import kotlinx.coroutines.launch

data class BusinessProfileUiState(
    val registrationState: NetworkResponse<SaveBusinessProfileResponse> = NetworkResponse.Loading(),
) : UiState

sealed interface BusinessProfileUiEvent : UiEvent {
    data class Save(val profile: BusinessProfileRequest) : BusinessProfileUiEvent
}

sealed interface BusinessProfileUiEffect : UiEffect {
    data object NoOpEffect : BusinessProfileUiEffect
}

class BusinessProfileViewModel(
    private val saveBusinessProfileUseCase: SaveBusinessProfileUseCase,
) : BaseViewModel<BusinessProfileUiState, BusinessProfileUiEvent, BusinessProfileUiEffect>(
        BusinessProfileUiState(),
    ) {
    override fun onEvent(event: BusinessProfileUiEvent) {
        when (event) {
            is BusinessProfileUiEvent.Save -> saveBusinessProfile(event.profile)
        }
    }

    fun saveBusinessProfile(profile: BusinessProfileRequest) {
        viewModelScope.launch {
            updateState { copy(registrationState = NetworkResponse.Loading()) }
            when (val result = saveBusinessProfileUseCase(profile).toResource()) {
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
