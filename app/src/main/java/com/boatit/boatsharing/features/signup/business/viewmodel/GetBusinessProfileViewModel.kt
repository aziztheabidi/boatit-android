package com.boatit.boatsharing.features.signup.business.viewmodel

import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.core.presentation.BaseViewModel
import com.boatit.boatsharing.core.presentation.UiEffect
import com.boatit.boatsharing.core.presentation.UiEvent
import com.boatit.boatsharing.core.presentation.UiState
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.features.signup.business.domain.usecase.FetchBusinessProfileUseCase
import com.boatit.boatsharing.features.signup.business.model.GetBusinessProfileResponse
import kotlinx.coroutines.launch

data class GetBusinessProfileUiState(
    val registrationState: NetworkResponse<GetBusinessProfileResponse> = NetworkResponse.Loading(),
) : UiState

sealed interface GetBusinessProfileUiEvent : UiEvent {
    data object Fetch : GetBusinessProfileUiEvent
}

sealed interface GetBusinessProfileUiEffect : UiEffect {
    data object NoOpEffect : GetBusinessProfileUiEffect
}

class GetBusinessProfileViewModel(
    private val fetchBusinessProfileUseCase: FetchBusinessProfileUseCase,
) : BaseViewModel<GetBusinessProfileUiState, GetBusinessProfileUiEvent, GetBusinessProfileUiEffect>(
        GetBusinessProfileUiState(),
    ) {
    override fun onEvent(event: GetBusinessProfileUiEvent) {
        when (event) {
            GetBusinessProfileUiEvent.Fetch -> fetchProfile()
        }
    }

    fun GetBusinessProfile() {
        onEvent(GetBusinessProfileUiEvent.Fetch)
    }

    private fun fetchProfile() {
        viewModelScope.launch {
            updateState { copy(registrationState = NetworkResponse.Loading()) }
            when (val result = fetchBusinessProfileUseCase().toResource()) {
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
