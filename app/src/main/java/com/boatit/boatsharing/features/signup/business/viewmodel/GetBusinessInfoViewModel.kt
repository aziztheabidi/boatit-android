package com.boatit.boatsharing.features.signup.business.viewmodel

import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.core.presentation.BaseViewModel
import com.boatit.boatsharing.core.presentation.UiEffect
import com.boatit.boatsharing.core.presentation.UiEvent
import com.boatit.boatsharing.core.presentation.UiState
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.features.signup.business.domain.usecase.FetchBusinessInfoUseCase
import com.boatit.boatsharing.features.signup.business.model.BusinessInfoResponse
import kotlinx.coroutines.launch

data class GetBusinessInfoUiState(
    val registrationState: NetworkResponse<BusinessInfoResponse> = NetworkResponse.Loading(),
) : UiState

sealed interface GetBusinessInfoUiEvent : UiEvent {
    data object Fetch : GetBusinessInfoUiEvent
}

sealed interface GetBusinessInfoUiEffect : UiEffect {
    data object NoOpEffect : GetBusinessInfoUiEffect
}

class GetBusinessInfoViewModel(
    private val fetchBusinessInfoUseCase: FetchBusinessInfoUseCase,
) : BaseViewModel<GetBusinessInfoUiState, GetBusinessInfoUiEvent, GetBusinessInfoUiEffect>(
        GetBusinessInfoUiState(),
    ) {
    override fun onEvent(event: GetBusinessInfoUiEvent) {
        when (event) {
            GetBusinessInfoUiEvent.Fetch -> GetBusinessProfile()
        }
    }

    fun GetBusinessProfile() {
        viewModelScope.launch {
            updateState { copy(registrationState = NetworkResponse.Loading()) }
            when (val result = fetchBusinessInfoUseCase().toResource()) {
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
