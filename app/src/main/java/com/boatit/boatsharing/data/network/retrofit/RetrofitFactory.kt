package com.boatit.boatsharing.data.network.retrofit

import com.boatit.boatsharing.data.network.di.ApiConstants
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val NETWORK_TIMEOUT_SECONDS = 30L

fun createRetrofitOkHttpClient(
    authTokenInterceptor: AuthTokenInterceptor,
    unauthorizedResponseInterceptor: UnauthorizedResponseInterceptor,
): OkHttpClient =
    OkHttpClient
        .Builder()
        .addInterceptor(authTokenInterceptor)
        .addInterceptor(unauthorizedResponseInterceptor)
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
        .connectTimeout(NETWORK_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .readTimeout(NETWORK_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .writeTimeout(NETWORK_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .build()

fun createRetrofit(
    okHttpClient: OkHttpClient,
    gson: Gson,
): Retrofit =
    Retrofit
        .Builder()
        .baseUrl(ApiConstants.BASE_URL.ensureTrailingSlash())
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

private fun String.ensureTrailingSlash(): String = if (endsWith("/")) this else "$this/"
