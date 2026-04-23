package com.boatit.boatsharing.features.userroles.viewmodel

import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.core.presentation.BaseViewModel
import com.boatit.boatsharing.core.presentation.UiEffect
import com.boatit.boatsharing.core.presentation.UiEvent
import com.boatit.boatsharing.core.presentation.UiState
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.features.userroles.domain.model.DeviceTokenUpdateDomainModel
import com.boatit.boatsharing.features.userroles.domain.usecase.UpdateDeviceTokenUseCase
import kotlinx.coroutines.launch

data class FCMTokenUiState(
    val tokenUpdateState: NetworkResponse<DeviceTokenUpdateDomainModel> = NetworkResponse.Loading(),
) : UiState

sealed interface FCMTokenUiEvent : UiEvent {
    data class UpdateFcmToken(
        val userId: String,
        val token: String,
    ) : FCMTokenUiEvent
}

sealed interface FCMTokenUiEffect : UiEffect {
    data object NoOpEffect : FCMTokenUiEffect
}

class FCMTokenViewModel(
    private val updateDeviceTokenUseCase: UpdateDeviceTokenUseCase,
) : BaseViewModel<FCMTokenUiState, FCMTokenUiEvent, FCMTokenUiEffect>(FCMTokenUiState()) {
    override fun onEvent(event: FCMTokenUiEvent) {
        when (event) {
            is FCMTokenUiEvent.UpdateFcmToken -> performUpdateFcmToken(event.userId, event.token)
        }
    }

    fun updateFcmToken(
        userId: String,
        token: String,
    ) {
        onEvent(FCMTokenUiEvent.UpdateFcmToken(userId, token))
    }

    private fun performUpdateFcmToken(
        userId: String,
        token: String,
    ) {
        viewModelScope.launch {
            updateState { copy(tokenUpdateState = NetworkResponse.Loading()) }
            when (val result = updateDeviceTokenUseCase(userId, token).toResource()) {
                is Resource.Success -> {
                    updateState { copy(tokenUpdateState = NetworkResponse.Success(result.data)) }
                }

                is Resource.Error -> {
                    updateState { copy(tokenUpdateState = NetworkResponse.Error(result.error)) }
                }

                Resource.Loading -> {
                    updateState { copy(tokenUpdateState = NetworkResponse.Loading()) }
                }
            }
        }
    }
}
