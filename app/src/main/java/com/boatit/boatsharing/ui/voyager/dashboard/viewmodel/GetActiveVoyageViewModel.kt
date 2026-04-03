package com.boatit.boatsharing.ui.voyager.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.ui.voyager.dashboard.model.ActiveVoyageResponse
import com.boatit.boatsharing.ui.voyager.dashboard.repository.GetActiveVoyageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GetActiveVoyageViewModel(
    private val repository: GetActiveVoyageRepository,
) : ViewModel() {

    private val _loginState =
        MutableStateFlow<NetworkResponse<ActiveVoyageResponse>>(NetworkResponse.Loading())
    val loginState: StateFlow<NetworkResponse<ActiveVoyageResponse>> = _loginState

    fun voyages() {
        viewModelScope.launch {
            _loginState.value = NetworkResponse.Loading()
            val result = repository.voyages()
            result.onSuccess { response ->
                _loginState.value = NetworkResponse.Success(response)
            }.onFailure { error ->
                _loginState.value = NetworkResponse.Error(error.message ?: "Login failed")
            }
        }
    }

    fun resetNearbyPlaces() {
        _loginState.value = NetworkResponse.Loading()
    }
}
