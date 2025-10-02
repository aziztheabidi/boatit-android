package com.boatit.boatsharing.ui.signup.captain.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.ui.signup.captain.model.GetCaptainDocumentResponse
import com.boatit.boatsharing.ui.signup.captain.model.SaveCaptainDocumentRequest
import com.boatit.boatsharing.ui.signup.captain.model.SaveCaptainDocumentResponse
import com.boatit.boatsharing.ui.signup.captain.repository.CaptainDocsRepository
import com.boatit.boatsharing.utils.AppConstants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CaptainDocsViewModel(private val repository: CaptainDocsRepository) : ViewModel() {

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
        get() = licenseNo.isNotEmpty() &&
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
            val result = repository.CaptainDocs(
                SaveCaptainDocumentRequest(
                    UserId = AppConstants.USER_ID.toString(),
                    LicenseNumber = licenseNo,
                    LicenseExpiration = licenseNoExpiryDate,
                    TypeOfLicense = licenseType,
                    InsuranceCompany = insuranceCompany,
                    PolicyNumber = policyNo,
                    PolicyExpiration = policyExpirationDate
                )
            )
            result.onSuccess {
                _registrationState.value = NetworkResponse.Success(it)
                isLoading = false
                isNetworkError = false
            }.onFailure {
                _registrationState.value = NetworkResponse.Error(it.message ?: "Registration failed")
                isLoading = false
                isNetworkError = true
                errorMessage = it.message
                isError = true
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


