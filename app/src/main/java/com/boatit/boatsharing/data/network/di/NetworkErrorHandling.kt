package com.boatit.boatsharing.data.network.di

import com.boatit.boatsharing.domain.core.ApiException
import com.boatit.boatsharing.domain.core.ErrorType
import com.boatit.boatsharing.domain.core.ExceptionMapper
import com.boatit.boatsharing.domain.core.NetworkException
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

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

suspend inline fun <reified Req : Any, reified Res : Any> executePostRequest(
    httpClient: HttpClient,
    url: String,
    requestBody: Req,
    successStatus: HttpStatusCode,
    crossinline requestConfig: HttpRequestBuilder.() -> Unit = {},
    crossinline onApiError: (responseBody: Res, status: HttpStatusCode) -> Exception,
    crossinline onException: (exception: Exception) -> Result<Res>,
): Result<Res> {
    return try {
        val response: HttpResponse =
            httpClient.post(url) {
                contentType(ContentType.Application.Json)
                requestConfig()
                setBody(requestBody)
            }
        val responseBody: Res = response.body()
        if (response.status == successStatus) {
            Result.success(responseBody)
        } else {
            Result.failure(onApiError(responseBody, response.status))
        }
    } catch (e: Exception) {
        onException(e)
    }
}

/**
 * Executes a GET request and maps the [HttpResponse] in [handleResponse].
 * Use for repositories that need custom success vs non-success handling (query params, mixed body types).
 */
suspend inline fun <reified Res : Any> executeGetRequest(
    httpClient: HttpClient,
    url: String,
    crossinline requestConfig: HttpRequestBuilder.() -> Unit = {},
    crossinline handleResponse: suspend (HttpResponse) -> Result<Res>,
    crossinline onException: (Exception) -> Result<Res>,
): Result<Res> {
    return try {
        val response: HttpResponse =
            httpClient.get(url) {
                requestConfig()
            }
        handleResponse(response)
    } catch (e: Exception) {
        onException(e)
    }
}
