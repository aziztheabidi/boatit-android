package com.boatit.boatsharing.data.network.di

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/** Google Play Services and other non-Firebase platform bindings. */
val platformServicesModule =
    module {
        single<FusedLocationProviderClient> {
            LocationServices.getFusedLocationProviderClient(androidContext())
        }
    }
