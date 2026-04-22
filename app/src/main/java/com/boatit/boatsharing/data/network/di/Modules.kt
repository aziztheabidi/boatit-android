package com.boatit.boatsharing.data.network.di

import org.koin.dsl.module

val Modules =
    module {
        includes(
            coreModule,
            authModule,
            voyagerModule,
            captainModule,
            businessModule,
            chatUserrolesModule,
        )
    }
