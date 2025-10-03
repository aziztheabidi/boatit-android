package com.boatit.boatsharing.ui.voyager.dashboard.viewmodel

import android.text.format.DateFormat
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.ui.voyager.dashboard.model.CalculateFair
import com.boatit.boatsharing.ui.voyager.dashboard.repository.CalculateFairRepository
import com.boatit.boatsharing.utils.AppConstants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalTime
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

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

    init {
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val currentTime = DateFormat.format("HH:mm:ss", Date(System.currentTimeMillis())).toString()

        AppConstants.Event_Time = currentTime
        AppConstants.Event_Date = currentDate

        _uiState.update {
            it.copy(
                dob = currentDate,
                startTime = currentTime
            )
        }
    }

    fun onDobChange(newDob: String) {
        AppConstants.Event_Date = newDob
        _uiState.update { it.copy(dob = newDob) }
    }

    fun onStartTimeChange(newStartTime: String) {
        AppConstants.Event_Time = newStartTime
        _uiState.update { it.copy(startTime = newStartTime) }
    }

    fun onEndTimeChange(newEndTime: String) {
        AppConstants.Event_Time_End = newEndTime
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

    fun resetNearbyPlaces() {
        _registrationState.value = NetworkResponse.Loading()
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

    fun calculateFare() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, isNetworkError = false, errorMessage = null) }
            val duration = if (_uiState.value.spendTimeSwitchState) {
                val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                val start = timeFormat.parse(_uiState.value.startTime)
                val end = timeFormat.parse(_uiState.value.endTime)
                val durationMillis = end.time - start.time
                val durationInMinutes = TimeUnit.MILLISECONDS.toMinutes(durationMillis)
                AppConstants.No_of_Hour = durationInMinutes.toDouble() / 60f
                (durationInMinutes.toFloat() / 60f).toString()
            }else {
                AppConstants.No_of_Hour = 0.0
                "0"
            }
            val result = repository.CalculateFairRepoFunc(duration)
            result.onSuccess { response ->
                _registrationState.value = NetworkResponse.Success(response)
                _uiState.update { it.copy(isLoading = false, isButtonEnabled = true) }
                AppConstants.Per_Hour_Rate = response.obj?.PerHourRate ?: 0.0
                AppConstants.Estimated_Cost = response.obj?.TotalFair ?: 0.0
                AppConstants.Total_Cost = response.obj?.TotalFair ?: 0.0

            }.onFailure { error ->
                Log.e("calculate_fare_error",error.toString())
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



