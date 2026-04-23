package com.boatit.boatsharing.features.voyager.dashboard.viewmodel

import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.core.presentation.BaseViewModel
import com.boatit.boatsharing.core.presentation.UiEffect
import com.boatit.boatsharing.core.presentation.UiEvent
import com.boatit.boatsharing.core.presentation.UiState
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.features.voyager.dashboard.domain.usecase.FetchFollowedVoyagersUseCase
import com.boatit.boatsharing.features.voyager.dashboard.model.FollowedVoyagersResponse
import kotlinx.coroutines.launch

data class FollowedVoyagerUiState(
    val registrationState: NetworkResponse<FollowedVoyagersResponse> = NetworkResponse.Loading(),
) : UiState

sealed interface FollowedVoyagerUiEvent : UiEvent {
    data object LoadFollowedVoyagers : FollowedVoyagerUiEvent
}

sealed interface FollowedVoyagerUiEffect : UiEffect {
    data object NoOpEffect : FollowedVoyagerUiEffect
}

class FollowedVoyagerViewModel(
    private val fetchFollowedVoyagersUseCase: FetchFollowedVoyagersUseCase,
) : BaseViewModel<FollowedVoyagerUiState, FollowedVoyagerUiEvent, FollowedVoyagerUiEffect>(
        FollowedVoyagerUiState(),
    ) {
    override fun onEvent(event: FollowedVoyagerUiEvent) {
        when (event) {
            FollowedVoyagerUiEvent.LoadFollowedVoyagers -> followedVoyagerFunc()
        }
    }

    private fun followedVoyagerFunc() {
        viewModelScope.launch {
            updateState { copy(registrationState = NetworkResponse.Loading()) }
            when (val result = fetchFollowedVoyagersUseCase().toResource()) {
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
