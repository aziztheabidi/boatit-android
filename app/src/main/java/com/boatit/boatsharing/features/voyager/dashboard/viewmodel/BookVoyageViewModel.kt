package com.boatit.boatsharing.features.voyager.dashboard.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.features.voyager.dashboard.domain.usecase.BookVoyageUseCase
import com.boatit.boatsharing.features.voyager.dashboard.model.BookVoyageRequest
import com.boatit.boatsharing.features.voyager.dashboard.model.BookVoyageResponse
import com.boatit.boatsharing.features.voyager.dashboard.model.BookVoyageUiEffect
import com.boatit.boatsharing.features.voyager.dashboard.model.BookVoyageUiEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class BookVoyageUiState(
    val isSubmitting: Boolean = false,
    val showErrorDialog: Boolean = false,
    val errorMessage: String = "",
)

class BookVoyageViewModel(
    private val bookVoyageUseCase: BookVoyageUseCase,
) : com.boatit.boatsharing.core.presentation.LegacyMviViewModel(), IBookVoyageViewModel {
    private val _uiState = MutableStateFlow(BookVoyageUiState())
    override val uiState: StateFlow<BookVoyageUiState> = _uiState.asStateFlow()

    private val _uiEffects = MutableSharedFlow<BookVoyageUiEffect>(extraBufferCapacity = 1)
    override val uiEffects: SharedFlow<BookVoyageUiEffect> = _uiEffects

    private val _nearbyPlaces = MutableStateFlow<NetworkResponse<BookVoyageResponse>>(NetworkResponse.Loading())
    val nearbyPlaces: StateFlow<NetworkResponse<BookVoyageResponse>> = _nearbyPlaces.asStateFlow()

    override fun onEvent(event: BookVoyageUiEvent) {
        when (event) {
            is BookVoyageUiEvent.SubmitBookVoyage -> bookVoyageVMfunc(event.request)
            BookVoyageUiEvent.ResetRequestState -> resetNearbyPlaces()
            BookVoyageUiEvent.DismissErrorDialog -> {
                _uiState.value = _uiState.value.copy(showErrorDialog = false, errorMessage = "")
            }
        }
    }

    fun bookVoyageVMfunc(profile: BookVoyageRequest) =
        viewModelScope.launch {
            _uiState.value =
                _uiState.value.copy(
                    isSubmitting = true,
                    showErrorDialog = false,
                    errorMessage = "",
                )
            _nearbyPlaces.value = NetworkResponse.Loading()
            when (val result = bookVoyageUseCase(profile).toResource()) {
                is Resource.Success -> {
                    val placesResponse = result.data
                    _nearbyPlaces.value = NetworkResponse.Success(placesResponse)
                    _uiState.value = _uiState.value.copy(isSubmitting = false)
                    _uiEffects.tryEmit(
                        BookVoyageUiEffect.BookedSuccess(
                            message = placesResponse.Message,
                            voyageId = placesResponse.obj,
                        ),
                    )
                }

                is Resource.Error -> {
                    val errorMessage = result.error.toMessage()
                    runCatching {
                        Log.e("viewModel", "Error fetching places: $errorMessage")
                    }
                    _nearbyPlaces.value = NetworkResponse.Error(result.error)
                    _uiState.value =
                        _uiState.value.copy(
                            isSubmitting = false,
                            showErrorDialog = true,
                            errorMessage = errorMessage,
                        )
                    _uiEffects.tryEmit(BookVoyageUiEffect.BookedError(errorMessage))
                }

                Resource.Loading -> {
                    _nearbyPlaces.value = NetworkResponse.Loading()
                    _uiState.value = _uiState.value.copy(isSubmitting = true)
                }
            }
        }

    fun resetNearbyPlaces() {
        _nearbyPlaces.value = NetworkResponse.Loading()
        _uiState.value = _uiState.value.copy(isSubmitting = false)
    }
}
