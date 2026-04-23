package com.boatit.boatsharing.features.voyager.dashboard.model

import com.boatit.boatsharing.core.presentation.UiEffect
import com.boatit.boatsharing.core.presentation.UiEvent
import com.boatit.boatsharing.core.presentation.UiState
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse

data class BookVoyageUiState(
    val isSubmitting: Boolean = false,
    val showErrorDialog: Boolean = false,
    val errorMessage: String = "",
    val bookRequest: NetworkResponse<BookVoyageResponse> = NetworkResponse.Loading(),
) : UiState

sealed interface BookVoyageUiEvent : UiEvent {
    data class SubmitBookVoyage(val request: BookVoyageRequest) : BookVoyageUiEvent

    data object ResetRequestState : BookVoyageUiEvent

    data object DismissErrorDialog : BookVoyageUiEvent
}

sealed interface BookVoyageUiEffect : UiEffect {
    data class BookedSuccess(val message: String?, val voyageId: String?) : BookVoyageUiEffect

    data class BookedError(val message: String) : BookVoyageUiEffect
}

typealias BookVoyageContractState = BookVoyageUiState
