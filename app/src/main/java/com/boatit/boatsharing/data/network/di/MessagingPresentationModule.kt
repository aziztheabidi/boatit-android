package com.boatit.boatsharing.data.network.di

import com.boatit.boatsharing.fcm.viewmodel.SendNotificationViewModel
import com.boatit.boatsharing.features.login.viewmodel.NotificationViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/** FCM-related ViewModels (presentation only; service lives in [firebaseSdkModule]). */
val messagingPresentationModule =
    module {
        viewModel { NotificationViewModel() }
        viewModel { SendNotificationViewModel(get()) }
    }
