package com.boatit.boatsharing.ui.chat.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.ui.chat.model.ActiveVoyagersResponse
import com.boatit.boatsharing.ui.chat.model.VoyagerInfo
import com.boatit.boatsharing.ui.chat.repository.VoyagersRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class VoyagersListViewModel(private val repository: VoyagersRepository) : ViewModel() {

    private val _loginState =
        MutableStateFlow<NetworkResponse<ActiveVoyagersResponse>>(NetworkResponse.Loading())
    val loginState: StateFlow<NetworkResponse<ActiveVoyagersResponse>> = _loginState
    var followed = mutableStateOf(emptyList<VoyagerInfo>())
    var allusers = mutableStateOf(emptyList<VoyagerInfo>())
    var searchQuery by mutableStateOf("")

    val filteredBoatList: List<VoyagerInfo>
        get() = if (searchQuery.isBlank()) {
            allusers.value
        } else {
            allusers.value.filter {
                it.FirstName.contains(searchQuery, ignoreCase = true) ||
                        it.LastName.contains(searchQuery, ignoreCase = true)
            }
        }

    val filteredBoatListFollowed: List<VoyagerInfo>
        get() = if (searchQuery.isBlank()) {
            followed.value
        } else {
            followed.value.filter {
                it.FirstName.contains(searchQuery, ignoreCase = true)
            }
        }

    fun updateSearchQuery(query: String)
       { searchQuery = query }

    fun onBoatList(value: ActiveVoyagersResponse) { followed.value = value.obj.Followed
        allusers.value = value.obj.UnFollowed
    }

    fun voyages() {
        viewModelScope.launch {
            _loginState.value = NetworkResponse.Loading()
            val result = repository.voyages()
            result.onSuccess { response ->
                _loginState.value = NetworkResponse.Success(response)
            }.onFailure { error ->
                _loginState.value = NetworkResponse.Error(error.message ?: "Failed to load voyagers")
            }
        }
    }

    fun resetNearbyPlaces() {
        _loginState.value = NetworkResponse.Loading()
    }
}
