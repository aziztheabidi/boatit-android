package com.boatit.boatsharing.features.login.viewmodel

import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.core.presentation.BaseViewModel
import com.boatit.boatsharing.core.presentation.UiEffect
import com.boatit.boatsharing.core.presentation.UiEvent
import com.boatit.boatsharing.core.presentation.UiState
import com.boatit.boatsharing.fcm.MyFirebaseMessagingService
import com.boatit.boatsharing.features.voyager.dashboard.model.VoyageNotification
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class NotificationUiState(
    val notification: VoyageNotification? = null,
) : UiState

sealed interface NotificationUiEvent : UiEvent {
    data object None : NotificationUiEvent
}

sealed interface NotificationUiEffect : UiEffect {
    data object NoOpEffect : NotificationUiEffect
}

class NotificationViewModel :
    BaseViewModel<NotificationUiState, NotificationUiEvent, NotificationUiEffect>(
        NotificationUiState(),
    ) {
    init {
        observeNotifications()
    }

    override fun onEvent(event: NotificationUiEvent) {
        when (event) {
            NotificationUiEvent.None -> Unit
        }
    }

    private fun observeNotifications() {
        viewModelScope.launch {
            MyFirebaseMessagingService.notificationFlow.collectLatest { newNotification ->
                updateState { copy(notification = null) }
                updateState { copy(notification = newNotification) }
            }
        }
    }
}
