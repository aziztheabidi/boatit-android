package com.boatit.boatsharing.features.signup.captain.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.features.signup.captain.domain.usecase.SaveCaptainDocsUseCase
import com.boatit.boatsharing.features.signup.captain.model.GetCaptainDocumentResponse
import com.boatit.boatsharing.features.signup.captain.model.SaveCaptainDocumentRequest
import com.boatit.boatsharing.features.signup.captain.model.SaveCaptainDocumentResponse
import com.boatit.boatsharing.data.local.prefmanager.UserSessionStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CaptainDocsViewModel(
    private val saveCaptainDocsUseCase: SaveCaptainDocsUseCase,
    private val userSessionStore: UserSessionStore,
) : com.boatit.boatsharing.core.presentation.LegacyMviViewModel() {
    var licenseNo by mutableStateOf("")
    var licenseNoExpiryDate by mutableStateOf("")
    var licenseType by mutableStateOf("")
    var insuranceCompany by mutableStateOf("")
    var policyNo by mutableStateOf("")
    var policyExpirationDate by mutableStateOf("")

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var isError by mutableStateOf(false)
    var isNetworkError by mutableStateOf(false)

    val isFormValid: Boolean
        get() =
            licenseNo.isNotEmpty() &&
                licenseNoExpiryDate.isNotEmpty() &&
                licenseType.isNotEmpty() &&
                insuranceCompany.isNotEmpty() &&
                policyNo.isNotEmpty() &&
                policyExpirationDate.isNotEmpty()

    private val _registrationState = MutableStateFlow<NetworkResponse<SaveCaptainDocumentResponse>>(NetworkResponse.Loading())
    val registrationState: StateFlow<NetworkResponse<SaveCaptainDocumentResponse>> = _registrationState

    fun saveDocs() {
        viewModelScope.launch {
            isLoading = true
            _registrationState.value = NetworkResponse.Loading()
            val result =
                saveCaptainDocsUseCase(
                    SaveCaptainDocumentRequest(
                        UserId = userSessionStore.currentUserId(),
                        LicenseNumber = licenseNo,
                        LicenseExpiration = licenseNoExpiryDate,
                        TypeOfLicense = licenseType,
                        InsuranceCompany = insuranceCompany,
                        PolicyNumber = policyNo,
                        PolicyExpiration = policyExpirationDate,
                    ),
                )
            when (val resource = result.toResource()) {
                is Resource.Success -> {
                    _registrationState.value = NetworkResponse.Success(resource.data)
                    isLoading = false
                    isNetworkError = false
                }

                is Resource.Error -> {
                    val message = resource.error.toMessage()
                    _registrationState.value = NetworkResponse.Error(resource.error)
                    isLoading = false
                    isNetworkError = true
                    errorMessage = message
                    isError = true
                }

                Resource.Loading -> {
                    _registrationState.value = NetworkResponse.Loading()
                    isLoading = true
                }
            }
        }
    }

    fun clearError() {
        errorMessage = null
        isError = false
    }

    fun loadInitialData(data: GetCaptainDocumentResponse?) {
        licenseNo = data?.obj?.LicenseNumber.orEmpty()
        licenseNoExpiryDate = data?.obj?.LicenseExpiration.orEmpty()
        licenseType = data?.obj?.TypeOfLicense.orEmpty()
        insuranceCompany = data?.obj?.InsuranceCompany.orEmpty()
        policyNo = data?.obj?.PolicyNumber.orEmpty()
        policyExpirationDate = data?.obj?.PolicyExpiration.orEmpty()
    }
}
