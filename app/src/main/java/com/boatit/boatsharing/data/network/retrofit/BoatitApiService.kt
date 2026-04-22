package com.boatit.boatsharing.data.network.retrofit

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.QueryMap
import retrofit2.http.Url

interface BoatitApiService {
    @POST
    suspend fun post(
        @Url endpoint: String,
        @Body body: Any,
        @Header("Authorization") authorization: String? = null,
    ): Response<ResponseBody>

    @GET
    suspend fun get(
        @Url endpoint: String,
        @QueryMap query: Map<String, String> = emptyMap(),
        @Header("Authorization") authorization: String? = null,
    ): Response<ResponseBody>
}
