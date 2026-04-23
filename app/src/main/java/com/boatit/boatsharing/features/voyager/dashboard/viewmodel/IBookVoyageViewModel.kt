package com.boatit.boatsharing.features.voyager.dashboard.viewmodel

import com.boatit.boatsharing.features.voyager.dashboard.model.BookVoyageUiEffect
import com.boatit.boatsharing.features.voyager.dashboard.model.BookVoyageUiEvent
import com.boatit.boatsharing.features.voyager.dashboard.model.BookVoyageUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface IBookVoyageViewModel {
    val uiState: StateFlow<BookVoyageUiState>
    val uiEffect: Flow<BookVoyageUiEffect>

    fun onEvent(event: BookVoyageUiEvent)
}
