package com.boatit.boatsharing.features.signup.general.viewmodel

import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.core.presentation.BaseViewModel
import com.boatit.boatsharing.core.presentation.UiEffect
import com.boatit.boatsharing.core.presentation.UiEvent
import com.boatit.boatsharing.core.presentation.UiState
import com.boatit.boatsharing.features.signup.general.domain.model.VoyagerProfileDomainResult
import com.boatit.boatsharing.features.signup.general.domain.usecase.SaveVoyagerProfileUseCase
import com.boatit.boatsharing.features.signup.general.model.VoyagerProfileRequest
import com.boatit.boatsharing.data.local.prefmanager.SharedPrefManager
import kotlinx.coroutines.launch

data class VoyagerProfileUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
) : UiState

sealed interface VoyagerProfileUiEvent : UiEvent {
    data class Submit(val profile: VoyagerProfileRequest) : VoyagerProfileUiEvent

    data object ClearError : VoyagerProfileUiEvent
}

sealed interface VoyagerProfileUiEffect : UiEffect {
    data class ShowToast(val message: String) : VoyagerProfileUiEffect

    data object SaveSuccess : VoyagerProfileUiEffect
}

class VoyagerProfileViewModel(
    private val saveVoyagerProfileUseCase: SaveVoyagerProfileUseCase,
    private val sharedPrefManager: SharedPrefManager,
) : BaseViewModel<VoyagerProfileUiState, VoyagerProfileUiEvent, VoyagerProfileUiEffect>(VoyagerProfileUiState()) {
    override fun onEvent(event: VoyagerProfileUiEvent) {
        when (event) {
            is VoyagerProfileUiEvent.Submit -> saveProfileInternal(event.profile)
            VoyagerProfileUiEvent.ClearError -> updateState { copy(errorMessage = null) }
        }
    }

    fun saveProfile(profile: VoyagerProfileRequest) {
        onEvent(VoyagerProfileUiEvent.Submit(profile))
    }

    private fun saveProfileInternal(profile: VoyagerProfileRequest) {
        if (currentState.isLoading) return

        viewModelScope.launch {
            updateState { copy(isLoading = true, errorMessage = null) }

            when (val result = saveVoyagerProfileUseCase(profile).toResource()) {
                is Resource.Success -> handleSuccess(result.data)
                is Resource.Error -> {
                    val message = result.error.toMessage()
                    updateState { copy(isLoading = false, errorMessage = message) }
                    emitEffect(VoyagerProfileUiEffect.ShowToast(message))
                }
                Resource.Loading -> updateState { copy(isLoading = true) }
            }
        }
    }

    private fun handleSuccess(response: VoyagerProfileDomainResult) {
        saveLoginData(0)
        updateState { copy(isLoading = false, errorMessage = null) }
        emitEffect(VoyagerProfileUiEffect.ShowToast(response.message.ifBlank { "Profile saved" }))
        emitEffect(VoyagerProfileUiEffect.SaveSuccess)
    }

    private fun saveLoginData(userData: Int) {
        sharedPrefManager.saveMissingStep(userData)
    }
}
