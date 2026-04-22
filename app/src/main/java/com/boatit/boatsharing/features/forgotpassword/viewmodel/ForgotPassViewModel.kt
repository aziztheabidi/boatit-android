package com.boatit.boatsharing.features.forgotpassword.viewmodel

import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.core.presentation.BaseViewModel
import com.boatit.boatsharing.core.presentation.UiEffect
import com.boatit.boatsharing.core.presentation.UiEvent
import com.boatit.boatsharing.core.presentation.UiState
import com.boatit.boatsharing.features.forgotpassword.domain.model.ForgotPasswordDomainModel
import com.boatit.boatsharing.features.forgotpassword.domain.usecase.SendForgotPasswordUseCase
import kotlinx.coroutines.launch

data class ForgotPassUiState(
    val email: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
) : UiState {
    val isEmailValid: Boolean
        get() = email.contains("@") && email.contains(".")

    val isFormValid: Boolean
        get() = email.isNotEmpty() && isEmailValid
}

sealed interface ForgotPassUiEvent : UiEvent {
    data class EmailChanged(val value: String) : ForgotPassUiEvent

    data object Submit : ForgotPassUiEvent

    data object ClearError : ForgotPassUiEvent
}

sealed interface ForgotPassUiEffect : UiEffect {
    data class ShowToast(val message: String) : ForgotPassUiEffect

    data object NavigateToLogin : ForgotPassUiEffect
}

class ForgotPassViewModel(
    private val sendForgotPasswordUseCase: SendForgotPasswordUseCase,
) : BaseViewModel<ForgotPassUiState, ForgotPassUiEvent, ForgotPassUiEffect>(ForgotPassUiState()) {
    override fun onEvent(event: ForgotPassUiEvent) {
        when (event) {
            is ForgotPassUiEvent.EmailChanged -> {
                updateState { copy(email = event.value, errorMessage = null) }
            }

            ForgotPassUiEvent.Submit -> forgotPass()
            ForgotPassUiEvent.ClearError -> updateState { copy(errorMessage = null) }
        }
    }

    private fun forgotPass() {
        if (!currentState.isFormValid || currentState.isLoading) return

        viewModelScope.launch {
            updateState { copy(isLoading = true, errorMessage = null) }

            when (val result = sendForgotPasswordUseCase(currentState.email).toResource()) {
                is Resource.Success -> {
                    handleSuccess(result.data)
                }

                is Resource.Error -> {
                    val message = result.error.toMessage()
                    updateState { copy(isLoading = false, errorMessage = message) }
                    emitEffect(ForgotPassUiEffect.ShowToast(message))
                }

                Resource.Loading -> {
                    updateState { copy(isLoading = true) }
                }
            }
        }
    }

    private fun handleSuccess(response: ForgotPasswordDomainModel) {
        updateState { copy(isLoading = false, errorMessage = null) }
        emitEffect(ForgotPassUiEffect.ShowToast(response.message.ifBlank { "Success" }))
        emitEffect(ForgotPassUiEffect.NavigateToLogin)
    }
}
