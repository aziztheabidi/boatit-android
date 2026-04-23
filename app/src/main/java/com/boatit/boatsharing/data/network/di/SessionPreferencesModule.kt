package com.boatit.boatsharing.data.network.di

import com.boatit.boatsharing.data.local.prefmanager.ICaptainStatusProvider
import com.boatit.boatsharing.data.local.prefmanager.IRoleProvider
import com.boatit.boatsharing.data.local.prefmanager.ITokenProvider
import com.boatit.boatsharing.data.local.prefmanager.RoleProvider
import com.boatit.boatsharing.data.local.prefmanager.SharedPrefManager
import com.boatit.boatsharing.data.local.prefmanager.StatusProvider
import com.boatit.boatsharing.data.local.prefmanager.TokenProvider
import com.boatit.boatsharing.data.local.prefmanager.UserSessionStore
import com.boatit.boatsharing.data.local.session.SessionManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/** Local preferences, role/token/captain-status providers, and session state (no HTTP client). */
val sessionPreferencesModule =
    module {
        single<ITokenProvider> { TokenProvider(androidContext()) }
        single<IRoleProvider> { RoleProvider(androidContext()) }
        single<ICaptainStatusProvider> { StatusProvider(androidContext()) }
        single { SharedPrefManager(androidContext()) }
        single { SessionManager(androidContext(), get()) }
        single { UserSessionStore(get()) }
    }
