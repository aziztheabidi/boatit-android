package com.boatit.boatsharing.features.userroles.viewmodel

import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.core.presentation.BaseViewModel
import com.boatit.boatsharing.core.presentation.UiEffect
import com.boatit.boatsharing.core.presentation.UiEvent
import com.boatit.boatsharing.core.presentation.UiState
import com.boatit.boatsharing.data.local.prefmanager.IRoleProvider
import com.boatit.boatsharing.data.local.prefmanager.ITokenProvider
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.features.userroles.domain.model.RoleAssignmentDomainModel
import com.boatit.boatsharing.features.userroles.domain.usecase.AssignUserRoleUseCase
import kotlinx.coroutines.launch

data class RoleUiState(
    val roleState: NetworkResponse<RoleAssignmentDomainModel> = NetworkResponse.Loading(),
    val selectedRole: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
) : UiState

sealed interface RoleUiEvent : UiEvent {
    data class SelectRole(
        val userId: String,
        val role: String,
    ) : RoleUiEvent

    data object ResetRoleState : RoleUiEvent
}

sealed interface RoleUiEffect : UiEffect {
    data object NoOpEffect : RoleUiEffect
}

class RoleViewModel(
    private val assignUserRoleUseCase: AssignUserRoleUseCase,
    private val roleProvider: IRoleProvider,
    private val tokenProvider: ITokenProvider,
) : BaseViewModel<RoleUiState, RoleUiEvent, RoleUiEffect>(RoleUiState()) {
    override fun onEvent(event: RoleUiEvent) {
        when (event) {
            is RoleUiEvent.SelectRole -> performSelectRole(event.userId, event.role)
            RoleUiEvent.ResetRoleState -> resetRoleState()
        }
    }

    fun selectRole(
        userId: String,
        role: String,
    ) {
        onEvent(RoleUiEvent.SelectRole(userId, role))
    }

    private fun performSelectRole(
        userId: String,
        role: String,
    ) {
        updateState {
            copy(
                isLoading = true,
                errorMessage = null,
                selectedRole = role,
            )
        }

        viewModelScope.launch {
            when (val result = assignUserRoleUseCase(userId, role, tokenProvider.getAccessToken()).toResource()) {
                is Resource.Success -> {
                    val response = result.data
                    updateState {
                        copy(
                            roleState = NetworkResponse.Success(response),
                            isLoading = false,
                        )
                    }
                    roleProvider.saveRole(role)
                    tokenProvider.saveTokens(response.accessToken, response.refreshToken)
                }

                is Resource.Error -> {
                    val message = result.error.toMessage()
                    updateState {
                        copy(
                            roleState = NetworkResponse.Error(result.error),
                            isLoading = false,
                            errorMessage = message,
                        )
                    }
                }

                Resource.Loading -> {
                    updateState {
                        copy(
                            roleState = NetworkResponse.Loading(),
                            isLoading = true,
                        )
                    }
                }
            }
        }
    }

    private fun resetRoleState() {
        updateState { copy(roleState = NetworkResponse.Loading()) }
    }
}
