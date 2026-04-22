package com.boatit.boatsharing.features.signup.general.viewmodel

import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.core.presentation.BaseViewModel
import com.boatit.boatsharing.core.presentation.UiEffect
import com.boatit.boatsharing.core.presentation.UiEvent
import com.boatit.boatsharing.core.presentation.UiState
import com.boatit.boatsharing.features.signup.general.domain.model.RegistrationDomainModel
import com.boatit.boatsharing.features.signup.general.domain.usecase.RegisterUserUseCase
import kotlinx.coroutines.launch

data class RegistrationUiState(
    val email: String = "",
    val name: String = "",
    val phoneNumber: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
) : UiState {
    val isEmailValid: Boolean
        get() = email.contains("@") && email.contains(".")

    val isNameValid: Boolean
        get() = name.length > 3

    val isPhoneValid: Boolean
        get() = phoneNumber.length > 3

    val isFormValid: Boolean
        get() =
            email.isNotBlank() && name.isNotBlank() && phoneNumber.isNotBlank() &&
                isEmailValid && isNameValid && isPhoneValid
}

sealed interface RegistrationUiEvent : UiEvent {
    data class EmailChanged(val value: String) : RegistrationUiEvent

    data class NameChanged(val value: String) : RegistrationUiEvent

    data class PhoneChanged(val value: String) : RegistrationUiEvent

    data object Submit : RegistrationUiEvent

    data object ClearError : RegistrationUiEvent
}

sealed interface RegistrationUiEffect : UiEffect {
    data class ShowToast(val message: String) : RegistrationUiEffect

    data class NavigateToNext(val email: String) : RegistrationUiEffect
}

class RegistrationViewModel(
    private val registerUserUseCase: RegisterUserUseCase,
) : BaseViewModel<RegistrationUiState, RegistrationUiEvent, RegistrationUiEffect>(RegistrationUiState()) {
    override fun onEvent(event: RegistrationUiEvent) {
        when (event) {
            is RegistrationUiEvent.EmailChanged -> updateState { copy(email = event.value, errorMessage = null) }
            is RegistrationUiEvent.NameChanged -> updateState { copy(name = event.value, errorMessage = null) }
            is RegistrationUiEvent.PhoneChanged -> updateState { copy(phoneNumber = event.value, errorMessage = null) }
            RegistrationUiEvent.ClearError -> updateState { copy(errorMessage = null) }
            RegistrationUiEvent.Submit -> registerInternal()
        }
    }

    fun onEmailChange(value: String) {
        onEvent(RegistrationUiEvent.EmailChanged(value))
    }

    fun onNameChange(value: String) {
        onEvent(RegistrationUiEvent.NameChanged(value))
    }

    fun onPhoneChange(value: String) {
        onEvent(RegistrationUiEvent.PhoneChanged(value))
    }

    fun clearError() {
        onEvent(RegistrationUiEvent.ClearError)
    }

    fun register() {
        onEvent(RegistrationUiEvent.Submit)
    }

    private fun registerInternal() {
        if (!currentState.isFormValid || currentState.isLoading) return

        viewModelScope.launch {
            updateState { copy(isLoading = true, errorMessage = null) }

            when (
                val result =
                    registerUserUseCase(
                        username = currentState.name,
                        phoneNumber = currentState.phoneNumber,
                        email = currentState.email,
                    ).toResource()
            ) {
                is Resource.Success -> handleSuccess(result.data)
                is Resource.Error -> {
                    val message = result.error.toMessage()
                    updateState { copy(isLoading = false, errorMessage = message) }
                    emitEffect(RegistrationUiEffect.ShowToast(message))
                }
                Resource.Loading -> updateState { copy(isLoading = true) }
            }
        }
    }

    private fun handleSuccess(response: RegistrationDomainModel) {
        val message = response.message.ifBlank { "Registration successful" }
        val emailForNextStep = response.email?.takeIf { it.isNotBlank() } ?: currentState.email.trim()
        updateState { copy(isLoading = false, errorMessage = null) }
        emitEffect(RegistrationUiEffect.ShowToast(message))
        emitEffect(RegistrationUiEffect.NavigateToNext(emailForNextStep))
    }
}
