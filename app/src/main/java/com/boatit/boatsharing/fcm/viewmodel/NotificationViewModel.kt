package com.boatit.boatsharing.ui.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.fcm.MyFirebaseMessagingService
import com.boatit.boatsharing.ui.voyager.dashbaord.model.VoyageNotification
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NotificationViewModel : ViewModel() {
    private val _notificationState = MutableStateFlow<VoyageNotification?>(null)
    val notificationState = _notificationState.asStateFlow()

    init {
        observeNotifications()
    }

    private fun observeNotifications() {
        viewModelScope.launch {
            MyFirebaseMessagingService.notificationFlow.collectLatest { newNotification ->
                _notificationState.value = null
                _notificationState.value = newNotification
            }
        }
    }
}
