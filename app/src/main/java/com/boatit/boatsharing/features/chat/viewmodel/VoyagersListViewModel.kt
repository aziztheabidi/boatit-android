package com.boatit.boatsharing.features.chat.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.features.chat.domain.usecase.FetchVoyagersUseCase
import com.boatit.boatsharing.features.chat.model.ActiveVoyagersResponse
import com.boatit.boatsharing.features.chat.model.VoyagerInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class VoyagersListViewModel(
    private val fetchVoyagersUseCase: FetchVoyagersUseCase,
) : com.boatit.boatsharing.core.presentation.LegacyMviViewModel() {
    private val _loginState = MutableStateFlow<NetworkResponse<ActiveVoyagersResponse>>(NetworkResponse.Loading())
    val loginState: StateFlow<NetworkResponse<ActiveVoyagersResponse>> = _loginState
    var followed = mutableStateOf(emptyList<VoyagerInfo>())
    var allusers = mutableStateOf(emptyList<VoyagerInfo>())
    var searchQuery by mutableStateOf("")

    val filteredBoatList: List<VoyagerInfo>
        get() =
            if (searchQuery.isBlank()) {
                allusers.value
            } else {
                allusers.value.filter {
                    it.FirstName.contains(searchQuery, ignoreCase = true) ||
                        it.LastName.contains(searchQuery, ignoreCase = true)
                }
            }

    val filteredBoatListFollowed: List<VoyagerInfo>
        get() =
            if (searchQuery.isBlank()) {
                followed.value
            } else {
                followed.value.filter {
                    it.FirstName.contains(searchQuery, ignoreCase = true)
                }
            }

    fun updateSearchQuery(query: String) {
        searchQuery = query
    }

    fun onBoatList(value: ActiveVoyagersResponse) {
        followed.value = value.obj.Followed
        allusers.value = value.obj.UnFollowed
    }

    fun voyages() {
        viewModelScope.launch {
            _loginState.value = NetworkResponse.Loading()
            when (val result = fetchVoyagersUseCase().toResource()) {
                is Resource.Success -> {
                    _loginState.value = NetworkResponse.Success(result.data)
                }

                is Resource.Error -> {
                    _loginState.value = NetworkResponse.Error(result.error)
                }

                Resource.Loading -> {
                    _loginState.value = NetworkResponse.Loading()
                }
            }
        }
    }

    fun resetNearbyPlaces() {
        _loginState.value = NetworkResponse.Loading()
    }
}
