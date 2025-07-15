package com.boatit.boatsharing.ui.business.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.network.networkreposne.NetworkResponse
import com.boatit.boatsharing.ui.business.model.DocksDropdownResponse
import com.boatit.boatsharing.ui.business.model.GetBusinessResponse
import com.boatit.boatsharing.ui.business.repository.GetBusinessDocksRepo
import com.boatit.boatsharing.ui.business.repository.GetBusinessRepo
import com.boatit.boatsharing.ui.captain.availablitystatus.model.CaptainAvailabilityRequest
import com.boatit.boatsharing.ui.captain.availablitystatus.model.CaptainAvailabilityResponse
import com.boatit.boatsharing.ui.captain.availablitystatus.repository.UpdateStatusRepository
import com.boatit.boatsharing.ui.captain.dashbaord.model.AcceptVoyageRequest
import com.boatit.boatsharing.ui.captain.dashbaord.model.AcceptVoyageResponse
import com.boatit.boatsharing.ui.captain.dashbaord.model.VoyageStartRequest
import com.boatit.boatsharing.ui.captain.dashbaord.model.VoyageStartResponse
import com.boatit.boatsharing.ui.captain.dashbaord.repository.AcceptRequestRepository
import com.boatit.boatsharing.ui.captain.dashbaord.repository.StartVoyageRepository
import com.boatit.boatsharing.ui.captain.voyages.model.CaptainVoyages
import com.boatit.boatsharing.ui.captain.voyages.model.CaptainVoyagesResponse
import com.boatit.boatsharing.ui.captain.voyages.repository.CaptainVoyagesRepository
import com.boatit.boatsharing.ui.login.model.LoginResponse
import com.boatit.boatsharing.ui.login.model.UserData
import com.boatit.boatsharing.ui.login.repository.LoginRepository
import com.boatit.boatsharing.ui.voyager.dashbaord.model.ActiveVoyageResponse
import com.boatit.boatsharing.ui.voyager.dashbaord.model.VoyagerVoyagesResponse
import com.boatit.boatsharing.ui.voyager.dashbaord.repository.GetActiveVoyageRepository
import com.boatit.boatsharing.ui.voyager.dashbaord.repository.VoyagerVoyagesRepository
import com.boatit.boatsharing.utils.prefmanager.SharedPrefManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class GetBusinessViewModel(private val repository: GetBusinessRepo, private val drepository: GetBusinessDocksRepo) : ViewModel() {

    private val _loginState = MutableStateFlow<NetworkResponse<GetBusinessResponse>>(NetworkResponse.Loading())
    val loginState: StateFlow<NetworkResponse<GetBusinessResponse>> = _loginState

    private val _docksState = MutableStateFlow<NetworkResponse<DocksDropdownResponse>>(NetworkResponse.Loading())
    val docksState: StateFlow<NetworkResponse<DocksDropdownResponse>> = _docksState

    private val _logoutEvent = MutableStateFlow(false)
    val logoutEvent = _logoutEvent.asStateFlow()

    fun voyages() {
        viewModelScope.launch {
            _loginState.value = NetworkResponse.Loading()
            val result = repository.voyages()
            result.onSuccess { response ->
                _loginState.value = NetworkResponse.Success(response)
            }.onFailure { exception ->
                if (exception.message?.contains("401")!!) {
                    _logoutEvent.value = true
                }
                _loginState.value = NetworkResponse.Error(exception.message ?: "Login failed")
            }
        }
    }

    fun docks() {
        viewModelScope.launch {
            _docksState.value = NetworkResponse.Loading()
            val result = drepository.voyages()
            result.onSuccess { response ->
                _docksState.value = NetworkResponse.Success(response)
            }.onFailure { error ->
                _docksState.value = NetworkResponse.Error(error.message ?: "Login failed")
            }
        }
    }

    fun resetNearbyPlaces() {
        _loginState.value = NetworkResponse.Loading()
    }

    fun resetDocks() {
        _docksState.value = NetworkResponse.Loading()
    }
}



