package com.boatit.boatsharing.fcm.viewmodel

import androidx.lifecycle.ViewModel
import com.boatit.boatsharing.fcm.FirebaseNotificationService

class SendNotificationViewModel(private val notificationService: FirebaseNotificationService) : com.boatit.boatsharing.core.presentation.LegacyMviViewModel() {
    fun sendNotification(
        receiverId: String,
        message: String,
    ) {
        notificationService.sendPushNotification(receiverId, message)
    }
}
