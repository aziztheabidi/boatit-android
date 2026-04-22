package com.boatit.boatsharing.features.signup.general.viewmodel

import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.core.presentation.BaseViewModel
import com.boatit.boatsharing.core.presentation.UiEffect
import com.boatit.boatsharing.core.presentation.UiEvent
import com.boatit.boatsharing.core.presentation.UiState
import com.boatit.boatsharing.features.signup.general.domain.model.VerifyEmailDomainModel
import com.boatit.boatsharing.features.signup.general.domain.usecase.VerifySignupEmailUseCase
import kotlinx.coroutines.launch

data class VerifyEmailUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
) : UiState

sealed interface VerifyEmailUiEvent : UiEvent {
    data class Submit(val email: String, val otp: String) : VerifyEmailUiEvent

    data object ClearError : VerifyEmailUiEvent
}

sealed interface VerifyEmailUiEffect : UiEffect {
    data class ShowToast(val message: String) : VerifyEmailUiEffect

    data class NavigateToCreatePassword(val token: String) : VerifyEmailUiEffect
}

class VerifyEmailViewModel(
    private val verifySignupEmailUseCase: VerifySignupEmailUseCase,
) : BaseViewModel<VerifyEmailUiState, VerifyEmailUiEvent, VerifyEmailUiEffect>(VerifyEmailUiState()) {
    override fun onEvent(event: VerifyEmailUiEvent) {
        when (event) {
            is VerifyEmailUiEvent.Submit -> verifyEmailInternal(event.email, event.otp)
            VerifyEmailUiEvent.ClearError -> updateState { copy(errorMessage = null) }
        }
    }

    fun verifyEmail(
        email: String,
        otp: String,
    ) {
        onEvent(VerifyEmailUiEvent.Submit(email, otp))
    }

    private fun verifyEmailInternal(
        email: String,
        otp: String,
    ) {
        if (currentState.isLoading) return

        viewModelScope.launch {
            updateState { copy(isLoading = true, errorMessage = null) }

            when (val result = verifySignupEmailUseCase(email, otp).toResource()) {
                is Resource.Success -> handleSuccess(result.data)
                is Resource.Error -> {
                    val message = result.error.toMessage()
                    updateState { copy(isLoading = false, errorMessage = message) }
                    emitEffect(VerifyEmailUiEffect.ShowToast(message))
                }
                Resource.Loading -> updateState { copy(isLoading = true) }
            }
        }
    }

    private fun handleSuccess(response: VerifyEmailDomainModel) {
        updateState { copy(isLoading = false, errorMessage = null) }
        emitEffect(VerifyEmailUiEffect.ShowToast(response.message.ifBlank { "Email verified" }))
        val token = response.verificationToken.orEmpty()
        if (token.isNotBlank()) {
            emitEffect(VerifyEmailUiEffect.NavigateToCreatePassword(token))
        }
    }
}
