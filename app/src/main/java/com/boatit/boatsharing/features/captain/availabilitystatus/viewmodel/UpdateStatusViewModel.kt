package com.boatit.boatsharing.features.captain.availabilitystatus.viewmodel

import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.R
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.core.presentation.BaseViewModel
import com.boatit.boatsharing.core.presentation.UiEffect
import com.boatit.boatsharing.core.presentation.UiEvent
import com.boatit.boatsharing.core.presentation.UiState
import com.boatit.boatsharing.features.captain.availabilitystatus.model.CaptainAvailabilityRequest
import com.boatit.boatsharing.features.captain.domain.usecase.UpdateCaptainAvailabilityUseCase
import com.boatit.boatsharing.data.local.prefmanager.ICaptainStatusProvider
import kotlinx.coroutines.launch

data class CaptainStatusUiState(
    val title: String = "Welcome!",
    val subtitle: String = "Tap the wheel to go online and start getting voyage requests",
    val image: Int = R.drawable.wheel_inactive,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isOnline: Boolean = false,
) : UiState

sealed interface CaptainStatusUiEvent : UiEvent {
    data class SetOnlineStatus(val isOnline: Boolean) : CaptainStatusUiEvent

    data class ToggleStatus(val userId: String) : CaptainStatusUiEvent
}

sealed interface CaptainStatusUiEffect : UiEffect {
    data class ShowToast(val message: String) : CaptainStatusUiEffect

    data object NavigateToDashboard : CaptainStatusUiEffect

    data object NavigateToOffline : CaptainStatusUiEffect
}

class UpdateStatusViewModel(
    private val updateCaptainAvailabilityUseCase: UpdateCaptainAvailabilityUseCase,
    private val statusProvider: ICaptainStatusProvider,
) : BaseViewModel<CaptainStatusUiState, CaptainStatusUiEvent, CaptainStatusUiEffect>(CaptainStatusUiState()) {
    override fun onEvent(event: CaptainStatusUiEvent) {
        when (event) {
            is CaptainStatusUiEvent.SetOnlineStatus -> setOnlineStatus(event.isOnline)
            is CaptainStatusUiEvent.ToggleStatus -> toggleStatus(event.userId)
        }
    }

    fun setOnlineStatus(isOnline: Boolean) {
        updateState {
            val visuals =
                if (isOnline) {
                    Triple(
                        "You are Online!",
                        "Start accepting voyager and help voyagers reach their destinations.",
                        R.drawable.wheel_icon,
                    )
                } else {
                    Triple(
                        "Welcome!",
                        "Tap the wheel to go online and start getting voyage requests",
                        R.drawable.wheel_inactive,
                    )
                }

            copy(
                title = visuals.first,
                subtitle = visuals.second,
                image = visuals.third,
                isOnline = isOnline,
                errorMessage = null,
            )
        }
    }

    fun toggleStatus(userId: String) {
        if (currentState.isLoading) return

        val previousStatus = statusProvider.isCaptainOnline()
        val newStatus = currentState.isOnline

        updateState { copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            when (
                val result =
                    updateCaptainAvailabilityUseCase(
                        CaptainAvailabilityRequest(userId, newStatus),
                    ).toResource()
            ) {
                is Resource.Success -> {
                    statusProvider.setCaptainStatus(newStatus)
                    updateState { copy(isLoading = false, errorMessage = null, isOnline = newStatus) }
                    emitEffect(CaptainStatusUiEffect.ShowToast(result.data.Message))
                    emitEffect(
                        if (newStatus) {
                            CaptainStatusUiEffect.NavigateToDashboard
                        } else {
                            CaptainStatusUiEffect.NavigateToOffline
                        },
                    )
                }

                is Resource.Error -> {
                    setOnlineStatus(previousStatus)
                    updateState {
                        copy(
                            isLoading = false,
                            errorMessage = result.error.toMessage(),
                            isOnline = previousStatus,
                        )
                    }
                    emitEffect(CaptainStatusUiEffect.ShowToast(result.error.toMessage()))
                }

                Resource.Loading -> {
                    updateState { copy(isLoading = true) }
                }
            }
        }
    }

    fun getCaptainStatus(): Boolean = statusProvider.isCaptainOnline()
}
