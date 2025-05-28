package com.boatit.boatsharing.ui.voyager.dashbaord.repository

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.network.networkreposne.NetworkResponse
import com.boatit.boatsharing.ui.signup.general.model.RegistrationResponse
import com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel.RegistrationRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegistrationViewModel(private val repository: RegistrationRepository) : ViewModel() {

    // Form state
    var email by mutableStateOf("")
        private set
    var name by mutableStateOf("")
        private set
    var phoneNumber by mutableStateOf("")
        private set

    // Loading/Error states
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)

    // Registration response state
    private val _registrationState = MutableStateFlow<NetworkResponse<RegistrationResponse>>(NetworkResponse.Loading())
    val registrationState: StateFlow<NetworkResponse<RegistrationResponse>> = _registrationState

    // Navigation trigger (e.g. success = go to next screen)
    private val _navigateToNext = MutableSharedFlow<String>()
    val navigateToNext: SharedFlow<String> = _navigateToNext

    // Validation logic
    val isEmailValid get() = email.contains("@") && email.contains(".")
    val isNameValid get() = name.length > 3
    val isPhoneValid get() = phoneNumber.length > 3
    val isFormValid get() = email.isNotBlank() && name.isNotBlank() && phoneNumber.isNotBlank() &&
            isEmailValid && isNameValid && isPhoneValid

    // Input change handlers
    fun onEmailChange(value: String) {
        email = value
        clearError()
    }

    fun onNameChange(value: String) {
        name = value
        clearError()
    }

    fun onPhoneChange(value: String) {
        phoneNumber = value
        clearError()
    }

    fun clearError() {
        errorMessage = null
    }

    fun register() {
        if (!isFormValid) return

        viewModelScope.launch {
            isLoading = true
            _registrationState.value = NetworkResponse.Loading()

            val result = repository.tempRegister(name, phoneNumber, email)

            result.onSuccess { response ->
                _registrationState.value = NetworkResponse.Success(response)
                isLoading = false
                _navigateToNext.emit(email) // Navigate to next screen
            }.onFailure { error ->
                _registrationState.value = NetworkResponse.Error(error.message ?: "Registration failed")
                isLoading = false
                errorMessage = error.message ?: "Unknown error"
            }
        }
    }
}



