package com.boatit.boatsharing.features.signup.captain.viewmodel

import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.core.presentation.BaseViewModel
import com.boatit.boatsharing.core.presentation.UiEffect
import com.boatit.boatsharing.core.presentation.UiEvent
import com.boatit.boatsharing.core.presentation.UiState
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.features.signup.captain.domain.usecase.FetchCaptainBoatUseCase
import com.boatit.boatsharing.features.signup.captain.model.GetCaptainBoatResponse
import kotlinx.coroutines.launch

data class GetCaptainBoatUiState(
    val registrationState: NetworkResponse<GetCaptainBoatResponse> = NetworkResponse.Loading(),
) : UiState

sealed interface GetCaptainBoatUiEvent : UiEvent {
    data object Fetch : GetCaptainBoatUiEvent
}

sealed interface GetCaptainBoatUiEffect : UiEffect {
    data object NoOpEffect : GetCaptainBoatUiEffect
}

class GetCaptainBoatViewModel(
    private val fetchCaptainBoatUseCase: FetchCaptainBoatUseCase,
) : BaseViewModel<GetCaptainBoatUiState, GetCaptainBoatUiEvent, GetCaptainBoatUiEffect>(
        GetCaptainBoatUiState(),
    ) {
    override fun onEvent(event: GetCaptainBoatUiEvent) {
        when (event) {
            GetCaptainBoatUiEvent.Fetch -> fetchBoat()
        }
    }

    fun GetCaptainBoat() {
        onEvent(GetCaptainBoatUiEvent.Fetch)
    }

    private fun fetchBoat() {
        viewModelScope.launch {
            updateState { copy(registrationState = NetworkResponse.Loading()) }
            when (val result = fetchCaptainBoatUseCase().toResource()) {
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
