package com.boatit.boatsharing.ui.voyager.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.network.networkreposne.NetworkResponse
import com.boatit.boatsharing.ui.captain.availabilitystatus.model.CaptainAvailabilityRequest
import com.boatit.boatsharing.ui.captain.availabilitystatus.model.CaptainAvailabilityResponse
import com.boatit.boatsharing.ui.captain.availabilitystatus.repository.UpdateStatusRepository
import com.boatit.boatsharing.ui.captain.dashboard.model.AcceptVoyageRequest
import com.boatit.boatsharing.ui.captain.dashboard.model.AcceptVoyageResponse
import com.boatit.boatsharing.ui.captain.dashboard.model.CaptainFeedbackRequest
import com.boatit.boatsharing.ui.captain.dashboard.model.CaptainFeedbackResponse
import com.boatit.boatsharing.ui.captain.dashboard.model.VoyageStartRequest
import com.boatit.boatsharing.ui.captain.dashboard.model.VoyageStartResponse
import com.boatit.boatsharing.ui.captain.dashboard.repository.AcceptRequestRepository
import com.boatit.boatsharing.ui.captain.dashboard.repository.CaptainFeedbackRepository
import com.boatit.boatsharing.ui.captain.dashboard.repository.StartVoyageRepository
import com.boatit.boatsharing.ui.login.model.LoginResponse
import com.boatit.boatsharing.ui.login.model.UserData
import com.boatit.boatsharing.ui.login.repository.LoginRepository
import com.boatit.boatsharing.ui.voyager.dashboard.model.VoyagerFeedbackRequest
import com.boatit.boatsharing.ui.voyager.dashboard.model.VoyagerFeedbackResponse
import com.boatit.boatsharing.ui.voyager.dashboard.model.VoyagerFollowBusinessRequest
import com.boatit.boatsharing.ui.voyager.dashboard.model.VoyagerFollowBusinessResponse
import com.boatit.boatsharing.ui.voyager.dashboard.repository.FollowBusinessRepository
import com.boatit.boatsharing.ui.voyager.dashboard.repository.VoyagerFeedbackRepository
import com.boatit.boatsharing.utils.prefmanager.SharedPrefManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class VoyagerFollowBusinessViewModel(private val repository: FollowBusinessRepository) : ViewModel() {

    private val _loginState = MutableStateFlow<NetworkResponse<VoyagerFollowBusinessResponse>>(NetworkResponse.Loading())
    val loginState: StateFlow<NetworkResponse<VoyagerFollowBusinessResponse>> = _loginState

    fun VoyagerFeedbackFunc(request: VoyagerFollowBusinessRequest) {
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

    fun VoyagerUnFollowFunc(request: VoyagerFollowBusinessRequest) {
        viewModelScope.launch {
            _loginState.value = NetworkResponse.Loading()
            val result = repository.unFollow(request)
            result.onSuccess { response ->
                _loginState.value = NetworkResponse.Success(response)
            }.onFailure { error ->
                _loginState.value = NetworkResponse.Error(error.message ?: "Login failed")
            }
        }
    }
}


