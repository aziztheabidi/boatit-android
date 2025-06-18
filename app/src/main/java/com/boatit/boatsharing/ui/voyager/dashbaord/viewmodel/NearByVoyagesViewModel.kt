package com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.util.Log
import com.boatit.boatsharing.network.networkreposne.NetworkResponse
import com.boatit.boatsharing.ui.voyager.dashbaord.model.Place
import com.boatit.boatsharing.ui.voyager.dashbaord.model.VoyageCategoryDropdownResponse
import com.boatit.boatsharing.ui.voyager.dashbaord.repository.FetchCategoryRepo
import com.boatit.boatsharing.ui.voyager.dashbaord.repository.FetchNearByVoyagesRepo
import com.boatit.boatsharing.utils.AppConstants

class NearByVoyagesViewModel(
    private val repository: FetchNearByVoyagesRepo,
    private val crepository: FetchCategoryRepo
) : ViewModel() {

    private val _nearbyPlaces = MutableStateFlow<NetworkResponse<List<Place>>>(NetworkResponse.Loading())
    val nearbyPlaces: StateFlow<NetworkResponse<List<Place>>> = _nearbyPlaces.asStateFlow()

    private val _categories = MutableStateFlow<NetworkResponse<VoyageCategoryDropdownResponse>>(NetworkResponse.Loading())
    val cate : StateFlow<NetworkResponse<VoyageCategoryDropdownResponse>> = _categories.asStateFlow()

    fun fetchNearbyPlaces() = viewModelScope.launch {
        _nearbyPlaces.value = NetworkResponse.Loading()
        val result = repository.getNearbyPlaces()
        result.onSuccess { placesResponse ->
            val places = placesResponse.obj?.All
            if (places != null) {
                if (places.isNotEmpty()) {
                    Log.d("viewModel", "First place: ${places[0].Name}")
                    _nearbyPlaces.value = NetworkResponse.Success(places)
                    AppConstants.PLACES = places
                    AppConstants.BPLACES = placesResponse.obj.Business!!
                } else {
                    Log.d("viewModel", "fetchNearbyPlaces: No nearby places found")
                    _nearbyPlaces.value = NetworkResponse.Error("No nearby places found")
                }
            }
        }.onFailure { exception ->
            Log.e("viewModel", "Error fetching places: ${exception.localizedMessage}", exception)
            _nearbyPlaces.value = NetworkResponse.Error("An error occurred: ${exception.localizedMessage}")
        }
    }

    fun fetchCategories() = viewModelScope.launch {
        _categories.value = NetworkResponse.Loading()
        val result = crepository.getNearbyPlaces()
        result.onSuccess { placesResponse ->
            val places = placesResponse
            if (places.obj != null) {
                if (places.obj.isNotEmpty()) {
                    _categories.value = NetworkResponse.Success(places)
                    AppConstants.Cates = places.obj
                } else {
                    Log.d("viewModel", "fetchNearbyPlaces: No nearby places found")
                    _categories.value = NetworkResponse.Error("No nearby places found")
                }
            }
        }.onFailure { exception ->
            Log.e("viewModel", "Error fetching places: ${exception.localizedMessage}", exception)
            _categories.value = NetworkResponse.Error("An error occurred: ${exception.localizedMessage}")
        }
    }
}



