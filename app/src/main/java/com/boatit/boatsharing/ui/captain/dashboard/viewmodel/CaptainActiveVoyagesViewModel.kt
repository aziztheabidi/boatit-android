package com.boatit.boatsharing.ui.captain.dashboard.viewmodel

import CaptainActiveVoyagesResponse
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.ui.captain.dashboard.repository.CaptainActiveVoyagesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class CaptainActiveVoyagesViewModel(private val repository: CaptainActiveVoyagesRepository) : ViewModel() {

    private val _loginState = MutableStateFlow<NetworkResponse<CaptainActiveVoyagesResponse>>(NetworkResponse.Loading())
    val loginState: StateFlow<NetworkResponse<CaptainActiveVoyagesResponse>> = _loginState

    fun voyages() {
        viewModelScope.launch {
            _loginState.value = NetworkResponse.Loading()
            val result = repository.voyages()
            result.onSuccess { response ->
                Log.e("captain_voyages",response.toString())

                _loginState.value = NetworkResponse.Success(response)
            }.onFailure { error ->

                Log.e("captain_voyages",error.toString())
                _loginState.value = NetworkResponse.Error(error.message ?: "Login failed")
            }
        }
    }

    fun resetNearbyPlaces() {
        _loginState.value = NetworkResponse.Loading()
    }
}


