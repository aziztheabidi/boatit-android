package com.boatit.boatsharing.features.voyager.dashboard.viewmodel

import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.core.presentation.BaseViewModel
import com.boatit.boatsharing.core.presentation.UiEffect
import com.boatit.boatsharing.core.presentation.UiEvent
import com.boatit.boatsharing.core.presentation.UiState
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.features.voyager.dashboard.domain.usecase.SubmitVoyagerFeedbackUseCase
import com.boatit.boatsharing.features.voyager.dashboard.model.VoyagerFeedbackRequest
import com.boatit.boatsharing.features.voyager.dashboard.model.VoyagerFeedbackResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class VoyagerFeedbackUiState(
    val isLoading: Boolean = false,
    val feedbackResponse: VoyagerFeedbackResponse? = null,
    val errorMessage: String? = null,
) : UiState

sealed interface VoyagerFeedbackUiEvent : UiEvent {
    data class Submit(val request: VoyagerFeedbackRequest) : VoyagerFeedbackUiEvent
}

sealed interface VoyagerFeedbackUiEffect : UiEffect {
    data class Submitted(val response: VoyagerFeedbackResponse) : VoyagerFeedbackUiEffect

    data class ShowToast(val message: String) : VoyagerFeedbackUiEffect
}

class VoyagerFeedbackViewModel(
    private val submitVoyagerFeedbackUseCase: SubmitVoyagerFeedbackUseCase,
) : BaseViewModel<VoyagerFeedbackUiState, VoyagerFeedbackUiEvent, VoyagerFeedbackUiEffect>(VoyagerFeedbackUiState()) {
    private val _loginState = MutableStateFlow<NetworkResponse<VoyagerFeedbackResponse>>(NetworkResponse.Loading())
    val loginState: StateFlow<NetworkResponse<VoyagerFeedbackResponse>> = _loginState

    override fun onEvent(event: VoyagerFeedbackUiEvent) {
        when (event) {
            is VoyagerFeedbackUiEvent.Submit -> VoyagerFeedbackFunc(event.request)
        }
    }

    fun VoyagerFeedbackFunc(request: VoyagerFeedbackRequest) {
        viewModelScope.launch {
            updateState { copy(isLoading = true, errorMessage = null) }
            _loginState.value = NetworkResponse.Loading()

            when (val result = submitVoyagerFeedbackUseCase(request).toResource()) {
                is Resource.Success -> {
                    updateState { copy(isLoading = false, feedbackResponse = result.data, errorMessage = null) }
                    _loginState.value = NetworkResponse.Success(result.data)
                    emitEffect(VoyagerFeedbackUiEffect.Submitted(result.data))
                }

                is Resource.Error -> {
                    val message = result.error.toMessage()
                    updateState { copy(isLoading = false, errorMessage = message) }
                    _loginState.value = NetworkResponse.Error(result.error)
                    emitEffect(VoyagerFeedbackUiEffect.ShowToast(message))
                }

                Resource.Loading -> {
                    updateState { copy(isLoading = true) }
                    _loginState.value = NetworkResponse.Loading()
                }
            }
        }
    }
}
