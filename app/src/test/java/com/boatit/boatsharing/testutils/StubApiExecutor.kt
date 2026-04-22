package com.boatit.boatsharing.testutils

import com.boatit.boatsharing.data.network.retrofit.ApiExecutor
import com.boatit.boatsharing.data.network.retrofit.BoatitApiService
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

/** [ApiExecutor] backed by a [BoatitApiService] that always returns HTTP 200 with the given JSON body. */
fun apiExecutorReturningJson(json: String): ApiExecutor {
    val gson = Gson()
    val jsonBody = json.toResponseBody("application/json; charset=utf-8".toMediaType())
    val service =
        object : BoatitApiService {
            override suspend fun post(
                endpoint: String,
                body: Any,
                authorization: String?,
            ): Response<ResponseBody> = Response.success(jsonBody)

            override suspend fun get(
                endpoint: String,
                query: Map<String, String>,
                authorization: String?,
            ): Response<ResponseBody> = error("Unexpected GET: $endpoint")
        }
    return ApiExecutor(service, gson)
}
