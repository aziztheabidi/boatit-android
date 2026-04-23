package com.boatit.boatsharing.features.chat.viewmodel

import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.core.presentation.BaseViewModel
import com.boatit.boatsharing.core.presentation.UiEffect
import com.boatit.boatsharing.core.presentation.UiEvent
import com.boatit.boatsharing.core.presentation.UiState
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.features.chat.domain.usecase.FetchVoyagersUseCase
import com.boatit.boatsharing.features.chat.model.ActiveVoyagersResponse
import com.boatit.boatsharing.features.chat.model.VoyagerInfo
import kotlinx.coroutines.launch

data class VoyagersListUiState(
    val loginState: NetworkResponse<ActiveVoyagersResponse> = NetworkResponse.Loading(),
    val followed: List<VoyagerInfo> = emptyList(),
    val allusers: List<VoyagerInfo> = emptyList(),
    val searchQuery: String = "",
) : UiState {
    val filteredBoatList: List<VoyagerInfo>
        get() =
            if (searchQuery.isBlank()) {
                allusers
            } else {
                allusers.filter {
                    it.FirstName.contains(searchQuery, ignoreCase = true) ||
                        it.LastName.contains(searchQuery, ignoreCase = true)
                }
            }

    val filteredBoatListFollowed: List<VoyagerInfo>
        get() =
            if (searchQuery.isBlank()) {
                followed
            } else {
                followed.filter {
                    it.FirstName.contains(searchQuery, ignoreCase = true)
                }
            }
}

sealed interface VoyagersListUiEvent : UiEvent {
    data object LoadVoyagers : VoyagersListUiEvent

    data object Reset : VoyagersListUiEvent

    data class SearchQueryChanged(val query: String) : VoyagersListUiEvent

    data class BoatListLoaded(val value: ActiveVoyagersResponse) : VoyagersListUiEvent
}

sealed interface VoyagersListUiEffect : UiEffect {
    data object NoOpEffect : VoyagersListUiEffect
}

class VoyagersListViewModel(
    private val fetchVoyagersUseCase: FetchVoyagersUseCase,
) : BaseViewModel<VoyagersListUiState, VoyagersListUiEvent, VoyagersListUiEffect>(VoyagersListUiState()) {
    override fun onEvent(event: VoyagersListUiEvent) {
        when (event) {
            VoyagersListUiEvent.LoadVoyagers -> voyages()
            VoyagersListUiEvent.Reset -> resetNearbyPlaces()
            is VoyagersListUiEvent.SearchQueryChanged -> {
                updateState { copy(searchQuery = event.query) }
            }
            is VoyagersListUiEvent.BoatListLoaded -> onBoatList(event.value)
        }
    }

    fun updateSearchQuery(query: String) {
        onEvent(VoyagersListUiEvent.SearchQueryChanged(query))
    }

    fun onBoatList(value: ActiveVoyagersResponse) {
        updateState {
            copy(
                followed = value.obj.Followed,
                allusers = value.obj.UnFollowed,
            )
        }
    }

    fun voyages() {
        viewModelScope.launch {
            updateState { copy(loginState = NetworkResponse.Loading()) }
            when (val result = fetchVoyagersUseCase().toResource()) {
                is Resource.Success -> {
                    updateState { copy(loginState = NetworkResponse.Success(result.data)) }
                }

                is Resource.Error -> {
                    updateState { copy(loginState = NetworkResponse.Error(result.error)) }
                }

                Resource.Loading -> {
                    updateState { copy(loginState = NetworkResponse.Loading()) }
                }
            }
        }
    }

    fun resetNearbyPlaces() {
        updateState { copy(loginState = NetworkResponse.Loading()) }
    }
}
