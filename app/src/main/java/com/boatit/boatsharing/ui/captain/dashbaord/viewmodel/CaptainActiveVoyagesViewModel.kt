package com.boatit.boatsharing.ui.captain.dashbaord.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.network.networkreposne.NetworkResponse
import com.boatit.boatsharing.ui.captain.availablitystatus.model.CaptainAvailabilityRequest
import com.boatit.boatsharing.ui.captain.availablitystatus.model.CaptainAvailabilityResponse
import com.boatit.boatsharing.ui.captain.availablitystatus.repository.UpdateStatusRepository
import com.boatit.boatsharing.ui.captain.dashbaord.model.AcceptVoyageRequest
import com.boatit.boatsharing.ui.captain.dashbaord.model.AcceptVoyageResponse
import com.boatit.boatsharing.ui.captain.dashbaord.model.CaptainActiveVoyagesResponse
import com.boatit.boatsharing.ui.captain.dashbaord.model.VoyageStartRequest
import com.boatit.boatsharing.ui.captain.dashbaord.model.VoyageStartResponse
import com.boatit.boatsharing.ui.captain.dashbaord.repository.AcceptRequestRepository
import com.boatit.boatsharing.ui.captain.dashbaord.repository.CaptainActiveVoyagesRepository
import com.boatit.boatsharing.ui.captain.dashbaord.repository.StartVoyageRepository
import com.boatit.boatsharing.ui.captain.voyages.model.CaptainVoyages
import com.boatit.boatsharing.ui.captain.voyages.model.CaptainVoyagesResponse
import com.boatit.boatsharing.ui.captain.voyages.repository.CaptainVoyagesRepository
import com.boatit.boatsharing.ui.login.model.LoginResponse
import com.boatit.boatsharing.ui.login.model.UserData
import com.boatit.boatsharing.ui.login.repository.LoginRepository
import com.boatit.boatsharing.utils.prefmanager.SharedPrefManager
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
                _loginState.value = NetworkResponse.Success(response)
            }.onFailure { error ->
                _loginState.value = NetworkResponse.Error(error.message ?: "Login failed")
            }
        }
    }
}


