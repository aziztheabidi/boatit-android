package com.boatit.boatsharing.ui.signup.captain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.network.networkreposne.NetworkResponse
import com.boatit.boatsharing.ui.signup.captain.model.CaptainProfileRequest
import com.boatit.boatsharing.ui.signup.captain.model.CaptainProfileResponse
import com.boatit.boatsharing.ui.signup.captain.model.GetCaptainProfileResponse
import com.boatit.boatsharing.ui.signup.captain.repository.CaptainProfileRepository
import com.boatit.boatsharing.ui.signup.general.model.VoyagerProfileRequest
import com.boatit.boatsharing.ui.signup.general.model.VoyagerProfileResponse
import com.boatit.boatsharing.ui.signup.general.viewmodel.VoyagerProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CaptainProfileViewModel(private val repository: CaptainProfileRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(CaptainProfileUiState())
    val uiState: StateFlow<CaptainProfileUiState> = _uiState

    val registrationState = MutableStateFlow<NetworkResponse<CaptainProfileResponse>>(NetworkResponse.Loading())

    fun onFieldChange(field: (CaptainProfileUiState) -> CaptainProfileUiState) {
        _uiState.update { field(it) }
    }

    fun toggleDatePicker(show: Boolean) {
        _uiState.update { it.copy(showDateDialog = show) }
    }

    fun updateDateOfBirth(date: String) {
        _uiState.update { it.copy(dateOfBirth = date, showDateDialog = false) }
    }

    fun saveProfile(userId: String) {
        val state = _uiState.value
        val request = CaptainProfileRequest(
            UserId = userId,
            PhoneNumber = state.phoneNumber,
            FirstName = state.firstName,
            LastName = state.lastName,
            Address = state.address,
            DateOfBirth = state.dateOfBirth,
            StripeEmail = state.stripeEmail,
        )

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = repository.CaptainProfile(request)
            result.onSuccess {
                _uiState.update { it.copy(isLoading = false, isSuccess = true, isNetworkError = false) }
                registrationState.value = NetworkResponse.Success(it)
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "Network error",
                        isNetworkError = true
                    )
                }
                registrationState.value = NetworkResponse.Error(error.message ?: "Registration failed")
            }
        }
    }

    fun resetSuccessState() {
        _uiState.update { it.copy(isSuccess = false) }
    }
}

data class CaptainProfileUiState(
    val firstName: String = "",
    val lastName: String = "",
    val phoneNumber: String = "",
    val address: String = "",
    val dateOfBirth: String = "",
    val stripeEmail: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false,
    val isNetworkError: Boolean = false,
    val showDateDialog: Boolean = false,
)



