package com.boatit.boatsharing.features.signup.captain.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.features.signup.captain.domain.usecase.SaveCaptainBoatUseCase
import com.boatit.boatsharing.features.signup.captain.model.GetCaptainBoatResponse
import com.boatit.boatsharing.features.signup.captain.model.SaveCaptainBoatRequest
import com.boatit.boatsharing.features.signup.captain.model.SaveCaptainBoatResponse
import com.boatit.boatsharing.data.local.prefmanager.SharedPrefManager
import com.boatit.boatsharing.data.local.prefmanager.UserSessionStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CaptainBoatViewModel(
    private val saveCaptainBoatUseCase: SaveCaptainBoatUseCase,
    private val sharedPrefManager: SharedPrefManager,
    private val userSessionStore: UserSessionStore,
) : com.boatit.boatsharing.core.presentation.LegacyMviViewModel() {
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
        get() =
            boatName.isNotEmpty() &&
                boatMake.isNotEmpty() &&
                boatModel.isNotEmpty() &&
                boatYear.isNotEmpty() &&
                boatSize.isNotEmpty() &&
                boatCapacity.isNotEmpty()

    fun onClearError() {
        isError = false
        errorMessage = null
    }

    fun saveProfile() {
        val request =
            SaveCaptainBoatRequest(
                UserId = userSessionStore.currentUserId(),
                Name = boatName,
                Make = boatMake,
                Model = boatModel,
                Year = boatYear.toIntOrNull() ?: 0,
                Size = boatSize.toIntOrNull() ?: 0,
                Capacity = boatCapacity.toIntOrNull() ?: 0,
            )

        viewModelScope.launch {
            isLoading = true
            _registrationState.value = NetworkResponse.Loading()
            when (val result = saveCaptainBoatUseCase(request).toResource()) {
                is Resource.Success -> {
                    _registrationState.value = NetworkResponse.Success(result.data)
                    saveLoginData(0)
                }

                is Resource.Error -> {
                    isLoading = false
                    isError = true
                    errorMessage = result.error.toMessage()
                    _registrationState.value = NetworkResponse.Error(result.error)
                }

                Resource.Loading -> {
                    _registrationState.value = NetworkResponse.Loading()
                    isLoading = true
                }
            }
        }
    }

    fun onRegistrationHandled() {
        isLoading = false
        isButtonClicked = false
    }

    fun loadInitialData(data: GetCaptainBoatResponse?) {
        boatName = data?.obj?.Name.orEmpty()
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
