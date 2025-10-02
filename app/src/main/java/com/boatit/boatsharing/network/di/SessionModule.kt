package com.boatit.boatsharing.network.di

import com.boatit.boatsharing.utils.session.SessionManager
import com.boatit.boatsharing.utils.session.TokenRefreshService
import com.boatit.boatsharing.utils.prefmanager.TokenProvider
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Dedicated module for session management dependencies
 */
val sessionModule = module {
    
    // Token refresh service
    single { 
        TokenRefreshService(
            httpClient = get(),
            tokenProvider = get()
        ) 
    }
    
    // Session manager
    single { 
        SessionManager(
            tokenProvider = get(),
            tokenRefreshService = get()
        ) 
    }
}

