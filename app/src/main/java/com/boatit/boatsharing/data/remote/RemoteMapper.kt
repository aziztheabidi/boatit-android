package com.boatit.boatsharing.data.remote

import retrofit2.Response

/**
 * Maps Retrofit [Response] to [Result] for the repository layer.
 * Keeps HTTP details out of ViewModels.
 */
object RemoteMapper {
    fun <T> toResult(response: Response<T>): Result<T> {
        return try {
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) Result.success(body)
                else Result.failure(IllegalStateException("Empty response body"))
            } else {
                val err = response.errorBody()?.string()?.takeIf { it.isNotBlank() }
                    ?: response.message()
                Result.failure(Exception("HTTP ${response.code()}: $err"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
