package com.boatit.boatsharing.features.signup.captain.viewmodel

import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.core.presentation.BaseViewModel
import com.boatit.boatsharing.core.presentation.UiEffect
import com.boatit.boatsharing.core.presentation.UiEvent
import com.boatit.boatsharing.core.presentation.UiState
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.features.signup.captain.domain.usecase.FetchCaptainDocsUseCase
import com.boatit.boatsharing.features.signup.captain.model.GetCaptainDocumentResponse
import kotlinx.coroutines.launch

data class GetCaptainDocsUiState(
    val registrationState: NetworkResponse<GetCaptainDocumentResponse> = NetworkResponse.Loading(),
) : UiState

sealed interface GetCaptainDocsUiEvent : UiEvent {
    data object Fetch : GetCaptainDocsUiEvent
}

sealed interface GetCaptainDocsUiEffect : UiEffect {
    data object NoOpEffect : GetCaptainDocsUiEffect
}

class GetCaptainDocsViewModel(
    private val fetchCaptainDocsUseCase: FetchCaptainDocsUseCase,
) : BaseViewModel<GetCaptainDocsUiState, GetCaptainDocsUiEvent, GetCaptainDocsUiEffect>(
        GetCaptainDocsUiState(),
    ) {
    override fun onEvent(event: GetCaptainDocsUiEvent) {
        when (event) {
            GetCaptainDocsUiEvent.Fetch -> fetchDocs()
        }
    }

    fun GetDocs() {
        onEvent(GetCaptainDocsUiEvent.Fetch)
    }

    private fun fetchDocs() {
        viewModelScope.launch {
            updateState { copy(registrationState = NetworkResponse.Loading()) }
            when (val result = fetchCaptainDocsUseCase().toResource()) {
                is Resource.Success -> {
                    updateState { copy(registrationState = NetworkResponse.Success(result.data)) }
                }

                is Resource.Error -> {
                    updateState { copy(registrationState = NetworkResponse.Error(result.error)) }
                }

                Resource.Loading -> {
                    updateState { copy(registrationState = NetworkResponse.Loading()) }
                }
            }
        }
    }
}
