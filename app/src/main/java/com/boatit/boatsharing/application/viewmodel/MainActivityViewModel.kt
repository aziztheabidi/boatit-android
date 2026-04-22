package com.boatit.boatsharing.application.viewmodel

import android.os.Build
import com.boatit.boatsharing.core.presentation.BaseViewModel
import com.boatit.boatsharing.core.presentation.UiEffect
import com.boatit.boatsharing.core.presentation.UiEvent
import com.boatit.boatsharing.core.presentation.UiState

class MainActivityViewModel :
    BaseViewModel<MainActivityUiState, MainActivityUiEvent, MainActivityUiEffect>(MainActivityUiState()) {
    override fun onEvent(event: MainActivityUiEvent) {
        when (event) {
            is MainActivityUiEvent.PermissionResult -> updateState { copy(permissionGranted = event.granted) }
        }
    }

    fun shouldRequestNotificationPermission(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
}

data class MainActivityUiState(val permissionGranted: Boolean? = null) : UiState

sealed interface MainActivityUiEvent : UiEvent {
    data class PermissionResult(val granted: Boolean) : MainActivityUiEvent
}

sealed interface MainActivityUiEffect : UiEffect
