package com.boatit.boatsharing.features.voyager.dashboard.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.core.presentation.BaseViewModel
import com.boatit.boatsharing.core.presentation.UiEffect
import com.boatit.boatsharing.core.presentation.UiEvent
import com.boatit.boatsharing.core.presentation.UiState
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.domain.core.ErrorType
import com.boatit.boatsharing.domain.core.requiresReLogin
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.features.voyager.dashboard.domain.usecase.FetchNearbyPlacesUseCase
import com.boatit.boatsharing.features.voyager.dashboard.domain.usecase.FetchVoyageCategoriesUseCase
import com.boatit.boatsharing.features.voyager.dashboard.model.Place
import com.boatit.boatsharing.features.voyager.dashboard.model.VoyageCategoryDropdownResponse
import kotlinx.coroutines.launch

data class NearByVoyagesUiState(
    val nearbyPlaces: NetworkResponse<List<Place>> = NetworkResponse.Loading(),
    val logoutEvent: Boolean = false,
    val categories: NetworkResponse<VoyageCategoryDropdownResponse> = NetworkResponse.Loading(),
) : UiState

sealed interface NearByVoyagesUiEvent : UiEvent {
    data object FetchNearbyPlaces : NearByVoyagesUiEvent

    data object FetchCategories : NearByVoyagesUiEvent
}

sealed interface NearByVoyagesUiEffect : UiEffect {
    data object NoOpEffect : NearByVoyagesUiEffect
}

class NearByVoyagesViewModel(
    private val fetchNearbyPlacesUseCase: FetchNearbyPlacesUseCase,
    private val fetchVoyageCategoriesUseCase: FetchVoyageCategoriesUseCase,
) : BaseViewModel<NearByVoyagesUiState, NearByVoyagesUiEvent, NearByVoyagesUiEffect>(NearByVoyagesUiState()) {
    override fun onEvent(event: NearByVoyagesUiEvent) {
        when (event) {
            NearByVoyagesUiEvent.FetchNearbyPlaces -> fetchNearbyPlaces()
            NearByVoyagesUiEvent.FetchCategories -> fetchCategories()
        }
    }

    fun fetchNearbyPlaces() =
        viewModelScope.launch {
            updateState { copy(nearbyPlaces = NetworkResponse.Loading()) }
            when (val result = fetchNearbyPlacesUseCase().toResource()) {
                is Resource.Success -> {
                    val places = result.data.obj?.All
                    if (!places.isNullOrEmpty()) {
                        Log.d("viewModel", "First place: ${places[0].Name}")
                        updateState { copy(nearbyPlaces = NetworkResponse.Success(places)) }
                    } else {
                        Log.d("viewModel", "fetchNearbyPlaces: No nearby places found")
                        updateState {
                            copy(
                                nearbyPlaces =
                                    NetworkResponse.Error(ErrorType.Unknown("No nearby places found")),
                            )
                        }
                    }
                }

                is Resource.Error -> {
                    val message = result.error.toMessage()
                    Log.d("viewModel", "Error fetching places: $message")
                    if (result.error.requiresReLogin()) {
                        updateState { copy(logoutEvent = true) }
                    }
                    updateState { copy(nearbyPlaces = NetworkResponse.Error(result.error)) }
                }

                Resource.Loading -> {
                    updateState { copy(nearbyPlaces = NetworkResponse.Loading()) }
                }
            }
        }

    fun fetchCategories() =
        viewModelScope.launch {
            updateState { copy(categories = NetworkResponse.Loading()) }
            when (val result = fetchVoyageCategoriesUseCase().toResource()) {
                is Resource.Success -> {
                    val places = result.data
                    if (!places.obj.isNullOrEmpty()) {
                        updateState { copy(categories = NetworkResponse.Success(places)) }
                    } else {
                        Log.d("viewModel", "fetchNearbyPlaces: No nearby places found")
                        updateState {
                            copy(
                                categories =
                                    NetworkResponse.Error(ErrorType.Unknown("No nearby places found")),
                            )
                        }
                    }
                }

                is Resource.Error -> {
                    val message = result.error.toMessage()
                    Log.e("viewModel", "Error fetching places: $message")
                    if (result.error.requiresReLogin()) {
                        updateState { copy(logoutEvent = true) }
                    }
                    updateState { copy(categories = NetworkResponse.Error(result.error)) }
                }

                Resource.Loading -> {
                    updateState { copy(categories = NetworkResponse.Loading()) }
                }
            }
        }
}
