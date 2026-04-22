package com.boatit.boatsharing.features.voyager.dashboard.viewmodel

import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.core.presentation.BaseViewModel
import com.boatit.boatsharing.core.presentation.UiEffect
import com.boatit.boatsharing.core.presentation.UiEvent
import com.boatit.boatsharing.core.presentation.UiState
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.features.voyager.dashboard.domain.usecase.FetchVoyagerPastVoyagesUseCase
import com.boatit.boatsharing.features.voyager.dashboard.model.VoyagerVoyagesResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class VoyagerVoyagesUiState(
    val isLoading: Boolean = false,
    val voyagesResponse: VoyagerVoyagesResponse? = null,
    val errorMessage: String? = null,
) : UiState

sealed interface VoyagerVoyagesUiEvent : UiEvent {
    data object FetchVoyages : VoyagerVoyagesUiEvent
}

sealed interface VoyagerVoyagesUiEffect : UiEffect {
    data class ShowToast(val message: String) : VoyagerVoyagesUiEffect
}

class VoyagerVoyagesViewModel(
    private val fetchVoyagerPastVoyagesUseCase: FetchVoyagerPastVoyagesUseCase,
) : BaseViewModel<VoyagerVoyagesUiState, VoyagerVoyagesUiEvent, VoyagerVoyagesUiEffect>(VoyagerVoyagesUiState()) {
    private val _loginState = MutableStateFlow<NetworkResponse<VoyagerVoyagesResponse>>(NetworkResponse.Loading())
    val loginState: StateFlow<NetworkResponse<VoyagerVoyagesResponse>> = _loginState

    override fun onEvent(event: VoyagerVoyagesUiEvent) {
        when (event) {
            VoyagerVoyagesUiEvent.FetchVoyages -> voyages()
        }
    }

    fun voyages() {
        viewModelScope.launch {
            updateState { copy(isLoading = true, errorMessage = null) }
            _loginState.value = NetworkResponse.Loading()

            when (val result = fetchVoyagerPastVoyagesUseCase().toResource()) {
                is Resource.Success -> {
                    updateState { copy(isLoading = false, voyagesResponse = result.data, errorMessage = null) }
                    _loginState.value = NetworkResponse.Success(result.data)
                }

                is Resource.Error -> {
                    val message = result.error.toMessage()
                    updateState { copy(isLoading = false, errorMessage = message) }
                    _loginState.value = NetworkResponse.Error(result.error)
                    emitEffect(VoyagerVoyagesUiEffect.ShowToast(message))
                }

                Resource.Loading -> {
                    updateState { copy(isLoading = true) }
                    _loginState.value = NetworkResponse.Loading()
                }
            }
        }
    }
}
