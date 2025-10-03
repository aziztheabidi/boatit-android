package com.boatit.boatsharing.ui.userroles.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.ui.login.model.LoginResponse
import com.boatit.boatsharing.ui.login.repository.LoginRepository
import com.boatit.boatsharing.ui.userroles.model.RoleResponse
import com.boatit.boatsharing.ui.userroles.repository.RoleRepository
import com.boatit.boatsharing.utils.prefmanager.RoleProvider
import com.boatit.boatsharing.utils.prefmanager.TokenProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class RoleViewModel(
    private val repository: RoleRepository,
    private val roleProvider: RoleProvider,
    private val tokenProvider: TokenProvider
) : ViewModel() {

    private val _roleState = MutableStateFlow<NetworkResponse<RoleResponse>>(NetworkResponse.Loading())
    val roleState: StateFlow<NetworkResponse<RoleResponse>> = _roleState

    private val _selectedRole = MutableStateFlow<String?>(null)
    val selectedRole: StateFlow<String?> = _selectedRole

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun selectRole(userId: String, role: String) {
        _isLoading.value = true
        _errorMessage.value = null
        _selectedRole.value = role

        viewModelScope.launch {
            val result = repository.login(userId, role)
            result.onSuccess { response ->
                _roleState.value = NetworkResponse.Success(response)
                roleProvider.saveRole(role)
                tokenProvider.saveTokens(response.obj?.Accesstoken, response.obj?.Refreshtoken)
                _isLoading.value = false
            }.onFailure { error ->
                _roleState.value = NetworkResponse.Error(error.message ?: "Failed to assign role")
                _isLoading.value = false
                _errorMessage.value = error.message
            }
        }
    }

    fun resetNearbyPlaces() {
        _roleState.value = NetworkResponse.Loading()
    }
}

