package com.boatit.boatsharing.features.userroles.viewmodel

import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.data.local.prefmanager.IRoleProvider
import com.boatit.boatsharing.data.local.prefmanager.ITokenProvider
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.features.userroles.domain.model.RoleAssignmentDomainModel
import com.boatit.boatsharing.features.userroles.domain.usecase.AssignUserRoleUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RoleViewModel(
    private val assignUserRoleUseCase: AssignUserRoleUseCase,
    private val roleProvider: IRoleProvider,
    private val tokenProvider: ITokenProvider,
) : com.boatit.boatsharing.core.presentation.LegacyMviViewModel() {
    private val _roleState = MutableStateFlow<NetworkResponse<RoleAssignmentDomainModel>>(NetworkResponse.Loading())
    val roleState: StateFlow<NetworkResponse<RoleAssignmentDomainModel>> = _roleState

    private val _selectedRole = MutableStateFlow<String?>(null)
    val selectedRole: StateFlow<String?> = _selectedRole

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun selectRole(
        userId: String,
        role: String,
    ) {
        _isLoading.value = true
        _errorMessage.value = null
        _selectedRole.value = role

        viewModelScope.launch {
            when (val result = assignUserRoleUseCase(userId, role, tokenProvider.getAccessToken()).toResource()) {
                is Resource.Success -> {
                    val response = result.data
                    _roleState.value = NetworkResponse.Success(response)
                    roleProvider.saveRole(role)
                    tokenProvider.saveTokens(response.accessToken, response.refreshToken)
                    _isLoading.value = false
                }

                is Resource.Error -> {
                    val message = result.error.toMessage()
                    _roleState.value = NetworkResponse.Error(result.error)
                    _isLoading.value = false
                    _errorMessage.value = message
                }

                Resource.Loading -> {
                    _roleState.value = NetworkResponse.Loading()
                    _isLoading.value = true
                }
            }
        }
    }

    fun resetRoleState() {
        _roleState.value = NetworkResponse.Loading()
    }

    @Deprecated("Use resetRoleState")
    fun resetNearbyPlaces() {
        resetRoleState()
    }
}
