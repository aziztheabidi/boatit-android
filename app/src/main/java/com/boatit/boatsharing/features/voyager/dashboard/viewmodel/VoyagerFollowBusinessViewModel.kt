package com.boatit.boatsharing.features.voyager.dashboard.viewmodel

import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.core.presentation.BaseViewModel
import com.boatit.boatsharing.core.presentation.UiEffect
import com.boatit.boatsharing.core.presentation.UiEvent
import com.boatit.boatsharing.core.presentation.UiState
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.features.voyager.dashboard.domain.usecase.FollowBusinessUseCase
import com.boatit.boatsharing.features.voyager.dashboard.domain.usecase.UnFollowBusinessUseCase
import com.boatit.boatsharing.features.voyager.dashboard.model.VoyagerFollowBusinessRequest
import com.boatit.boatsharing.features.voyager.dashboard.model.VoyagerFollowBusinessResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class VoyagerFollowBusinessUiState(
    val isLoading: Boolean = false,
    val response: VoyagerFollowBusinessResponse? = null,
    val errorMessage: String? = null,
) : UiState

sealed interface VoyagerFollowBusinessUiEvent : UiEvent {
    data class Follow(val request: VoyagerFollowBusinessRequest) : VoyagerFollowBusinessUiEvent

    data class UnFollow(val request: VoyagerFollowBusinessRequest) : VoyagerFollowBusinessUiEvent
}

sealed interface VoyagerFollowBusinessUiEffect : UiEffect {
    data class Followed(val response: VoyagerFollowBusinessResponse) : VoyagerFollowBusinessUiEffect

    data class UnFollowed(val response: VoyagerFollowBusinessResponse) : VoyagerFollowBusinessUiEffect

    data class ShowToast(val message: String) : VoyagerFollowBusinessUiEffect
}

class VoyagerFollowBusinessViewModel(
    private val followBusinessUseCase: FollowBusinessUseCase,
    private val unFollowBusinessUseCase: UnFollowBusinessUseCase,
) : BaseViewModel<VoyagerFollowBusinessUiState, VoyagerFollowBusinessUiEvent, VoyagerFollowBusinessUiEffect>(
        VoyagerFollowBusinessUiState(),
    ) {
    private val _loginState = MutableStateFlow<NetworkResponse<VoyagerFollowBusinessResponse>>(NetworkResponse.Loading())
    val loginState: StateFlow<NetworkResponse<VoyagerFollowBusinessResponse>> = _loginState

    override fun onEvent(event: VoyagerFollowBusinessUiEvent) {
        when (event) {
            is VoyagerFollowBusinessUiEvent.Follow -> VoyagerFeedbackFunc(event.request)
            is VoyagerFollowBusinessUiEvent.UnFollow -> VoyagerUnFollowFunc(event.request)
        }
    }

    fun VoyagerFeedbackFunc(request: VoyagerFollowBusinessRequest) {
        viewModelScope.launch {
            updateState { copy(isLoading = true, errorMessage = null) }
            _loginState.value = NetworkResponse.Loading()

            when (val result = followBusinessUseCase(request).toResource()) {
                is Resource.Success -> {
                    updateState { copy(isLoading = false, response = result.data, errorMessage = null) }
                    _loginState.value = NetworkResponse.Success(result.data)
                    emitEffect(VoyagerFollowBusinessUiEffect.Followed(result.data))
                }

                is Resource.Error -> {
                    val message = result.error.toMessage()
                    updateState { copy(isLoading = false, errorMessage = message) }
                    _loginState.value = NetworkResponse.Error(result.error)
                    emitEffect(VoyagerFollowBusinessUiEffect.ShowToast(message))
                }

                Resource.Loading -> {
                    updateState { copy(isLoading = true) }
                    _loginState.value = NetworkResponse.Loading()
                }
            }
        }
    }

    fun VoyagerUnFollowFunc(request: VoyagerFollowBusinessRequest) {
        viewModelScope.launch {
            updateState { copy(isLoading = true, errorMessage = null) }
            _loginState.value = NetworkResponse.Loading()

            when (val result = unFollowBusinessUseCase(request).toResource()) {
                is Resource.Success -> {
                    updateState { copy(isLoading = false, response = result.data, errorMessage = null) }
                    _loginState.value = NetworkResponse.Success(result.data)
                    emitEffect(VoyagerFollowBusinessUiEffect.UnFollowed(result.data))
                }

                is Resource.Error -> {
                    val message = result.error.toMessage()
                    updateState { copy(isLoading = false, errorMessage = message) }
                    _loginState.value = NetworkResponse.Error(result.error)
                    emitEffect(VoyagerFollowBusinessUiEffect.ShowToast(message))
                }

                Resource.Loading -> {
                    updateState { copy(isLoading = true) }
                    _loginState.value = NetworkResponse.Loading()
                }
            }
        }
    }
}
