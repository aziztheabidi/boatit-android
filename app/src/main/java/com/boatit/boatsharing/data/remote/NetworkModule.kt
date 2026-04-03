package com.boatit.boatsharing.data.remote

import com.boatit.boatsharing.data.remote.api.AccountApi
import com.boatit.boatsharing.data.remote.api.BusinessProfileApi
import com.boatit.boatsharing.data.remote.api.DockApi
import com.boatit.boatsharing.data.remote.api.RegistrationTempApi
import com.boatit.boatsharing.data.remote.api.UserProfileApi
import com.boatit.boatsharing.data.remote.api.VoyageApi
import com.boatit.boatsharing.network.di.ApiConstants
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private fun apiBaseUrl(): String {
    val base = ApiConstants.BASE_URL.trim()
    return if (base.endsWith("/")) base else "$base/"
}

val networkModule = module {
    single<Gson> { GsonBuilder().serializeNulls().create() }

    single {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .addInterceptor(AuthInterceptor(get()))
            .authenticator(TokenRefreshAuthenticator(get(), get()))
            .build()
    }

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(apiBaseUrl())
            .client(get())
            .addConverterFactory(GsonConverterFactory.create(get<Gson>()))
            .build()
    }

    single<AccountApi> { get<Retrofit>().create(AccountApi::class.java) }
    single<RegistrationTempApi> { get<Retrofit>().create(RegistrationTempApi::class.java) }
    single<DockApi> { get<Retrofit>().create(DockApi::class.java) }
    single<VoyageApi> { get<Retrofit>().create(VoyageApi::class.java) }
    single<UserProfileApi> { get<Retrofit>().create(UserProfileApi::class.java) }
    single<BusinessProfileApi> { get<Retrofit>().create(BusinessProfileApi::class.java) }
}
