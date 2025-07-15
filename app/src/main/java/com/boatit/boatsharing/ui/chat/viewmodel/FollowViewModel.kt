package com.boatit.boatsharing.ui.chat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.util.Log
import com.boatit.boatsharing.network.networkreposne.NetworkResponse
import com.boatit.boatsharing.ui.chat.model.ComplainRequest
import com.boatit.boatsharing.ui.chat.model.FollowRequest
import com.boatit.boatsharing.ui.chat.model.FollowResponse
import com.boatit.boatsharing.ui.chat.repository.FollowRepository
import com.boatit.boatsharing.ui.voyager.dashbaord.model.CancelBookedVoyageResponse
import com.boatit.boatsharing.ui.voyager.dashbaord.model.CancelBookedVoyages
import com.boatit.boatsharing.ui.voyager.dashbaord.model.ConfirmBookedVoyageResponse
import com.boatit.boatsharing.ui.voyager.dashbaord.model.ConfirmBookedVoyages
import com.boatit.boatsharing.ui.voyager.dashbaord.model.FindBoatRequest
import com.boatit.boatsharing.ui.voyager.dashbaord.model.FindBoatResponse
import com.boatit.boatsharing.ui.voyager.dashbaord.model.Place
import com.boatit.boatsharing.ui.voyager.dashbaord.repository.CancelBookedVoyageRepository
import com.boatit.boatsharing.ui.voyager.dashbaord.repository.ConfirmBookedVoyageRepository
import com.boatit.boatsharing.ui.voyager.dashbaord.repository.FetchNearByVoyagesRepo
import com.boatit.boatsharing.ui.voyager.dashbaord.repository.FindBoatRepo
import com.boatit.boatsharing.utils.AppConstants

class FollowViewModel(
    private val repository: FollowRepository
) : ViewModel() {

    private val _nearbyPlaces = MutableStateFlow<NetworkResponse<FollowResponse>>(NetworkResponse.Loading())
    val nearbyPlaces: StateFlow<NetworkResponse<FollowResponse>> = _nearbyPlaces.asStateFlow()

    fun followFunc(profile: FollowRequest) = viewModelScope.launch {
        _nearbyPlaces.value = NetworkResponse.Loading()
        val result = repository.findboat(profile)
        result.onSuccess { placesResponse ->
            _nearbyPlaces.value = NetworkResponse.Success(placesResponse)
        }.onFailure { exception ->
            Log.e("viewModel", "Error fetching places: ${exception.localizedMessage}", exception)
            _nearbyPlaces.value = NetworkResponse.Error("An error occurred: ${exception.localizedMessage}")
        }
    }

    fun complainFunc(profile: ComplainRequest) = viewModelScope.launch {
        _nearbyPlaces.value = NetworkResponse.Loading()
        val result = repository.complian(profile)
        result.onSuccess { placesResponse ->
            _nearbyPlaces.value = NetworkResponse.Success(placesResponse)
        }.onFailure { exception ->
            Log.e("viewModel", "Error fetching places: ${exception.localizedMessage}", exception)
            _nearbyPlaces.value = NetworkResponse.Error("An error occurred: ${exception.localizedMessage}")
        }
    }

    fun resetNearbyPlaces() {
        _nearbyPlaces.value = NetworkResponse.Loading()
    }
}


