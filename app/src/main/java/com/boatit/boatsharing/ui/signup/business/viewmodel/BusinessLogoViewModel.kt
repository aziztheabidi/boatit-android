package com.boatit.boatsharing.ui.signup.business.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.ui.signup.business.model.SaveBusinessLogoResponse
import com.boatit.boatsharing.ui.signup.business.repository.BusinessLogoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File

class BusinessLogoViewModel(private val businessLogoRepository: BusinessLogoRepository
) : ViewModel() {

    private val _registrationState = MutableStateFlow<NetworkResponse<SaveBusinessLogoResponse>>(NetworkResponse.Loading())
    val registrationState: StateFlow<NetworkResponse<SaveBusinessLogoResponse>> = _registrationState

    fun uploadBusinessLogo(userId: String, logoFile: File) {
        viewModelScope.launch {
            _registrationState.value = NetworkResponse.Loading()
            val result = businessLogoRepository.saveBusinessLogo(userId, logoFile)
            result.onSuccess { response ->
                _registrationState.value = NetworkResponse.Success(response)
            }.onFailure { error ->
                _registrationState.value = NetworkResponse.Error(error.message ?: "Unknown error occurred")
            }
        }
    }
}


