package com.boatit.boatsharing.fcm

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.boatit.boatsharing.R
import com.boatit.boatsharing.application.MainActivity
import com.boatit.boatsharing.ui.login.viewmodel.NotificationViewModel
import com.boatit.boatsharing.ui.voyager.dashbaord.model.VoyageNotification
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MyFirebaseMessagingService : FirebaseMessagingService(){

    companion object {
        private val _notificationFlow = MutableSharedFlow<VoyageNotification>(extraBufferCapacity = 1, replay = 1)
        val notificationFlow = _notificationFlow.asSharedFlow()
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        remoteMessage.data.isNotEmpty().let {
            Log.d("FCM", "Message Data Payload: ${remoteMessage.data}")
            val jsonData = Gson().toJson(remoteMessage.data)
            val voyageData = Gson().fromJson(jsonData, VoyageNotification::class.java)
            if (isAppInForeground()) {
                _notificationFlow.tryEmit(voyageData)
            } else {
                showNotification(voyageData)
            }
            showNotification(voyageData)
        }
    }

    @SuppressLint("MissingPermission")
    private fun showNotification(voyageData: VoyageNotification) {
        val channelId = "voyage_notifications"
        val channelName = "Voyage Updates"

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        // Intent to open MainActivity on tap
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.message_icon)
            .setContentTitle("New Voyage Update!")
            .setContentText("Voyage for ${voyageData.Name} - ${voyageData.PickupDock} to ${voyageData.DropOffDock}")
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        NotificationManagerCompat.from(this).notify(1, notification)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        saveTokenToFirebase(token)
    }

    private fun saveTokenToFirebase(token: String) {}

    private fun isAppInForeground(): Boolean {
        return applicationContext.packageManager.getLaunchIntentForPackage(packageName) != null
    }
}
