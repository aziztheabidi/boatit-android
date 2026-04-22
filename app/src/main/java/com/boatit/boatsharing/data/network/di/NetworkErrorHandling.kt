package com.boatit.boatsharing.data.network.di

import com.boatit.boatsharing.domain.core.ApiException
import com.boatit.boatsharing.domain.core.ErrorType
import com.boatit.boatsharing.domain.core.ExceptionMapper
import com.boatit.boatsharing.domain.core.NetworkException
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode

/**
 * Network error handling utilities
 * Provides consistent error mapping and failure handling across the app
 */

/**
 * Convert HttpResponse to Result<T>
 * Parses response and maps to appropriate result type
 */
suspend inline fun <reified T> HttpResponse.toResult(
    successStatus: HttpStatusCode = HttpStatusCode.OK,
): Result<T> {
    return try {
        when {
            status == successStatus -> {
                val body = this.call.body<T>()
                Result.success(body)
            }

            status.value in 400..499 -> {
                Result.failure<T>(
                    ExceptionMapper.mapHttpException(status.value, status.description),
                )
            }

            status.value in 500..599 -> {
                Result.failure<T>(
                    ExceptionMapper.mapHttpException(status.value, "Server error"),
                )
            }

            else -> {
                Result.failure<T>(
                    Exception("Unexpected response: ${status.description}"),
                )
            }
        }
    } catch (e: Exception) {
        Result.failure<T>(e)
    }
}

/**
 * Handle network failures with proper error type mapping
 * Used in repository catch blocks
 */
fun <T> networkFailure(
    message: String,
    exception: Exception,
): Result<T> {
    val errorType = when (exception) {
        is ClientRequestException -> {
            ErrorType.Network(message, exception.response.status.value)
        }

        is ServerResponseException -> {
            ErrorType.Server(message, exception.response.status.value)
        }

        else -> ErrorType.Network(message)
    }

    return Result.failure(
        Exception("$message: ${errorType.toMessage()}"),
    )
}

/**
 * Extension function to map exception to ErrorType
 */
fun Exception.mapToErrorType(): ErrorType {
    return when (this) {
        is ClientRequestException -> {
            ErrorType.Network("Client error: ${response.status.description}", response.status.value)
        }

        is ServerResponseException -> {
            ErrorType.Server("Server error: ${response.status.description}", response.status.value)
        }

        else -> ErrorType.Unknown(message = message ?: "Unknown error", throwable = this)
    }
}
