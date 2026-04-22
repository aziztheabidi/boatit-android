package com.boatit.boatsharing.features.signup.captain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.features.signup.captain.domain.usecase.FetchCaptainDocsUseCase
import com.boatit.boatsharing.features.signup.captain.model.GetCaptainDocumentResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GetCaptainDocsViewModel(
    private val fetchCaptainDocsUseCase: FetchCaptainDocsUseCase,
) : com.boatit.boatsharing.core.presentation.LegacyMviViewModel() {
    private val _registrationState = MutableStateFlow<NetworkResponse<GetCaptainDocumentResponse>>(NetworkResponse.Loading())
    val registrationState: StateFlow<NetworkResponse<GetCaptainDocumentResponse>> = _registrationState

    fun GetDocs() {
        viewModelScope.launch {
            _registrationState.value = NetworkResponse.Loading()
            when (val result = fetchCaptainDocsUseCase().toResource()) {
                is Resource.Success -> {
                    _registrationState.value = NetworkResponse.Success(result.data)
                }

                is Resource.Error -> {
                    _registrationState.value = NetworkResponse.Error(result.error)
                }

                Resource.Loading -> {
                    _registrationState.value = NetworkResponse.Loading()
                }
            }
        }
    }
}
