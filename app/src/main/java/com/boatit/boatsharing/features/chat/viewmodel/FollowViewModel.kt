package com.boatit.boatsharing.features.chat.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.features.chat.domain.usecase.ComplainVoyagerUseCase
import com.boatit.boatsharing.features.chat.domain.usecase.FollowVoyagerUseCase
import com.boatit.boatsharing.features.chat.model.ComplainRequest
import com.boatit.boatsharing.features.chat.model.FollowRequest
import com.boatit.boatsharing.features.chat.model.FollowResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FollowViewModel(
    private val followVoyagerUseCase: FollowVoyagerUseCase,
    private val complainVoyagerUseCase: ComplainVoyagerUseCase,
) : com.boatit.boatsharing.core.presentation.LegacyMviViewModel() {
    private val _nearbyPlaces = MutableStateFlow<NetworkResponse<FollowResponse>>(NetworkResponse.Loading())
    val nearbyPlaces: StateFlow<NetworkResponse<FollowResponse>> = _nearbyPlaces.asStateFlow()

    fun followFunc(profile: FollowRequest) =
        viewModelScope.launch {
            _nearbyPlaces.value = NetworkResponse.Loading()
            when (val result = followVoyagerUseCase(profile).toResource()) {
                is Resource.Success -> {
                    _nearbyPlaces.value = NetworkResponse.Success(result.data)
                }

                is Resource.Error -> {
                    val message = result.error.toMessage()
                    Log.e("viewModel", "Error fetching places: $message")
                    _nearbyPlaces.value = NetworkResponse.Error(result.error)
                }

                Resource.Loading -> {
                    _nearbyPlaces.value = NetworkResponse.Loading()
                }
            }
        }

    fun complainFunc(profile: ComplainRequest) =
        viewModelScope.launch {
            _nearbyPlaces.value = NetworkResponse.Loading()
            when (val result = complainVoyagerUseCase(profile).toResource()) {
                is Resource.Success -> {
                    _nearbyPlaces.value = NetworkResponse.Success(result.data)
                }

                is Resource.Error -> {
                    val message = result.error.toMessage()
                    Log.e("viewModel", "Error fetching places: $message")
                    _nearbyPlaces.value = NetworkResponse.Error(result.error)
                }

                Resource.Loading -> {
                    _nearbyPlaces.value = NetworkResponse.Loading()
                }
            }
        }

    fun resetNearbyPlaces() {
        _nearbyPlaces.value = NetworkResponse.Loading()
    }
}
