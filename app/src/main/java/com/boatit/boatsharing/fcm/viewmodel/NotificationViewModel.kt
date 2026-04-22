package com.boatit.boatsharing.features.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.fcm.MyFirebaseMessagingService
import com.boatit.boatsharing.features.voyager.dashboard.model.VoyageNotification
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NotificationViewModel : com.boatit.boatsharing.core.presentation.LegacyMviViewModel() {
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
