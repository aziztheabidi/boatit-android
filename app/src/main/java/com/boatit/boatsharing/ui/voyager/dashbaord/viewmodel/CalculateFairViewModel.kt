package com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.network.networkreposne.NetworkResponse
import com.boatit.boatsharing.ui.signup.captain.model.GetCaptainProfileResponse
import com.boatit.boatsharing.ui.signup.captain.repository.GetCaptainProfileRepository
import com.boatit.boatsharing.ui.voyager.dashbaord.model.CalculateFair
import com.boatit.boatsharing.ui.voyager.dashbaord.repository.CalculateFairRepository
import com.boatit.boatsharing.utils.AppConstants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import android.text.format.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class CreateVoyageUiState(
    val dob: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val travelNowSwitchState: Boolean = false,
    val spendTimeSwitchState: Boolean = false,
    val isLoading: Boolean = false,
    val isNetworkError: Boolean = false,
    val errorMessage: String? = null,
    val showDatePicker: Boolean = false,
    val showStartTimePicker: Boolean = false,
    val showEndTimePicker: Boolean = false,
    val isButtonEnabled: Boolean = false
)

class CalculateFairViewModel(private val repository: CalculateFairRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateVoyageUiState())
    val uiState: StateFlow<CreateVoyageUiState> = _uiState

    private val _registrationState = MutableStateFlow<NetworkResponse<CalculateFair>>(NetworkResponse.Loading())
    val registrationState: StateFlow<NetworkResponse<CalculateFair>> = _registrationState

    fun onDobChange(newDob: String) {
        _uiState.update { it.copy(dob = newDob) }
    }

    fun onStartTimeChange(newStartTime: String) {
        _uiState.update { it.copy(startTime = newStartTime) }
    }

    fun onEndTimeChange(newEndTime: String) {
        _uiState.update { it.copy(endTime = newEndTime) }
    }

    fun onTravelNowSwitchChange(isChecked: Boolean) {
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val currentTime = DateFormat.format("hh:mm:ss", Date(System.currentTimeMillis())).toString()

        _uiState.update {
            it.copy(
                travelNowSwitchState = isChecked,
                dob = if (isChecked) currentDate else it.dob,
                startTime = if (isChecked) currentTime else it.startTime
            )
        }
    }

    fun onSpendTimeSwitchChange(isChecked: Boolean) {
        _uiState.update { it.copy(spendTimeSwitchState = isChecked) }
    }

    fun onShowDatePicker(show: Boolean) {
        _uiState.update { it.copy(showDatePicker = show) }
    }

    fun onShowStartTimePicker(show: Boolean) {
        _uiState.update { it.copy(showStartTimePicker = show) }
    }

    fun onShowEndTimePicker(show: Boolean) {
        _uiState.update { it.copy(showEndTimePicker = show) }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null, isNetworkError = false) }
    }

    fun calculateFare(fromDockId: String, toDockId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, isNetworkError = false, errorMessage = null) }
            val duration = if (_uiState.value.spendTimeSwitchState) _uiState.value.endTime else "0"

            val result = repository.CalculateFairRepoFunc(fromDockId, toDockId, duration)
            result.onSuccess { response ->
                _registrationState.value = NetworkResponse.Success(response)
                _uiState.update { it.copy(isLoading = false, isButtonEnabled = true) }

                AppConstants.Per_Hour_Rate = response.obj?.PerHourRate ?: 0.0
                AppConstants.Estimated_Cost = response.obj?.TotalFair ?: 0.0
                AppConstants.Total_Cost = response.obj?.TotalFair ?: 0.0

            }.onFailure { error ->
                _registrationState.value = NetworkResponse.Error(error.message ?: "Failed to calculate fare")
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isNetworkError = true,
                        errorMessage = error.message ?: "Network error, please try again."
                    )
                }
            }
        }
    }
}



