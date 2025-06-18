package com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.network.networkreposne.NetworkResponse
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
import com.boatit.boatsharing.ui.voyager.dashbaord.model.BusinessRelationshipResponse
import com.boatit.boatsharing.ui.voyager.dashbaord.model.SponsorPayments
import com.boatit.boatsharing.ui.voyager.dashbaord.model.VoyagerVoyagesResponse
import com.boatit.boatsharing.ui.voyager.dashbaord.repository.FetchBusinessRepo
import com.boatit.boatsharing.ui.voyager.dashbaord.repository.FutureVoyagesRepo
import com.boatit.boatsharing.ui.voyager.dashbaord.repository.SponcerVoyagesRepo
import com.boatit.boatsharing.ui.voyager.dashbaord.repository.VoyagerVoyagesRepository
import com.boatit.boatsharing.utils.prefmanager.SharedPrefManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class FetchBusinessViewModel(private val repository: FetchBusinessRepo) : ViewModel() {

    private val _loginState = MutableStateFlow<NetworkResponse<BusinessRelationshipResponse>>(NetworkResponse.Loading())
    val loginState: StateFlow<NetworkResponse<BusinessRelationshipResponse>> = _loginState

    var selectedIndex = 0

    fun voyages() {
        viewModelScope.launch {
            _loginState.value = NetworkResponse.Loading()
            val result = repository.getNearbyPlaces()
            result.onSuccess { response ->
                _loginState.value = NetworkResponse.Success(response)
            }.onFailure { error ->
                _loginState.value = NetworkResponse.Error(error.message ?: "Login failed")
            }
        }
    }
}


