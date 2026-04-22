package com.boatit.boatsharing.data.network.retrofit

import com.boatit.boatsharing.data.network.di.ApiError
import com.boatit.boatsharing.domain.core.ExceptionMapper
import com.google.gson.Gson
import retrofit2.Response

class ApiExecutor(
    @PublishedApi internal val apiService: BoatitApiService,
    @PublishedApi internal val gson: Gson,
) {
    suspend inline fun <reified T> post(
        endpoint: String,
        body: Any,
        successCode: Int = 200,
        authorization: String? = null,
    ): Result<T> {
        val response = apiService.post(endpoint.removePrefix("/"), body, authorization)
        return parseResponse(response, successCode)
    }

    suspend inline fun <reified T> get(
        endpoint: String,
        query: Map<String, String> = emptyMap(),
        successCode: Int = 200,
        authorization: String? = null,
    ): Result<T> {
        val response = apiService.get(endpoint.removePrefix("/"), query, authorization)
        return parseResponse(response, successCode)
    }

    inline fun <reified T> parseResponse(
        response: Response<okhttp3.ResponseBody>,
        successCode: Int,
    ): Result<T> {
        if (response.code() == successCode) {
            val body = response.body()?.string().orEmpty()
            return runCatching { gson.fromJson(body, T::class.java) }
                .fold(
                    onSuccess = { Result.success(it) },
                    onFailure = { Result.failure(Exception("Failed to parse response", it)) },
                )
        }

        val errorBody = response.errorBody()?.string().orEmpty()
        val apiError = runCatching { gson.fromJson(errorBody, ApiError::class.java) }.getOrNull()
        val message = apiError?.Message ?: response.message().ifBlank { "Request failed" }
        return Result.failure(ExceptionMapper.mapHttpException(response.code(), message))
    }
}
