package com.boatit.boatsharing.data.network.di

import com.boatit.boatsharing.data.local.session.ClearSessionUseCase
import com.boatit.boatsharing.data.local.session.SessionController
import com.boatit.boatsharing.data.network.session.UnauthorizedSessionHandler
import io.ktor.client.HttpClient
import org.koin.dsl.module

/**
 * Ktor HTTP client and session/network bridge.
 *
 * Registration order matters: [UnauthorizedSessionHandler] must be declared before [HttpClient]
 * so Koin can construct the client with a handler that captures a lazy [HttpClient] reference.
 */
val networkModule =
    module {
        single { UnauthorizedSessionHandler(get()) { get<HttpClient>() } }
        single { createKtorClient(get(), get()) }
        single { ClearSessionUseCase(get()) { get<HttpClient>() } }
        single { SessionController(get()) }
    }
