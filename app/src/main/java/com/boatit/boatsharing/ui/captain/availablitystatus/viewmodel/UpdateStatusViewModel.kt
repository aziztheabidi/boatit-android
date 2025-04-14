package com.boatit.boatsharing.ui.captain.availablitystatus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.network.networkreposne.NetworkResponse
import com.boatit.boatsharing.ui.captain.availablitystatus.model.CaptainAvailabilityRequest
import com.boatit.boatsharing.ui.captain.availablitystatus.model.CaptainAvailabilityResponse
import com.boatit.boatsharing.ui.captain.availablitystatus.repository.UpdateStatusRepository
import com.boatit.boatsharing.ui.login.model.LoginResponse
import com.boatit.boatsharing.ui.login.model.UserData
import com.boatit.boatsharing.ui.login.repository.LoginRepository
import com.boatit.boatsharing.utils.prefmanager.RoleProvider
import com.boatit.boatsharing.utils.prefmanager.SharedPrefManager
import com.boatit.boatsharing.utils.prefmanager.StatusProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class UpdateStatusViewModel(private val repository: UpdateStatusRepository,private val statusProvider: StatusProvider) : ViewModel() {

    private val _loginState = MutableStateFlow<NetworkResponse<CaptainAvailabilityResponse>>(NetworkResponse.Loading())
    val loginState: StateFlow<NetworkResponse<CaptainAvailabilityResponse>> = _loginState

    fun status(userId: String, isAvailable: Boolean) {
        viewModelScope.launch {
            _loginState.value = NetworkResponse.Loading()
            val result = repository.status(CaptainAvailabilityRequest(userId, isAvailable))
            result.onSuccess { response ->
                _loginState.value = NetworkResponse.Success(response)
                statusProvider.setCaptainStatus(isAvailable)
            }.onFailure { error ->
                _loginState.value = NetworkResponse.Error(error.message ?: "Login failed")
            }
        }
    }

    fun getCaptainStatus(): Boolean {
        return statusProvider.isCaptainOnline()
    }
}


