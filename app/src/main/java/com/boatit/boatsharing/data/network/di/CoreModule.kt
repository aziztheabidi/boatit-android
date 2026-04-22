package com.boatit.boatsharing.data.network.di

import com.boatit.boatsharing.fcm.FirebaseNotificationService
import com.boatit.boatsharing.fcm.viewmodel.SendNotificationViewModel
import com.boatit.boatsharing.features.captain.dashboard.viewmodel.LocationViewModel
import com.boatit.boatsharing.features.login.viewmodel.NotificationViewModel
import com.boatit.boatsharing.features.voyager.dashboard.repository.GoogleDirectionsApi
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.TrackingLocationViewModel
import com.boatit.boatsharing.data.network.retrofit.ApiExecutor
import com.boatit.boatsharing.data.network.retrofit.AuthTokenInterceptor
import com.boatit.boatsharing.data.network.retrofit.BoatitApiService
import com.boatit.boatsharing.data.network.retrofit.UnauthorizedResponseInterceptor
import com.boatit.boatsharing.data.network.retrofit.createRetrofit
import com.boatit.boatsharing.data.network.retrofit.createRetrofitOkHttpClient
import com.boatit.boatsharing.data.network.session.UnauthorizedSessionHandler
import com.boatit.boatsharing.data.local.prefmanager.ICaptainStatusProvider
import com.boatit.boatsharing.data.local.prefmanager.IRoleProvider
import com.boatit.boatsharing.data.local.prefmanager.ITokenProvider
import com.boatit.boatsharing.data.local.prefmanager.RoleProvider
import com.boatit.boatsharing.data.local.prefmanager.SharedPrefManager
import com.boatit.boatsharing.data.local.prefmanager.StatusProvider
import com.boatit.boatsharing.data.local.prefmanager.TokenProvider
import com.boatit.boatsharing.data.local.prefmanager.UserSessionStore
import com.boatit.boatsharing.data.local.session.ClearSessionUseCase
import com.boatit.boatsharing.data.local.session.SessionController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val coreModule =
    module {
        single { FirebaseAuth.getInstance() }
        single { FirebaseDatabase.getInstance() }
        single { FirebaseMessaging.getInstance() }
        single { FirebaseFirestore.getInstance() }

        single<FusedLocationProviderClient> {
            LocationServices.getFusedLocationProviderClient(androidContext())
        }
        single { GoogleDirectionsApi() }
        single { FirebaseNotificationService(get(), androidContext()) }

        viewModel { NotificationViewModel() }
        viewModel { TrackingLocationViewModel(get(), get(), get(), get()) }
        viewModel { SendNotificationViewModel(get()) }
        viewModel { LocationViewModel(get(), get(), get(), get()) }

        single<ITokenProvider> { TokenProvider(androidContext()) }
        single<IRoleProvider> { RoleProvider(androidContext()) }
        single<ICaptainStatusProvider> { StatusProvider(androidContext()) }
        single { Gson() }
        single { SharedPrefManager(androidContext()) }
        single { com.boatit.boatsharing.data.local.session.SessionManager(androidContext(), get()) }
        single { UnauthorizedSessionHandler(get()) { get() } }
        single { AuthTokenInterceptor(get()) }
        single { UnauthorizedResponseInterceptor(get()) }
        single { createRetrofitOkHttpClient(get(), get()) }
        single { createRetrofit(get(), get()) }
        single<BoatitApiService> { get<retrofit2.Retrofit>().create(BoatitApiService::class.java) }
        single { ApiExecutor(get(), get()) }
        single { UserSessionStore(get()) }
        single { ClearSessionUseCase(get()) { get() } }
        single { SessionController(get()) }

        single { createKtorClient(get(), get()) }
    }
