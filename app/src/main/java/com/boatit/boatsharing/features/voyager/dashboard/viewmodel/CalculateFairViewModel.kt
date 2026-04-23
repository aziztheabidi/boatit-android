package com.boatit.boatsharing.features.voyager.dashboard.viewmodel

import android.text.format.DateFormat
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.core.presentation.BaseViewModel
import com.boatit.boatsharing.core.presentation.UiState
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.features.voyager.dashboard.domain.usecase.CalculateVoyageFareUseCase
import com.boatit.boatsharing.features.voyager.dashboard.model.CalculateFair
import com.boatit.boatsharing.features.voyager.dashboard.model.CreateVoyageUiEffect
import com.boatit.boatsharing.features.voyager.dashboard.model.CreateVoyageUiEvent
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
    val fareResult: NetworkResponse<CalculateFair> = NetworkResponse.Loading(),
) : UiState

class CalculateFairViewModel(
    private val calculateVoyageFareUseCase: CalculateVoyageFareUseCase,
    private val draftStore: CreateVoyageDraftStore,
) : BaseViewModel<CreateVoyageUiState, CreateVoyageUiEvent, CreateVoyageUiEffect>(CreateVoyageUiState()),
    ICreateVoyageViewModel {
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

        updateState {
            copy(
                dob = currentDate,
                startTime = currentTime,
            )
        }
    }

    fun onDobChange(newDob: String) {
        updateState { copy(dob = newDob) }
    }

    fun onStartTimeChange(newStartTime: String) {
        updateState { copy(startTime = newStartTime) }
    }

    fun onEndTimeChange(newEndTime: String) {
        updateState { copy(endTime = newEndTime) }
    }

    fun onTravelNowSwitchChange(isChecked: Boolean) {
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val currentTime = DateFormat.format("hh:mm:ss", Date(System.currentTimeMillis())).toString()

        updateState {
            copy(
                travelNowSwitchState = isChecked,
                dob = if (isChecked) currentDate else dob,
                startTime = if (isChecked) currentTime else startTime,
            )
        }
    }

    fun resetNearbyPlaces() {
        updateState { copy(fareResult = NetworkResponse.Loading()) }
    }

    fun onSpendTimeSwitchChange(isChecked: Boolean) {
        updateState { copy(spendTimeSwitchState = isChecked) }
    }

    fun onShowDatePicker(show: Boolean) {
        updateState { copy(showDatePicker = show) }
    }

    fun onShowStartTimePicker(show: Boolean) {
        updateState { copy(showStartTimePicker = show) }
    }

    fun onShowEndTimePicker(show: Boolean) {
        updateState { copy(showEndTimePicker = show) }
    }

    fun clearError() {
        updateState { copy(errorMessage = null, isNetworkError = false) }
    }

    fun calculateFare() {
        viewModelScope.launch {
            updateState { copy(isLoading = true, isNetworkError = false, errorMessage = null) }
            val durationHours =
                if (currentState.spendTimeSwitchState) {
                    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                    val start = timeFormat.parse(currentState.startTime)
                    val end = timeFormat.parse(currentState.endTime)
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
                    updateState {
                        copy(
                            isLoading = false,
                            isButtonEnabled = true,
                            fareResult = NetworkResponse.Success(response),
                        )
                    }
                    emitEffect(CreateVoyageUiEffect.NavigateToRateCalculation)

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
                            isImmediately = currentState.travelNowSwitchState,
                            splitPaymentEnabled = draftStore.state.value.splitPaymentEnabled,
                            bookingDate = currentState.dob,
                            startTime = currentState.startTime,
                            isStayOnWater = currentState.spendTimeSwitchState,
                            endTime = currentState.endTime,
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
                    updateState {
                        copy(
                            isLoading = false,
                            isNetworkError = true,
                            errorMessage = message,
                            fareResult = NetworkResponse.Error(resource.error),
                        )
                    }
                }

                Resource.Loading -> {
                    updateState {
                        copy(
                            isLoading = true,
                            fareResult = NetworkResponse.Loading(),
                        )
                    }
                }
            }
        }
    }
}
