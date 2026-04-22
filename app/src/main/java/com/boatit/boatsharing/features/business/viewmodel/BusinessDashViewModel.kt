package com.boatit.boatsharing.features.business.viewmodel

import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.core.presentation.BaseViewModel
import com.boatit.boatsharing.core.presentation.UiEffect
import com.boatit.boatsharing.core.presentation.UiEvent
import com.boatit.boatsharing.core.presentation.UiState
import com.boatit.boatsharing.features.business.domain.usecase.DeleteBusinessDashboardImageUseCase
import com.boatit.boatsharing.features.business.domain.usecase.SaveBusinessDashboardProfileUseCase
import com.boatit.boatsharing.features.business.model.BusinessRequest
import com.boatit.boatsharing.features.business.model.DeleteRequest
import com.boatit.boatsharing.features.signup.business.model.SaveBusinessInfoResponse
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class BusinessDashUiState(
    val isLoading: Boolean = false,
    val response: SaveBusinessInfoResponse? = null,
    val errorMessage: String? = null,
) : UiState

sealed interface BusinessDashUiEvent : UiEvent {
    data class SaveProfile(val profile: BusinessRequest) : BusinessDashUiEvent

    data class DeleteImage(val request: DeleteRequest) : BusinessDashUiEvent

    data object Reset : BusinessDashUiEvent
}

sealed interface BusinessDashUiEffect : UiEffect {
    data class Saved(val response: SaveBusinessInfoResponse) : BusinessDashUiEffect

    data class ImageDeleted(val response: SaveBusinessInfoResponse) : BusinessDashUiEffect

    data class ShowToast(val message: String) : BusinessDashUiEffect
}

class BusinessDashViewModel(
    private val saveBusinessDashboardProfileUseCase: SaveBusinessDashboardProfileUseCase,
    private val deleteBusinessDashboardImageUseCase: DeleteBusinessDashboardImageUseCase,
) : BaseViewModel<BusinessDashUiState, BusinessDashUiEvent, BusinessDashUiEffect>(BusinessDashUiState()) {
    val businessDashState: StateFlow<BusinessDashUiState> = uiState

    override fun onEvent(event: BusinessDashUiEvent) {
        when (event) {
            is BusinessDashUiEvent.SaveProfile -> saveBusinessProfile(event.profile)
            is BusinessDashUiEvent.DeleteImage -> deleteImage(event.request)
            BusinessDashUiEvent.Reset -> resetNearbyPlaces()
        }
    }

    fun saveBusinessProfile(profile: BusinessRequest) {
        viewModelScope.launch {
            updateState { copy(isLoading = true, errorMessage = null) }

            when (val result = saveBusinessDashboardProfileUseCase(profile).toResource()) {
                is Resource.Success -> {
                    updateState {
                        copy(
                            isLoading = false,
                            response = result.data,
                            errorMessage = null,
                        )
                    }
                    emitEffect(BusinessDashUiEffect.Saved(result.data))
                }

                is Resource.Error -> {
                    val message = result.error.toMessage()
                    updateState {
                        copy(
                            isLoading = false,
                            errorMessage = message,
                        )
                    }
                    emitEffect(BusinessDashUiEffect.ShowToast(message))
                }

                Resource.Loading -> {
                    updateState { copy(isLoading = true) }
                }
            }
        }
    }

    fun deleteImage(profile: DeleteRequest) {
        viewModelScope.launch {
            updateState { copy(isLoading = true, errorMessage = null) }

            when (val result = deleteBusinessDashboardImageUseCase(profile).toResource()) {
                is Resource.Success -> {
                    updateState {
                        copy(
                            isLoading = false,
                            response = result.data,
                            errorMessage = null,
                        )
                    }
                    emitEffect(BusinessDashUiEffect.ImageDeleted(result.data))
                }

                is Resource.Error -> {
                    val message = result.error.toMessage()
                    updateState {
                        copy(
                            isLoading = false,
                            errorMessage = message,
                        )
                    }
                    emitEffect(BusinessDashUiEffect.ShowToast(message))
                }

                Resource.Loading -> {
                    updateState { copy(isLoading = true) }
                }
            }
        }
    }

    fun resetNearbyPlaces() {
        updateState {
            copy(
                isLoading = false,
                response = null,
                errorMessage = null,
            )
        }
    }
}
