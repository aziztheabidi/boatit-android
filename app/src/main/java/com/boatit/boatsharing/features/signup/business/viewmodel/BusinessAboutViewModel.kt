package com.boatit.boatsharing.features.signup.business.viewmodel

import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.core.presentation.BaseViewModel
import com.boatit.boatsharing.core.presentation.UiEffect
import com.boatit.boatsharing.core.presentation.UiEvent
import com.boatit.boatsharing.core.presentation.UiState
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.features.signup.business.domain.usecase.SaveBusinessAboutUseCase
import com.boatit.boatsharing.features.signup.business.model.SaveBusinessAboutRequest
import com.boatit.boatsharing.features.signup.business.model.SaveBusinessAboutResponse
import kotlinx.coroutines.launch

data class BusinessAboutUiState(
    val registrationState: NetworkResponse<SaveBusinessAboutResponse> = NetworkResponse.Loading(),
    val isSaving: Boolean = false,
) : UiState

sealed interface BusinessAboutUiEvent : UiEvent {
    data class Save(val profile: SaveBusinessAboutRequest) : BusinessAboutUiEvent
}

sealed interface BusinessAboutUiEffect : UiEffect {
    data class ShowSuccessToast(val message: String) : BusinessAboutUiEffect

    data class ShowErrorToast(val message: String) : BusinessAboutUiEffect

    data object NavigateToBusinessLogo : BusinessAboutUiEffect
}

class BusinessAboutViewModel(
    private val saveBusinessAboutUseCase: SaveBusinessAboutUseCase,
) : BaseViewModel<BusinessAboutUiState, BusinessAboutUiEvent, BusinessAboutUiEffect>(
        BusinessAboutUiState(),
    ) {
    override fun onEvent(event: BusinessAboutUiEvent) {
        when (event) {
            is BusinessAboutUiEvent.Save -> saveBusinessAbout(event.profile)
        }
    }

    fun saveBusinessAbout(profile: SaveBusinessAboutRequest) {
        viewModelScope.launch {
            updateState {
                copy(
                    isSaving = true,
                    registrationState = NetworkResponse.Loading(),
                )
            }
            when (val result = saveBusinessAboutUseCase(profile).toResource()) {
                is Resource.Success -> {
                    updateState {
                        copy(
                            registrationState = NetworkResponse.Success(result.data),
                            isSaving = false,
                        )
                    }
                    emitEffect(BusinessAboutUiEffect.ShowSuccessToast(result.data.Message))
                    emitEffect(BusinessAboutUiEffect.NavigateToBusinessLogo)
                }

                is Resource.Error -> {
                    val message = result.error.toMessage()
                    updateState {
                        copy(
                            registrationState = NetworkResponse.Error(result.error),
                            isSaving = false,
                        )
                    }
                    emitEffect(BusinessAboutUiEffect.ShowErrorToast(message))
                }

                Resource.Loading -> {
                    updateState { copy(registrationState = NetworkResponse.Loading()) }
                }
            }
        }
    }
}
