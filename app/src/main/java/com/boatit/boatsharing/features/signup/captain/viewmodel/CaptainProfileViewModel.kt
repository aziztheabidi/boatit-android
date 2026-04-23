package com.boatit.boatsharing.features.signup.captain.viewmodel

import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.core.presentation.BaseViewModel
import com.boatit.boatsharing.core.presentation.UiEffect
import com.boatit.boatsharing.core.presentation.UiEvent
import com.boatit.boatsharing.core.presentation.UiState
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.features.signup.captain.domain.usecase.SaveCaptainProfileUseCase
import com.boatit.boatsharing.features.signup.captain.model.CaptainProfileRequest
import com.boatit.boatsharing.features.signup.captain.model.CaptainProfileResponse
import kotlinx.coroutines.launch

data class CaptainProfileUiState(
    val registrationState: NetworkResponse<CaptainProfileResponse> = NetworkResponse.Loading(),
) : UiState

sealed interface CaptainProfileUiEvent : UiEvent {
    data class Save(val profile: CaptainProfileRequest) : CaptainProfileUiEvent
}

sealed interface CaptainProfileUiEffect : UiEffect {
    data object NoOpEffect : CaptainProfileUiEffect
}

class CaptainProfileViewModel(
    private val saveCaptainProfileUseCase: SaveCaptainProfileUseCase,
) : BaseViewModel<CaptainProfileUiState, CaptainProfileUiEvent, CaptainProfileUiEffect>(
        CaptainProfileUiState(),
    ) {
    override fun onEvent(event: CaptainProfileUiEvent) {
        when (event) {
            is CaptainProfileUiEvent.Save -> saveProfile(event.profile)
        }
    }

    fun saveProfile(profile: CaptainProfileRequest) {
        viewModelScope.launch {
            updateState { copy(registrationState = NetworkResponse.Loading()) }
            when (val result = saveCaptainProfileUseCase(profile).toResource()) {
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
