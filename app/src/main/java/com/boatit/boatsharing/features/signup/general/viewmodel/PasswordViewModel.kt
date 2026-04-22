package com.boatit.boatsharing.features.signup.general.viewmodel

import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.core.presentation.BaseViewModel
import com.boatit.boatsharing.core.presentation.UiEffect
import com.boatit.boatsharing.core.presentation.UiEvent
import com.boatit.boatsharing.core.presentation.UiState
import com.boatit.boatsharing.features.login.domain.model.LoginDomainModel
import com.boatit.boatsharing.features.login.domain.model.toUserData
import com.boatit.boatsharing.features.login.model.UserData
import com.boatit.boatsharing.features.signup.general.domain.usecase.RegisterPasswordUseCase
import com.boatit.boatsharing.data.local.prefmanager.SharedPrefManager
import kotlinx.coroutines.launch

data class PasswordUiState(
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
) : UiState {
    val isFormValid: Boolean
        get() = password.isNotEmpty()
}

sealed interface PasswordUiEvent : UiEvent {
    data class PasswordChanged(val value: String) : PasswordUiEvent

    data class Submit(val token: String) : PasswordUiEvent

    data object ClearError : PasswordUiEvent
}

sealed interface PasswordUiEffect : UiEffect {
    data class ShowToast(val message: String) : PasswordUiEffect

    data object NavigateToSelectRole : PasswordUiEffect
}

class PasswordViewModel(
    private val registerPasswordUseCase: RegisterPasswordUseCase,
    private val sharedPrefManager: SharedPrefManager,
) : BaseViewModel<PasswordUiState, PasswordUiEvent, PasswordUiEffect>(PasswordUiState()) {
    override fun onEvent(event: PasswordUiEvent) {
        when (event) {
            is PasswordUiEvent.PasswordChanged -> updateState { copy(password = event.value, errorMessage = null) }
            is PasswordUiEvent.Submit -> passwordRegInternal(currentState.password, event.token)
            PasswordUiEvent.ClearError -> updateState { copy(errorMessage = null) }
        }
    }

    fun onPasswordChange(newPassword: String) {
        onEvent(PasswordUiEvent.PasswordChanged(newPassword))
    }

    fun passwordReg(
        password: String,
        token: String,
    ) {
        if (password != currentState.password) {
            onPasswordChange(password)
        }
        onEvent(PasswordUiEvent.Submit(token))
    }

    private fun passwordRegInternal(
        password: String,
        token: String,
    ) {
        if (!currentState.isFormValid || currentState.isLoading) return

        viewModelScope.launch {
            updateState { copy(isLoading = true, errorMessage = null) }

            when (val result = registerPasswordUseCase(password, token).toResource()) {
                is Resource.Success -> handleSuccess(result.data)
                is Resource.Error -> {
                    val message = result.error.toMessage()
                    updateState { copy(isLoading = false, errorMessage = message) }
                    emitEffect(PasswordUiEffect.ShowToast(message))
                }
                Resource.Loading -> updateState { copy(isLoading = true) }
            }
        }
    }

    private fun handleSuccess(response: LoginDomainModel) {
        response.user?.let { saveLoginData(it.toUserData()) }
        updateState { copy(isLoading = false, errorMessage = null) }
        emitEffect(PasswordUiEffect.ShowToast(response.message.ifBlank { "Registration successful" }))
        emitEffect(PasswordUiEffect.NavigateToSelectRole)
    }

    private fun saveLoginData(userData: UserData) {
        sharedPrefManager.saveLoginData(userData)
    }
}
