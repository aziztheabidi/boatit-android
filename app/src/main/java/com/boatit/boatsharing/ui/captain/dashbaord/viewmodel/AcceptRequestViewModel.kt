package com.boatit.boatsharing.ui.captain.dashbaord.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.network.networkreposne.NetworkResponse
import com.boatit.boatsharing.ui.captain.availablitystatus.model.CaptainAvailabilityRequest
import com.boatit.boatsharing.ui.captain.availablitystatus.model.CaptainAvailabilityResponse
import com.boatit.boatsharing.ui.captain.availablitystatus.repository.UpdateStatusRepository
import com.boatit.boatsharing.ui.captain.dashbaord.model.AcceptVoyageRequest
import com.boatit.boatsharing.ui.captain.dashbaord.model.AcceptVoyageResponse
import com.boatit.boatsharing.ui.captain.dashbaord.repository.AcceptRequestRepository
import com.boatit.boatsharing.ui.login.model.LoginResponse
import com.boatit.boatsharing.ui.login.model.UserData
import com.boatit.boatsharing.ui.login.repository.LoginRepository
import com.boatit.boatsharing.utils.prefmanager.SharedPrefManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class AcceptRequestViewModel(private val repository: AcceptRequestRepository) : ViewModel() {

    private val _loginState = MutableStateFlow<NetworkResponse<AcceptVoyageResponse>>(NetworkResponse.Loading())
    val loginState: StateFlow<NetworkResponse<AcceptVoyageResponse>> = _loginState

    fun accept(request: AcceptVoyageRequest) {
        viewModelScope.launch {
            _loginState.value = NetworkResponse.Loading()
            val result = repository.status(request)
            Log.e("Accept_request",result.toString())
            result.onSuccess { response ->
                _loginState.value = NetworkResponse.Success(response)
            }.onFailure { error ->
                _loginState.value = NetworkResponse.Error(error.message!!)
            }


        }
    }

    fun decline(request: AcceptVoyageRequest) {
        viewModelScope.launch {
            _loginState.value = NetworkResponse.Loading()
            val result = repository.decline(request)
            result.onSuccess { response ->
                _loginState.value = NetworkResponse.Success(response)
            }.onFailure { error ->
                _loginState.value = NetworkResponse.Error(error.message!!)
            }


        }
    }

    fun resetNearbyPlaces() {
        _loginState.value = NetworkResponse.Loading()
    }
}


