package com.boatit.boatsharing.features.signup.general.viewmodel

import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.core.presentation.BaseViewModel
import com.boatit.boatsharing.core.presentation.UiEffect
import com.boatit.boatsharing.core.presentation.UiEvent
import com.boatit.boatsharing.core.presentation.UiState
import com.boatit.boatsharing.features.signup.general.domain.model.VoyagerProfileDomainModel
import com.boatit.boatsharing.features.signup.general.domain.usecase.FetchVoyagerProfileUseCase
import kotlinx.coroutines.launch

data class GetVoyagerProfileUiState(
    val isLoading: Boolean = false,
    val profile: VoyagerProfileDomainModel? = null,
    val errorMessage: String? = null,
) : UiState

sealed interface GetVoyagerProfileUiEvent : UiEvent {
    data object Load : GetVoyagerProfileUiEvent
}

sealed interface GetVoyagerProfileUiEffect : UiEffect {
    data class ShowToast(val message: String) : GetVoyagerProfileUiEffect
}

class GetVoyagerProfileViewModel(
    private val fetchVoyagerProfileUseCase: FetchVoyagerProfileUseCase,
) : BaseViewModel<GetVoyagerProfileUiState, GetVoyagerProfileUiEvent, GetVoyagerProfileUiEffect>(GetVoyagerProfileUiState()) {
    override fun onEvent(event: GetVoyagerProfileUiEvent) {
        when (event) {
            GetVoyagerProfileUiEvent.Load -> loadProfileInternal()
        }
    }

    fun fetchVoyagerProfile() {
        onEvent(GetVoyagerProfileUiEvent.Load)
    }

    @Deprecated("Use fetchVoyagerProfile")
    fun GetVoyagerProfile() {
        fetchVoyagerProfile()
    }

    private fun loadProfileInternal() {
        if (currentState.isLoading) return

        viewModelScope.launch {
            updateState { copy(isLoading = true, errorMessage = null) }

            when (val result = fetchVoyagerProfileUseCase().toResource()) {
                is Resource.Success -> handleSuccess(result.data)
                is Resource.Error -> {
                    val message = result.error.toMessage()
                    updateState { copy(isLoading = false, errorMessage = message) }
                    emitEffect(GetVoyagerProfileUiEffect.ShowToast(message))
                }
                Resource.Loading -> updateState { copy(isLoading = true) }
            }
        }
    }

    private fun handleSuccess(response: VoyagerProfileDomainModel?) {
        updateState {
            copy(
                isLoading = false,
                profile = response,
                errorMessage = null,
            )
        }
    }
}
