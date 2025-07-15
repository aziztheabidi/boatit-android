package com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.util.Log
import com.boatit.boatsharing.network.networkreposne.NetworkResponse
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


