package com.boatit.boatsharing.features.voyager.dashboard.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.core.presentation.BaseViewModel
import com.boatit.boatsharing.core.presentation.UiEffect
import com.boatit.boatsharing.core.presentation.UiEvent
import com.boatit.boatsharing.core.presentation.UiState
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.features.voyager.dashboard.domain.usecase.FetchBusinessRelationshipsUseCase
import com.boatit.boatsharing.features.voyager.dashboard.model.BusinessRelationshipResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class FetchBusinessUiState(
    val isLoading: Boolean = false,
    val relationships: BusinessRelationshipResponse? = null,
    val errorMessage: String? = null,
    val selectedIndex: Int = 0,
) : UiState

sealed interface FetchBusinessUiEvent : UiEvent {
    data object Fetch : FetchBusinessUiEvent

    data class SelectIndex(val index: Int) : FetchBusinessUiEvent
}

sealed interface FetchBusinessUiEffect : UiEffect {
    data class ShowToast(val message: String) : FetchBusinessUiEffect
}

class FetchBusinessViewModel(
    private val fetchBusinessRelationshipsUseCase: FetchBusinessRelationshipsUseCase,
) : BaseViewModel<FetchBusinessUiState, FetchBusinessUiEvent, FetchBusinessUiEffect>(FetchBusinessUiState()) {
    private val _loginState = MutableStateFlow<NetworkResponse<BusinessRelationshipResponse>>(NetworkResponse.Loading())
    val loginState: StateFlow<NetworkResponse<BusinessRelationshipResponse>> = _loginState

    var selectedIndex = 0
        set(value) {
            field = value
            updateState { copy(selectedIndex = value) }
        }

    override fun onEvent(event: FetchBusinessUiEvent) {
        when (event) {
            FetchBusinessUiEvent.Fetch -> voyages()
            is FetchBusinessUiEvent.SelectIndex -> {
                selectedIndex = event.index
            }
        }
    }

    fun voyages() {
        viewModelScope.launch {
            updateState { copy(isLoading = true, errorMessage = null) }
            _loginState.value = NetworkResponse.Loading()

            when (val result = fetchBusinessRelationshipsUseCase().toResource()) {
                is Resource.Success -> {
                    Log.e("bussiness_response", result.data.toString())
                    updateState { copy(isLoading = false, relationships = result.data, errorMessage = null) }
                    _loginState.value = NetworkResponse.Success(result.data)
                }

                is Resource.Error -> {
                    val message = result.error.toMessage()
                    Log.e("bussiness_response", message)
                    updateState { copy(isLoading = false, errorMessage = message) }
                    _loginState.value = NetworkResponse.Error(result.error)
                    emitEffect(FetchBusinessUiEffect.ShowToast(message))
                }

                Resource.Loading -> {
                    updateState { copy(isLoading = true) }
                    _loginState.value = NetworkResponse.Loading()
                }
            }
        }
    }
}
