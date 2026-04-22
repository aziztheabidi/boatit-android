package com.boatit.boatsharing.features.voyager.dashboard.viewmodel

import android.text.format.DateFormat
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.features.voyager.dashboard.domain.usecase.CalculateVoyageFareUseCase
import com.boatit.boatsharing.features.voyager.dashboard.model.CalculateFair
import com.boatit.boatsharing.features.voyager.dashboard.model.CreateVoyageUiEffect
import com.boatit.boatsharing.features.voyager.dashboard.model.CreateVoyageUiEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
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
    val isButtonEnabled: Boolean = false,
)

class CalculateFairViewModel(
    private val calculateVoyageFareUseCase: CalculateVoyageFareUseCase,
    private val draftStore: CreateVoyageDraftStore,
) : com.boatit.boatsharing.core.presentation.LegacyMviViewModel(), ICreateVoyageViewModel {
    private val _uiState = MutableStateFlow(CreateVoyageUiState())
    override val uiState: StateFlow<CreateVoyageUiState> = _uiState

    private val _uiEffects = MutableSharedFlow<CreateVoyageUiEffect>(extraBufferCapacity = 1)
    override val uiEffects: SharedFlow<CreateVoyageUiEffect> = _uiEffects

    private val _registrationState = MutableStateFlow<NetworkResponse<CalculateFair>>(NetworkResponse.Loading())
    val registrationState: StateFlow<NetworkResponse<CalculateFair>> = _registrationState

    override fun onEvent(event: CreateVoyageUiEvent) {
        when (event) {
            is CreateVoyageUiEvent.DobChanged -> onDobChange(event.value)
            is CreateVoyageUiEvent.StartTimeChanged -> onStartTimeChange(event.value)
            is CreateVoyageUiEvent.EndTimeChanged -> onEndTimeChange(event.value)
            is CreateVoyageUiEvent.TravelNowToggled -> onTravelNowSwitchChange(event.enabled)
            is CreateVoyageUiEvent.SpendTimeToggled -> onSpendTimeSwitchChange(event.enabled)
            is CreateVoyageUiEvent.ShowDatePicker -> onShowDatePicker(event.show)
            is CreateVoyageUiEvent.ShowStartTimePicker -> onShowStartTimePicker(event.show)
            is CreateVoyageUiEvent.ShowEndTimePicker -> onShowEndTimePicker(event.show)
            CreateVoyageUiEvent.ClearError -> clearError()
            CreateVoyageUiEvent.CalculateFare -> calculateFare()
            CreateVoyageUiEvent.ResetRequestState -> resetNearbyPlaces()
        }
    }

    init {
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val currentTime = DateFormat.format("HH:mm:ss", Date(System.currentTimeMillis())).toString()

        _uiState.update {
            it.copy(
                dob = currentDate,
                startTime = currentTime,
            )
        }
    }

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
                startTime = if (isChecked) currentTime else it.startTime,
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
            val durationHours =
                if (_uiState.value.spendTimeSwitchState) {
                    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                    val start = timeFormat.parse(_uiState.value.startTime)
                    val end = timeFormat.parse(_uiState.value.endTime)
                    val durationMillis = end.time - start.time
                    val durationInMinutes = TimeUnit.MILLISECONDS.toMinutes(durationMillis)
                    durationInMinutes.toDouble() / 60.0
                } else {
                    0.0
                }
            val duration = durationHours.toString()
            val draft = draftStore.state.value
            val result =
                calculateVoyageFareUseCase(
                    durationInHours = duration,
                    fromDockId = draft.pickupDockId,
                    toDockId = draft.dropOffDockId,
                    voyageCategoryId = draft.voyageCategoryId,
                    noOfVoyagers = draft.noOfVoyagers,
                )
            when (val resource = result.toResource()) {
                is Resource.Success -> {
                    val response = resource.data
                    _registrationState.value = NetworkResponse.Success(response)
                    _uiState.update { it.copy(isLoading = false, isButtonEnabled = true) }
                    _uiEffects.tryEmit(CreateVoyageUiEffect.NavigateToRateCalculation)

                    draftStore.setDraft(
                        CreateVoyageDraftState(
                            initialized = true,
                            voyagerUserId = draftStore.state.value.voyagerUserId,
                            eventName = draftStore.state.value.eventName,
                            voyageCategoryId = draftStore.state.value.voyageCategoryId,
                            pickupDockId = draftStore.state.value.pickupDockId,
                            pickupDockName = draftStore.state.value.pickupDockName,
                            dropOffDockId = draftStore.state.value.dropOffDockId,
                            dropOffDockName = draftStore.state.value.dropOffDockName,
                            noOfVoyagers = draftStore.state.value.noOfVoyagers,
                            isImmediately = _uiState.value.travelNowSwitchState,
                            splitPaymentEnabled = draftStore.state.value.splitPaymentEnabled,
                            bookingDate = _uiState.value.dob,
                            startTime = _uiState.value.startTime,
                            isStayOnWater = _uiState.value.spendTimeSwitchState,
                            endTime = _uiState.value.endTime,
                            perHourRate = response.obj?.PerHourRate ?: 0.0,
                            durationInHours = durationHours,
                            estimatedCost = response.obj?.TotalFair ?: 0.0,
                            totalCostAmount = response.obj?.TotalFair ?: 0.0,
                            sponsorEntries = draftStore.state.value.sponsorEntries,
                        ),
                    )
                }

                is Resource.Error -> {
                    val message = resource.error.toMessage()
                    Log.e("calculate_fare_error", message)
                    _registrationState.value = NetworkResponse.Error(resource.error)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isNetworkError = true,
                            errorMessage = message,
                        )
                    }
                }

                Resource.Loading -> {
                    _registrationState.value = NetworkResponse.Loading()
                    _uiState.update { it.copy(isLoading = true) }
                }
            }
        }
    }
}
