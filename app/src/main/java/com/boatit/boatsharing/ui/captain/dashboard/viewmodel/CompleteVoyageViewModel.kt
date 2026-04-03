package com.boatit.boatsharing.ui.captain.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.ui.captain.availabilitystatus.model.CaptainAvailabilityRequest
import com.boatit.boatsharing.ui.captain.availabilitystatus.model.CaptainAvailabilityResponse
import com.boatit.boatsharing.ui.captain.availabilitystatus.repository.UpdateStatusRepository
import com.boatit.boatsharing.ui.captain.dashboard.model.AcceptVoyageRequest
import com.boatit.boatsharing.ui.captain.dashboard.model.AcceptVoyageResponse
import com.boatit.boatsharing.ui.captain.dashboard.model.VoyageCompleteRequest
import com.boatit.boatsharing.ui.captain.dashboard.model.VoyageCompleteResponse
import com.boatit.boatsharing.ui.captain.dashboard.model.VoyageStartRequest
import com.boatit.boatsharing.ui.captain.dashboard.model.VoyageStartResponse
import com.boatit.boatsharing.ui.captain.dashboard.repository.AcceptRequestRepository
import com.boatit.boatsharing.ui.captain.dashboard.repository.CompleteVoyageRepository
import com.boatit.boatsharing.ui.captain.dashboard.repository.StartVoyageRepository
import com.boatit.boatsharing.ui.login.model.LoginResponse
import com.boatit.boatsharing.ui.login.model.UserData
import com.boatit.boatsharing.ui.login.repository.LoginRepository
import com.boatit.boatsharing.utils.prefmanager.SharedPrefManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class CompleteVoyageViewModel(private val repository: CompleteVoyageRepository) : ViewModel() {

    private val _loginState = MutableStateFlow<NetworkResponse<VoyageCompleteResponse>>(NetworkResponse.Loading())
    val loginState: StateFlow<NetworkResponse<VoyageCompleteResponse>> = _loginState

    fun startvoyage(request: VoyageCompleteRequest) {
        viewModelScope.launch {
            _loginState.value = NetworkResponse.Loading()
            val result = repository.status(request)
            result.onSuccess { response ->
                _loginState.value = NetworkResponse.Success(response)
            }.onFailure { error ->
                _loginState.value = NetworkResponse.Error(error.message ?: "Login failed")
            }
        }
    }
}


