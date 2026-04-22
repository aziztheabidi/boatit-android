package com.boatit.boatsharing.features.voyager.dashboard.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.domain.core.ErrorType
import com.boatit.boatsharing.domain.core.requiresReLogin
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.features.voyager.dashboard.domain.usecase.FetchNearbyPlacesUseCase
import com.boatit.boatsharing.features.voyager.dashboard.domain.usecase.FetchVoyageCategoriesUseCase
import com.boatit.boatsharing.features.voyager.dashboard.model.Place
import com.boatit.boatsharing.features.voyager.dashboard.model.VoyageCategoryDropdownResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NearByVoyagesViewModel(
    private val fetchNearbyPlacesUseCase: FetchNearbyPlacesUseCase,
    private val fetchVoyageCategoriesUseCase: FetchVoyageCategoriesUseCase,
) : com.boatit.boatsharing.core.presentation.LegacyMviViewModel() {
    private val _nearbyPlaces = MutableStateFlow<NetworkResponse<List<Place>>>(NetworkResponse.Loading())
    val nearbyPlaces: StateFlow<NetworkResponse<List<Place>>> = _nearbyPlaces.asStateFlow()

    private val _logoutEvent = MutableStateFlow(false)
    val logoutEvent = _logoutEvent.asStateFlow()

    private val _categories = MutableStateFlow<NetworkResponse<VoyageCategoryDropdownResponse>>(NetworkResponse.Loading())
    val cate: StateFlow<NetworkResponse<VoyageCategoryDropdownResponse>> = _categories.asStateFlow()

    fun fetchNearbyPlaces() =
        viewModelScope.launch {
            _nearbyPlaces.value = NetworkResponse.Loading()
            when (val result = fetchNearbyPlacesUseCase().toResource()) {
                is Resource.Success -> {
                    val places = result.data.obj?.All
                    if (!places.isNullOrEmpty()) {
                        Log.d("viewModel", "First place: ${places[0].Name}")
                        _nearbyPlaces.value = NetworkResponse.Success(places)
                    } else {
                        Log.d("viewModel", "fetchNearbyPlaces: No nearby places found")
                        _nearbyPlaces.value =
                            NetworkResponse.Error(ErrorType.Unknown("No nearby places found"))
                    }
                }

                is Resource.Error -> {
                    val message = result.error.toMessage()
                    Log.d("viewModel", "Error fetching places: $message")
                    if (result.error.requiresReLogin()) {
                        _logoutEvent.value = true
                    }
                    _nearbyPlaces.value = NetworkResponse.Error(result.error)
                }

                Resource.Loading -> {
                    _nearbyPlaces.value = NetworkResponse.Loading()
                }
            }
        }

    fun fetchCategories() =
        viewModelScope.launch {
            _categories.value = NetworkResponse.Loading()
            when (val result = fetchVoyageCategoriesUseCase().toResource()) {
                is Resource.Success -> {
                    val places = result.data
                    if (!places.obj.isNullOrEmpty()) {
                        _categories.value = NetworkResponse.Success(places)
                    } else {
                        Log.d("viewModel", "fetchNearbyPlaces: No nearby places found")
                        _categories.value =
                            NetworkResponse.Error(ErrorType.Unknown("No nearby places found"))
                    }
                }

                is Resource.Error -> {
                    val message = result.error.toMessage()
                    Log.e("viewModel", "Error fetching places: $message")
                    if (result.error.requiresReLogin()) {
                        _logoutEvent.value = true
                    }
                    _categories.value = NetworkResponse.Error(result.error)
                }

                Resource.Loading -> {
                    _categories.value = NetworkResponse.Loading()
                }
            }
        }
}
