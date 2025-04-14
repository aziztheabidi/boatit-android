package com.boatit.boatsharing.fcm.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.boatit.boatsharing.fcm.FirebaseNotificationService

class SendNotificationViewModel(private val notificationService: FirebaseNotificationService) : ViewModel()  {

    fun sendNotification(receiverId: String, message: String, context: Context) {
        notificationService.sendPushNotification(receiverId, message, context)
    }
}