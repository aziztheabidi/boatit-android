package com.boatit.boatsharing.fcm.viewmodel

import com.boatit.boatsharing.core.presentation.BaseViewModel
import com.boatit.boatsharing.core.presentation.UiEffect
import com.boatit.boatsharing.core.presentation.UiEvent
import com.boatit.boatsharing.core.presentation.UiState
import com.boatit.boatsharing.fcm.FirebaseNotificationService

data class SendNotificationUiState(
    val idle: Boolean = true,
) : UiState

sealed interface SendNotificationUiEvent : UiEvent {
    data class Send(
        val receiverId: String,
        val message: String,
    ) : SendNotificationUiEvent
}

sealed interface SendNotificationUiEffect : UiEffect {
    data object NoOpEffect : SendNotificationUiEffect
}

class SendNotificationViewModel(
    private val notificationService: FirebaseNotificationService,
) : BaseViewModel<SendNotificationUiState, SendNotificationUiEvent, SendNotificationUiEffect>(
        SendNotificationUiState(),
    ) {
    override fun onEvent(event: SendNotificationUiEvent) {
        when (event) {
            is SendNotificationUiEvent.Send -> {
                notificationService.sendPushNotification(event.receiverId, event.message)
            }
        }
    }

    fun sendNotification(
        receiverId: String,
        message: String,
    ) {
        onEvent(SendNotificationUiEvent.Send(receiverId, message))
    }
}
