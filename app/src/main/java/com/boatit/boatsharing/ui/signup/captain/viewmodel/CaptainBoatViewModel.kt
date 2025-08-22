package com.boatit.boatsharing.ui.signup.captain.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.network.networkreposne.NetworkResponse
import com.boatit.boatsharing.ui.signup.captain.model.GetCaptainBoatResponse
import com.boatit.boatsharing.ui.signup.captain.model.GetCaptainDocumentResponse
import com.boatit.boatsharing.ui.signup.captain.model.SaveCaptainBoatRequest
import com.boatit.boatsharing.ui.signup.captain.model.SaveCaptainBoatResponse
import com.boatit.boatsharing.ui.signup.captain.repository.CaptainBoatRepository
import com.boatit.boatsharing.utils.AppConstants
import com.boatit.boatsharing.utils.prefmanager.SharedPrefManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CaptainBoatViewModel(private val repository: CaptainBoatRepository,
                           private val sharedPrefManager: SharedPrefManager
) : ViewModel() {

    var boatName by mutableStateOf("")
    var boatMake by mutableStateOf("")
    var boatModel by mutableStateOf("")
    var boatYear by mutableStateOf("")
    var boatSize by mutableStateOf("")
    var boatCapacity by mutableStateOf("")

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var isError by mutableStateOf(false)
    var isButtonClicked by mutableStateOf(false)

    private val _registrationState = MutableStateFlow<NetworkResponse<SaveCaptainBoatResponse>>(NetworkResponse.Loading())
    val registrationState: StateFlow<NetworkResponse<SaveCaptainBoatResponse>> = _registrationState

    val isValidate: Boolean
        get() = boatName.isNotEmpty()
                && boatMake.isNotEmpty()
                && boatModel.isNotEmpty()
                && boatYear.isNotEmpty()
                && boatSize.isNotEmpty()
                && boatCapacity.isNotEmpty()

    fun onClearError() {
        isError = false
        errorMessage = null
    }

    fun saveProfile() {
        val request = SaveCaptainBoatRequest(
            UserId = AppConstants.USER_ID.toString(),
            Name = boatName,
            Make = boatMake,
            Model = boatModel,
            Year = boatYear.toIntOrNull() ?: 0,
            Size = boatSize.toIntOrNull() ?: 0,
            Capacity = boatCapacity.toIntOrNull() ?: 0
        )

        viewModelScope.launch {
            isLoading = true
            _registrationState.value = NetworkResponse.Loading()
            val result = repository.CaptainBoat(request)
            result.onSuccess {
                _registrationState.value = NetworkResponse.Success(it)
                saveLoginData(0)
            }.onFailure { error ->
                isLoading = false
                isError = true
                errorMessage = error.message ?: "Registration failed"
                _registrationState.value = NetworkResponse.Error(errorMessage!!)
            }
        }
    }

    fun onRegistrationHandled() {
        isLoading = false
        isButtonClicked = false
    }

    fun loadInitialData(data: GetCaptainBoatResponse?) {
        boatName = data?.obj?.Name .orEmpty()
        boatMake = data?.obj?.Make.orEmpty()
        boatModel = data?.obj?.Model.orEmpty()
        boatYear = data?.obj?.Year.toString().orEmpty()
        boatSize = data?.obj?.Size.toString().orEmpty()
        boatCapacity = data?.obj?.Capacity.toString().orEmpty()
    }

    private fun saveLoginData(userData: Int) {
        sharedPrefManager.saveMissingStep(userData)
    }
}



