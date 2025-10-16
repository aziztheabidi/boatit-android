package com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.util.Log
import com.boatit.boatsharing.network.networkreposne.NetworkResponse
import com.boatit.boatsharing.ui.voyager.dashbaord.model.BookVoyageRequest
import com.boatit.boatsharing.ui.voyager.dashbaord.model.BookVoyageResponse
import com.boatit.boatsharing.ui.voyager.dashbaord.model.FindBoatRequest
import com.boatit.boatsharing.ui.voyager.dashbaord.model.FindBoatResponse
import com.boatit.boatsharing.ui.voyager.dashbaord.model.Place
import com.boatit.boatsharing.ui.voyager.dashbaord.repository.BookVoyageRepo
import com.boatit.boatsharing.ui.voyager.dashbaord.repository.FetchNearByVoyagesRepo
import com.boatit.boatsharing.ui.voyager.dashbaord.repository.FindBoatRepo
import com.boatit.boatsharing.utils.AppConstants
import com.google.gson.Gson

class BookVoyageViewModel(
    private val repository: BookVoyageRepo
) : ViewModel() {

    private val _nearbyPlaces = MutableStateFlow<NetworkResponse<BookVoyageResponse>>(NetworkResponse.Loading())
    val nearbyPlaces: StateFlow<NetworkResponse<BookVoyageResponse>> = _nearbyPlaces.asStateFlow()

    fun bookVoyageVMfunc(profile: BookVoyageRequest) = viewModelScope.launch {
        val json = Gson().toJson(profile)
        _nearbyPlaces.value = NetworkResponse.Loading()
        val result = repository.BookVoyageFunc(profile)
        result.onSuccess { placesResponse ->
            _nearbyPlaces.value = NetworkResponse.Success(placesResponse)
        }.onFailure { exception ->
            Log.e("viewModel", "Error fetching places: ${exception.localizedMessage}", exception)
            _nearbyPlaces.value = NetworkResponse.Error("${exception.localizedMessage}")
        }
    }

    fun resetNearbyPlaces() {
        _nearbyPlaces.value = NetworkResponse.Loading()
    }
}


