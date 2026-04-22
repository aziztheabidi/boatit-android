package com.boatit.boatsharing.features.business.viewmodel

import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.requiresReLogin
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.core.presentation.BaseViewModel
import com.boatit.boatsharing.core.presentation.UiEffect
import com.boatit.boatsharing.core.presentation.UiEvent
import com.boatit.boatsharing.core.presentation.UiState
import com.boatit.boatsharing.features.business.domain.usecase.FetchBusinessDashboardProfileUseCase
import com.boatit.boatsharing.features.business.domain.usecase.FetchBusinessDocksUseCase
import com.boatit.boatsharing.features.business.model.DocksDropdownResponse
import com.boatit.boatsharing.features.business.model.GetBusinessResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class GetBusinessUiState(
    val isProfileLoading: Boolean = false,
    val isDocksLoading: Boolean = false,
    val profile: GetBusinessResponse? = null,
    val docks: DocksDropdownResponse? = null,
    val profileErrorMessage: String? = null,
    val docksErrorMessage: String? = null,
) : UiState

sealed interface GetBusinessUiEvent : UiEvent {
    data object FetchProfile : GetBusinessUiEvent

    data object FetchDocks : GetBusinessUiEvent

    data object ResetProfile : GetBusinessUiEvent

    data object ResetDocks : GetBusinessUiEvent
}

sealed interface GetBusinessUiEffect : UiEffect {
    data class ShowProfileError(val message: String) : GetBusinessUiEffect

    data class ShowDocksError(val message: String) : GetBusinessUiEffect

    data object SessionExpired : GetBusinessUiEffect
}

class GetBusinessViewModel(
    private val fetchBusinessDashboardProfileUseCase: FetchBusinessDashboardProfileUseCase,
    private val fetchBusinessDocksUseCase: FetchBusinessDocksUseCase,
) : BaseViewModel<GetBusinessUiState, GetBusinessUiEvent, GetBusinessUiEffect>(GetBusinessUiState()) {
    val businessState: StateFlow<GetBusinessUiState> = uiState

    private val _logoutEvent = MutableStateFlow(false)
    val logoutEvent = _logoutEvent.asStateFlow()

    override fun onEvent(event: GetBusinessUiEvent) {
        when (event) {
            GetBusinessUiEvent.FetchProfile -> voyages()
            GetBusinessUiEvent.FetchDocks -> docks()
            GetBusinessUiEvent.ResetProfile -> resetNearbyPlaces()
            GetBusinessUiEvent.ResetDocks -> resetDocks()
        }
    }

    fun voyages() {
        viewModelScope.launch {
            updateState {
                copy(
                    isProfileLoading = true,
                    profileErrorMessage = null,
                )
            }

            when (val result = fetchBusinessDashboardProfileUseCase().toResource()) {
                is Resource.Success -> {
                    updateState {
                        copy(
                            isProfileLoading = false,
                            profile = result.data,
                            profileErrorMessage = null,
                        )
                    }
                }

                is Resource.Error -> {
                    val message = result.error.toMessage()
                    if (result.error.requiresReLogin()) {
                        _logoutEvent.value = true
                        emitEffect(GetBusinessUiEffect.SessionExpired)
                    }
                    updateState {
                        copy(
                            isProfileLoading = false,
                            profileErrorMessage = message,
                        )
                    }
                    emitEffect(GetBusinessUiEffect.ShowProfileError(message))
                }

                Resource.Loading -> {
                    updateState { copy(isProfileLoading = true) }
                }
            }
        }
    }

    fun docks() {
        viewModelScope.launch {
            updateState {
                copy(
                    isDocksLoading = true,
                    docksErrorMessage = null,
                )
            }

            when (val result = fetchBusinessDocksUseCase().toResource()) {
                is Resource.Success -> {
                    updateState {
                        copy(
                            isDocksLoading = false,
                            docks = result.data,
                            docksErrorMessage = null,
                        )
                    }
                }

                is Resource.Error -> {
                    val message = result.error.toMessage()
                    if (result.error.requiresReLogin()) {
                        _logoutEvent.value = true
                        emitEffect(GetBusinessUiEffect.SessionExpired)
                    }
                    updateState {
                        copy(
                            isDocksLoading = false,
                            docksErrorMessage = message,
                        )
                    }
                    emitEffect(GetBusinessUiEffect.ShowDocksError(message))
                }

                Resource.Loading -> {
                    updateState { copy(isDocksLoading = true) }
                }
            }
        }
    }

    fun resetNearbyPlaces() {
        updateState {
            copy(
                isProfileLoading = false,
                profileErrorMessage = null,
            )
        }
    }

    fun resetDocks() {
        updateState {
            copy(
                isDocksLoading = false,
                docksErrorMessage = null,
            )
        }
    }
}
