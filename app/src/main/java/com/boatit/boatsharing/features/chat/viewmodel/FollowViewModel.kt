package com.boatit.boatsharing.features.chat.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.core.presentation.BaseViewModel
import com.boatit.boatsharing.core.presentation.UiEffect
import com.boatit.boatsharing.core.presentation.UiEvent
import com.boatit.boatsharing.core.presentation.UiState
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.features.chat.domain.usecase.ComplainVoyagerUseCase
import com.boatit.boatsharing.features.chat.domain.usecase.FollowVoyagerUseCase
import com.boatit.boatsharing.features.chat.model.ComplainRequest
import com.boatit.boatsharing.features.chat.model.FollowRequest
import com.boatit.boatsharing.features.chat.model.FollowResponse
import kotlinx.coroutines.launch

data class FollowUiState(
    val nearbyPlaces: NetworkResponse<FollowResponse> = NetworkResponse.Loading(),
) : UiState

sealed interface FollowUiEvent : UiEvent {
    data class Follow(val request: FollowRequest) : FollowUiEvent

    data class Complain(val request: ComplainRequest) : FollowUiEvent

    data object Reset : FollowUiEvent
}

sealed interface FollowUiEffect : UiEffect {
    data object NoOpEffect : FollowUiEffect
}

class FollowViewModel(
    private val followVoyagerUseCase: FollowVoyagerUseCase,
    private val complainVoyagerUseCase: ComplainVoyagerUseCase,
) : BaseViewModel<FollowUiState, FollowUiEvent, FollowUiEffect>(FollowUiState()) {
    override fun onEvent(event: FollowUiEvent) {
        when (event) {
            is FollowUiEvent.Follow -> followFunc(event.request)
            is FollowUiEvent.Complain -> complainFunc(event.request)
            FollowUiEvent.Reset -> resetNearbyPlaces()
        }
    }

    fun followFunc(profile: FollowRequest) =
        viewModelScope.launch {
            updateState { copy(nearbyPlaces = NetworkResponse.Loading()) }
            when (val result = followVoyagerUseCase(profile).toResource()) {
                is Resource.Success -> {
                    updateState { copy(nearbyPlaces = NetworkResponse.Success(result.data)) }
                }

                is Resource.Error -> {
                    val message = result.error.toMessage()
                    Log.e("viewModel", "Error fetching places: $message")
                    updateState { copy(nearbyPlaces = NetworkResponse.Error(result.error)) }
                }

                Resource.Loading -> {
                    updateState { copy(nearbyPlaces = NetworkResponse.Loading()) }
                }
            }
        }

    fun complainFunc(profile: ComplainRequest) =
        viewModelScope.launch {
            updateState { copy(nearbyPlaces = NetworkResponse.Loading()) }
            when (val result = complainVoyagerUseCase(profile).toResource()) {
                is Resource.Success -> {
                    updateState { copy(nearbyPlaces = NetworkResponse.Success(result.data)) }
                }

                is Resource.Error -> {
                    val message = result.error.toMessage()
                    Log.e("viewModel", "Error fetching places: $message")
                    updateState { copy(nearbyPlaces = NetworkResponse.Error(result.error)) }
                }

                Resource.Loading -> {
                    updateState { copy(nearbyPlaces = NetworkResponse.Loading()) }
                }
            }
        }

    fun resetNearbyPlaces() {
        updateState { copy(nearbyPlaces = NetworkResponse.Loading()) }
    }
}
