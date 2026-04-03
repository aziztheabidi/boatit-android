package com.boatit.boatsharing.ui.signup.captain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.ui.signup.captain.model.SaveCaptainDocumentRequest
import com.boatit.boatsharing.ui.signup.captain.model.SaveCaptainDocumentResponse
import com.boatit.boatsharing.ui.signup.captain.repository.CaptainDocsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CaptainDocsViewModel(private val repository: CaptainDocsRepository) : ViewModel() {

    private val _registrationState = MutableStateFlow<NetworkResponse<SaveCaptainDocumentResponse>>(NetworkResponse.Loading())
    val registrationState: StateFlow<NetworkResponse<SaveCaptainDocumentResponse>> = _registrationState

    fun saveDocs(profile: SaveCaptainDocumentRequest) {
        viewModelScope.launch {
            _registrationState.value = NetworkResponse.Loading()
            val result = repository.CaptainDocs(profile)
            result.onSuccess { placesResponse ->
                _registrationState.value = NetworkResponse.Success(placesResponse)
            }.onFailure { error ->
                _registrationState.value = NetworkResponse.Error(error.message ?: "Registration failed")
            }
        }
    }
}


