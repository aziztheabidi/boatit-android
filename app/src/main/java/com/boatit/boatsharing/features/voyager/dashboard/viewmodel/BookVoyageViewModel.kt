package com.boatit.boatsharing.features.voyager.dashboard.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.core.presentation.BaseViewModel
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.features.voyager.dashboard.domain.usecase.BookVoyageUseCase
import com.boatit.boatsharing.features.voyager.dashboard.model.BookVoyageRequest
import com.boatit.boatsharing.features.voyager.dashboard.model.BookVoyageUiEffect
import com.boatit.boatsharing.features.voyager.dashboard.model.BookVoyageUiEvent
import com.boatit.boatsharing.features.voyager.dashboard.model.BookVoyageUiState
import kotlinx.coroutines.launch

class BookVoyageViewModel(
    private val bookVoyageUseCase: BookVoyageUseCase,
) : BaseViewModel<BookVoyageUiState, BookVoyageUiEvent, BookVoyageUiEffect>(BookVoyageUiState()),
    IBookVoyageViewModel {
    override fun onEvent(event: BookVoyageUiEvent) {
        when (event) {
            is BookVoyageUiEvent.SubmitBookVoyage -> bookVoyageVMfunc(event.request)
            BookVoyageUiEvent.ResetRequestState -> resetNearbyPlaces()
            BookVoyageUiEvent.DismissErrorDialog -> {
                updateState { copy(showErrorDialog = false, errorMessage = "") }
            }
        }
    }

    fun bookVoyageVMfunc(profile: BookVoyageRequest) =
        viewModelScope.launch {
            updateState {
                copy(
                    isSubmitting = true,
                    showErrorDialog = false,
                    errorMessage = "",
                    bookRequest = NetworkResponse.Loading(),
                )
            }
            when (val result = bookVoyageUseCase(profile).toResource()) {
                is Resource.Success -> {
                    val placesResponse = result.data
                    updateState {
                        copy(
                            isSubmitting = false,
                            bookRequest = NetworkResponse.Success(placesResponse),
                        )
                    }
                    emitEffect(
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
                    updateState {
                        copy(
                            isSubmitting = false,
                            showErrorDialog = true,
                            errorMessage = errorMessage,
                            bookRequest = NetworkResponse.Error(result.error),
                        )
                    }
                    emitEffect(BookVoyageUiEffect.BookedError(errorMessage))
                }

                Resource.Loading -> {
                    updateState {
                        copy(
                            isSubmitting = true,
                            bookRequest = NetworkResponse.Loading(),
                        )
                    }
                }
            }
        }

    fun resetNearbyPlaces() {
        updateState {
            copy(
                isSubmitting = false,
                bookRequest = NetworkResponse.Loading(),
            )
        }
    }
}
