package com.boatit.boatsharing.data.network.di

import org.koin.dsl.module

/**
 * Application-wide Koin modules. Order in [includes] is load order only; dependency
 * resolution is still graph-based. Keep [networkModule] after [sessionPreferencesModule]
 * so session types exist before the HTTP client stack is built.
 */
val Modules =
    module {
        includes(
            firebaseSdkModule,
            platformServicesModule,
            sessionPreferencesModule,
            networkModule,
            messagingPresentationModule,
            authModule,
            voyagerModule,
            captainModule,
            businessModule,
            chatUserrolesModule,
        )
    }
