package com.boatit.boatsharing.features.signup.captain.viewmodel

import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.core.presentation.BaseViewModel
import com.boatit.boatsharing.core.presentation.UiEffect
import com.boatit.boatsharing.core.presentation.UiEvent
import com.boatit.boatsharing.core.presentation.UiState
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.features.signup.captain.domain.usecase.FetchCaptainProfileUseCase
import com.boatit.boatsharing.features.signup.captain.model.GetCaptainProfileResponse
import kotlinx.coroutines.launch

data class GetCaptainProfileUiState(
    val registrationState: NetworkResponse<GetCaptainProfileResponse> = NetworkResponse.Loading(),
) : UiState

sealed interface GetCaptainProfileUiEvent : UiEvent {
    data object Fetch : GetCaptainProfileUiEvent
}

sealed interface GetCaptainProfileUiEffect : UiEffect {
    data object NoOpEffect : GetCaptainProfileUiEffect
}

class GetCaptainProfileViewModel(
    private val fetchCaptainProfileUseCase: FetchCaptainProfileUseCase,
) : BaseViewModel<GetCaptainProfileUiState, GetCaptainProfileUiEvent, GetCaptainProfileUiEffect>(
        GetCaptainProfileUiState(),
    ) {
    override fun onEvent(event: GetCaptainProfileUiEvent) {
        when (event) {
            GetCaptainProfileUiEvent.Fetch -> fetchProfile()
        }
    }

    fun GetCaptainProfile() {
        onEvent(GetCaptainProfileUiEvent.Fetch)
    }

    private fun fetchProfile() {
        viewModelScope.launch {
            updateState { copy(registrationState = NetworkResponse.Loading()) }
            when (val result = fetchCaptainProfileUseCase().toResource()) {
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
