package com.boatit.boatsharing.features.signup.captain.viewmodel

import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.core.presentation.BaseViewModel
import com.boatit.boatsharing.core.presentation.UiEffect
import com.boatit.boatsharing.core.presentation.UiEvent
import com.boatit.boatsharing.core.presentation.UiState
import com.boatit.boatsharing.data.local.prefmanager.SharedPrefManager
import com.boatit.boatsharing.data.local.prefmanager.UserSessionStore
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.features.signup.captain.domain.usecase.SaveCaptainBoatUseCase
import com.boatit.boatsharing.features.signup.captain.model.GetCaptainBoatResponse
import com.boatit.boatsharing.features.signup.captain.model.SaveCaptainBoatRequest
import com.boatit.boatsharing.features.signup.captain.model.SaveCaptainBoatResponse
import kotlinx.coroutines.launch

data class CaptainBoatUiState(
    val boatName: String = "",
    val boatMake: String = "",
    val boatModel: String = "",
    val boatYear: String = "",
    val boatSize: String = "",
    val boatCapacity: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isError: Boolean = false,
    val isButtonClicked: Boolean = false,
    val registrationState: NetworkResponse<SaveCaptainBoatResponse> = NetworkResponse.Loading(),
) : UiState {
    val isValidate: Boolean
        get() =
            boatName.isNotEmpty() &&
                boatMake.isNotEmpty() &&
                boatModel.isNotEmpty() &&
                boatYear.isNotEmpty() &&
                boatSize.isNotEmpty() &&
                boatCapacity.isNotEmpty()
}

sealed interface CaptainBoatUiEvent : UiEvent {
    data class BoatNameChanged(val value: String) : CaptainBoatUiEvent

    data class BoatMakeChanged(val value: String) : CaptainBoatUiEvent

    data class BoatModelChanged(val value: String) : CaptainBoatUiEvent

    data class BoatYearChanged(val value: String) : CaptainBoatUiEvent

    data class BoatSizeChanged(val value: String) : CaptainBoatUiEvent

    data class BoatCapacityChanged(val value: String) : CaptainBoatUiEvent

    data class LoadInitial(val data: GetCaptainBoatResponse?) : CaptainBoatUiEvent

    data object ClearError : CaptainBoatUiEvent

    data object SaveProfile : CaptainBoatUiEvent

    data object RegistrationHandled : CaptainBoatUiEvent
}

sealed interface CaptainBoatUiEffect : UiEffect {
    data object NoOpEffect : CaptainBoatUiEffect
}

class CaptainBoatViewModel(
    private val saveCaptainBoatUseCase: SaveCaptainBoatUseCase,
    private val sharedPrefManager: SharedPrefManager,
    private val userSessionStore: UserSessionStore,
) : BaseViewModel<CaptainBoatUiState, CaptainBoatUiEvent, CaptainBoatUiEffect>(CaptainBoatUiState()) {
    override fun onEvent(event: CaptainBoatUiEvent) {
        when (event) {
            is CaptainBoatUiEvent.BoatNameChanged -> updateState { copy(boatName = event.value) }
            is CaptainBoatUiEvent.BoatMakeChanged -> updateState { copy(boatMake = event.value) }
            is CaptainBoatUiEvent.BoatModelChanged -> updateState { copy(boatModel = event.value) }
            is CaptainBoatUiEvent.BoatYearChanged -> updateState { copy(boatYear = event.value) }
            is CaptainBoatUiEvent.BoatSizeChanged -> updateState { copy(boatSize = event.value) }
            is CaptainBoatUiEvent.BoatCapacityChanged -> updateState { copy(boatCapacity = event.value) }
            is CaptainBoatUiEvent.LoadInitial -> loadInitialData(event.data)
            CaptainBoatUiEvent.ClearError -> onClearError()
            CaptainBoatUiEvent.SaveProfile -> saveProfile()
            CaptainBoatUiEvent.RegistrationHandled -> {
                updateState { copy(isLoading = false, isButtonClicked = false) }
            }
        }
    }

    fun onClearError() {
        updateState { copy(isError = false, errorMessage = null) }
    }

    fun saveProfile() {
        updateState { copy(isButtonClicked = true) }
        val s = currentState
        val request =
            SaveCaptainBoatRequest(
                UserId = userSessionStore.currentUserId(),
                Name = s.boatName,
                Make = s.boatMake,
                Model = s.boatModel,
                Year = s.boatYear.toIntOrNull() ?: 0,
                Size = s.boatSize.toIntOrNull() ?: 0,
                Capacity = s.boatCapacity.toIntOrNull() ?: 0,
            )

        viewModelScope.launch {
            updateState { copy(isLoading = true, registrationState = NetworkResponse.Loading()) }
            when (val result = saveCaptainBoatUseCase(request).toResource()) {
                is Resource.Success -> {
                    updateState {
                        copy(
                            registrationState = NetworkResponse.Success(result.data),
                            isLoading = false,
                        )
                    }
                    saveLoginData(0)
                }

                is Resource.Error -> {
                    updateState {
                        copy(
                            isLoading = false,
                            isError = true,
                            errorMessage = result.error.toMessage(),
                            registrationState = NetworkResponse.Error(result.error),
                        )
                    }
                }

                Resource.Loading -> {
                    updateState { copy(registrationState = NetworkResponse.Loading(), isLoading = true) }
                }
            }
        }
    }

    fun onRegistrationHandled() {
        onEvent(CaptainBoatUiEvent.RegistrationHandled)
    }

    fun loadInitialData(data: GetCaptainBoatResponse?) {
        updateState {
            copy(
                boatName = data?.obj?.Name.orEmpty(),
                boatMake = data?.obj?.Make.orEmpty(),
                boatModel = data?.obj?.Model.orEmpty(),
                boatYear = data?.obj?.Year.toString().orEmpty(),
                boatSize = data?.obj?.Size.toString().orEmpty(),
                boatCapacity = data?.obj?.Capacity.toString().orEmpty(),
            )
        }
    }

    private fun saveLoginData(userData: Int) {
        sharedPrefManager.saveMissingStep(userData)
    }
}
