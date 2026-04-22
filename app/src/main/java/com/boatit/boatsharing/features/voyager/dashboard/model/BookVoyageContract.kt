package com.boatit.boatsharing.features.voyager.dashboard.model

import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.BookVoyageUiState

sealed interface BookVoyageUiEvent {
    data class SubmitBookVoyage(val request: BookVoyageRequest) : BookVoyageUiEvent

    data object ResetRequestState : BookVoyageUiEvent

    data object DismissErrorDialog : BookVoyageUiEvent
}

sealed interface BookVoyageUiEffect {
    data class BookedSuccess(val message: String?, val voyageId: String?) : BookVoyageUiEffect

    data class BookedError(val message: String) : BookVoyageUiEffect
}

typealias BookVoyageContractState = BookVoyageUiState
