package com.boatit.boatsharing.features.signup.captain.viewmodel

import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.core.presentation.BaseViewModel
import com.boatit.boatsharing.core.presentation.UiEffect
import com.boatit.boatsharing.core.presentation.UiEvent
import com.boatit.boatsharing.core.presentation.UiState
import com.boatit.boatsharing.data.local.prefmanager.UserSessionStore
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.features.signup.captain.domain.usecase.SaveCaptainDocsUseCase
import com.boatit.boatsharing.features.signup.captain.model.GetCaptainDocumentResponse
import com.boatit.boatsharing.features.signup.captain.model.SaveCaptainDocumentRequest
import com.boatit.boatsharing.features.signup.captain.model.SaveCaptainDocumentResponse
import kotlinx.coroutines.launch

data class CaptainDocsUiState(
    val licenseNo: String = "",
    val licenseNoExpiryDate: String = "",
    val licenseType: String = "",
    val insuranceCompany: String = "",
    val policyNo: String = "",
    val policyExpirationDate: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isError: Boolean = false,
    val isNetworkError: Boolean = false,
    val registrationState: NetworkResponse<SaveCaptainDocumentResponse> = NetworkResponse.Loading(),
) : UiState {
    val isFormValid: Boolean
        get() =
            licenseNo.isNotEmpty() &&
                licenseNoExpiryDate.isNotEmpty() &&
                licenseType.isNotEmpty() &&
                insuranceCompany.isNotEmpty() &&
                policyNo.isNotEmpty() &&
                policyExpirationDate.isNotEmpty()
}

sealed interface CaptainDocsUiEvent : UiEvent {
    data class LicenseNoChanged(val value: String) : CaptainDocsUiEvent

    data class LicenseExpiryChanged(val value: String) : CaptainDocsUiEvent

    data class LicenseTypeChanged(val value: String) : CaptainDocsUiEvent

    data class InsuranceCompanyChanged(val value: String) : CaptainDocsUiEvent

    data class PolicyNoChanged(val value: String) : CaptainDocsUiEvent

    data class PolicyExpirationChanged(val value: String) : CaptainDocsUiEvent

    data class LoadInitial(val data: GetCaptainDocumentResponse?) : CaptainDocsUiEvent

    data object ClearError : CaptainDocsUiEvent

    data object SaveDocs : CaptainDocsUiEvent
}

sealed interface CaptainDocsUiEffect : UiEffect {
    data object NoOpEffect : CaptainDocsUiEffect
}

class CaptainDocsViewModel(
    private val saveCaptainDocsUseCase: SaveCaptainDocsUseCase,
    private val userSessionStore: UserSessionStore,
) : BaseViewModel<CaptainDocsUiState, CaptainDocsUiEvent, CaptainDocsUiEffect>(CaptainDocsUiState()) {
    override fun onEvent(event: CaptainDocsUiEvent) {
        when (event) {
            is CaptainDocsUiEvent.LicenseNoChanged -> updateState { copy(licenseNo = event.value) }
            is CaptainDocsUiEvent.LicenseExpiryChanged -> updateState { copy(licenseNoExpiryDate = event.value) }
            is CaptainDocsUiEvent.LicenseTypeChanged -> updateState { copy(licenseType = event.value) }
            is CaptainDocsUiEvent.InsuranceCompanyChanged -> updateState { copy(insuranceCompany = event.value) }
            is CaptainDocsUiEvent.PolicyNoChanged -> updateState { copy(policyNo = event.value) }
            is CaptainDocsUiEvent.PolicyExpirationChanged -> updateState { copy(policyExpirationDate = event.value) }
            is CaptainDocsUiEvent.LoadInitial -> loadInitialData(event.data)
            CaptainDocsUiEvent.ClearError -> clearError()
            CaptainDocsUiEvent.SaveDocs -> saveDocs()
        }
    }

    fun saveDocs() {
        val s = currentState
        viewModelScope.launch {
            updateState { copy(isLoading = true, registrationState = NetworkResponse.Loading()) }
            val result =
                saveCaptainDocsUseCase(
                    SaveCaptainDocumentRequest(
                        UserId = userSessionStore.currentUserId(),
                        LicenseNumber = s.licenseNo,
                        LicenseExpiration = s.licenseNoExpiryDate,
                        TypeOfLicense = s.licenseType,
                        InsuranceCompany = s.insuranceCompany,
                        PolicyNumber = s.policyNo,
                        PolicyExpiration = s.policyExpirationDate,
                    ),
                )
            when (val resource = result.toResource()) {
                is Resource.Success -> {
                    updateState {
                        copy(
                            registrationState = NetworkResponse.Success(resource.data),
                            isLoading = false,
                            isNetworkError = false,
                        )
                    }
                }

                is Resource.Error -> {
                    val message = resource.error.toMessage()
                    updateState {
                        copy(
                            registrationState = NetworkResponse.Error(resource.error),
                            isLoading = false,
                            isNetworkError = true,
                            errorMessage = message,
                            isError = true,
                        )
                    }
                }

                Resource.Loading -> {
                    updateState { copy(registrationState = NetworkResponse.Loading(), isLoading = true) }
                }
            }
        }
    }

    fun clearError() {
        updateState { copy(errorMessage = null, isError = false) }
    }

    fun loadInitialData(data: GetCaptainDocumentResponse?) {
        updateState {
            copy(
                licenseNo = data?.obj?.LicenseNumber.orEmpty(),
                licenseNoExpiryDate = data?.obj?.LicenseExpiration.orEmpty(),
                licenseType = data?.obj?.TypeOfLicense.orEmpty(),
                insuranceCompany = data?.obj?.InsuranceCompany.orEmpty(),
                policyNo = data?.obj?.PolicyNumber.orEmpty(),
                policyExpirationDate = data?.obj?.PolicyExpiration.orEmpty(),
            )
        }
    }
}
