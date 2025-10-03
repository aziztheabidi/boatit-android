package com.boatit.boatsharing.ui.signup.captain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.ui.signup.captain.model.GetCaptainDocumentResponse
import com.boatit.boatsharing.ui.signup.captain.model.SaveCaptainBoatRequest
import com.boatit.boatsharing.ui.signup.captain.model.SaveCaptainBoatResponse
import com.boatit.boatsharing.ui.signup.captain.repository.CaptainBoatRepository
import com.boatit.boatsharing.ui.signup.captain.repository.GetCaptainDocsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GetCaptainDocsViewModel(private val repository: GetCaptainDocsRepository) : ViewModel() {

    private val _registrationState = MutableStateFlow<NetworkResponse<GetCaptainDocumentResponse>>(NetworkResponse.Loading())
    val registrationState: StateFlow<NetworkResponse<GetCaptainDocumentResponse>> = _registrationState

    fun GetDocs() {
        viewModelScope.launch {
            _registrationState.value = NetworkResponse.Loading()
            val result = repository.GetCaptainDocs()
            result.onSuccess { placesResponse ->
                _registrationState.value = NetworkResponse.Success(placesResponse)
            }.onFailure { error ->
                _registrationState.value = NetworkResponse.Error(error.message ?: "Registration failed")
            }
        }
    }
}


