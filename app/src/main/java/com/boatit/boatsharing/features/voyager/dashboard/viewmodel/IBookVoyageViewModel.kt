package com.boatit.boatsharing.features.voyager.dashboard.viewmodel

import com.boatit.boatsharing.features.voyager.dashboard.model.BookVoyageUiEffect
import com.boatit.boatsharing.features.voyager.dashboard.model.BookVoyageUiEvent
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface IBookVoyageViewModel {
    val uiState: StateFlow<BookVoyageUiState>
    val uiEffects: SharedFlow<BookVoyageUiEffect>

    fun onEvent(event: BookVoyageUiEvent)
}
