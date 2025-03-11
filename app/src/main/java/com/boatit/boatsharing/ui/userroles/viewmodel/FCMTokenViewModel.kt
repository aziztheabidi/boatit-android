package com.boatit.boatsharing.ui.userroles.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.network.networkreposne.NetworkResponse
import com.boatit.boatsharing.ui.login.model.LoginResponse
import com.boatit.boatsharing.ui.login.repository.LoginRepository
import com.boatit.boatsharing.ui.userroles.model.RoleResponse
import com.boatit.boatsharing.ui.userroles.model.UpdateDeviceTokenResponse
import com.boatit.boatsharing.ui.userroles.repository.FCMTokenRepository
import com.boatit.boatsharing.ui.userroles.repository.RoleRepository
import com.boatit.boatsharing.utils.prefmanager.RoleProvider
import com.boatit.boatsharing.utils.prefmanager.TokenProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class FCMTokenViewModel(private val repository: FCMTokenRepository) : ViewModel() {

    private val _loginState = MutableStateFlow<NetworkResponse<UpdateDeviceTokenResponse>>(NetworkResponse.Loading())
    val loginState: StateFlow<NetworkResponse<UpdateDeviceTokenResponse>> = _loginState
    fun fcm(userid: String, token: String) {
        viewModelScope.launch {
            _loginState.value = NetworkResponse.Loading()
            val result = repository.login(userid, token)
            result.onSuccess { response ->
                _loginState.value = NetworkResponse.Success(response)
            }.onFailure { error ->
                _loginState.value = NetworkResponse.Error(error.message ?: "Login failed")
            }
        }
    }
}
