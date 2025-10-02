package com.boatit.boatsharing.ui.voyager.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.util.Log
import com.boatit.boatsharing.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.ui.voyager.dashboard.model.CancelBookedVoyageResponse
import com.boatit.boatsharing.ui.voyager.dashboard.model.CancelBookedVoyages
import com.boatit.boatsharing.ui.voyager.dashboard.model.ConfirmBookedVoyageResponse
import com.boatit.boatsharing.ui.voyager.dashboard.model.ConfirmBookedVoyages
import com.boatit.boatsharing.ui.voyager.dashboard.model.FindBoatRequest
import com.boatit.boatsharing.ui.voyager.dashboard.model.FindBoatResponse
import com.boatit.boatsharing.ui.voyager.dashboard.model.Place
import com.boatit.boatsharing.ui.voyager.dashboard.repository.CancelBookedVoyageRepository
import com.boatit.boatsharing.ui.voyager.dashboard.repository.ConfirmBookedVoyageRepository
import com.boatit.boatsharing.ui.voyager.dashboard.repository.FetchNearByVoyagesRepo
import com.boatit.boatsharing.ui.voyager.dashboard.repository.FindBoatRepo
import com.boatit.boatsharing.utils.AppConstants

class CancelBookedVoyageViewModel(
    private val repository: CancelBookedVoyageRepository
) : ViewModel() {

    private val _nearbyPlaces = MutableStateFlow<NetworkResponse<CancelBookedVoyageResponse>>(NetworkResponse.Loading())
    val nearbyPlaces: StateFlow<NetworkResponse<CancelBookedVoyageResponse>> = _nearbyPlaces.asStateFlow()

    fun fetchNearbyPlaces(profile: CancelBookedVoyages) = viewModelScope.launch {
        _nearbyPlaces.value = NetworkResponse.Loading()
        val result = repository.findboat(profile)
        result.onSuccess { placesResponse ->
            _nearbyPlaces.value = NetworkResponse.Success(placesResponse)
        }.onFailure { exception ->
            Log.e("viewModel", "${exception.localizedMessage}", exception)
            _nearbyPlaces.value = NetworkResponse.Error("${exception.localizedMessage}")
        }
    }

    fun resetNearbyPlaces() {
        _nearbyPlaces.value = NetworkResponse.Loading()
    }
}


