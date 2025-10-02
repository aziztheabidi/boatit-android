package com.boatit.boatsharing.network.interceptors

import android.util.Log
import com.boatit.boatsharing.utils.session.SessionManager
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.io.IOException
import java.lang.Math.pow

/**
 * Network interceptor that handles automatic retry logic and session management
 * 
 * @deprecated This class is deprecated. Use KtorClient.kt with native Ktor plugins instead.
 * The functionality has been migrated to createKtorClientWithInterceptor() which provides:
 * - Better performance using Ktor's native HttpRequestRetry plugin
 * - Automatic token refresh via Auth plugin
 * - Native timeout handling via HttpTimeout plugin
 * - Simplified maintenance and future-proofing
 * 
 * Implements LLR-3.1.1: Server Error Detection Implementation
 * Implements LLR-3.1.2: Exponential Backoff Implementation
 * Implements LLR-3.1.3: Retry Logic Implementation
 * Implements LLR-3.2.1: Timeout Error Detection Implementation
 * Implements LLR-3.2.2: Linear Backoff Implementation
 * Implements LLR-3.2.3: Timeout Retry Logic Implementation
 * Implements LLR-3.3.1: Client Error Detection Implementation
 * Implements LLR-3.3.2: No-Retry Policy Implementation
 * Implements LLR-3.4.1: Malformed Response Detection Implementation
 * Implements LLR-3.4.2: Malformed Response Handling Implementation
 * Implements LLR-3.5.1: Retry Limit Enforcement Implementation
 */
@Deprecated(
    message = "Use KtorClient.kt with native Ktor plugins instead. Migrated to createKtorClientWithInterceptor()",
    replaceWith = ReplaceWith("createKtorClientWithInterceptor(tokenProvider, sessionManager)", "com.boatit.boatsharing.network.di.createKtorClientWithInterceptor"),
    level = DeprecationLevel.WARNING
)
class NetworkInterceptor(
    private val sessionManager: SessionManager
) {
    
    companion object {
        private const val MAX_RETRY_ATTEMPTS = 3
        private const val BASE_RETRY_DELAY_MS = 1000L
        private const val EXPONENTIAL_BACKOFF_MULTIPLIER = 2
        private const val LINEAR_BACKOFF_INCREMENT_MS = 2000L
        
        // LLR-3.1.1: Server Error Detection Implementation
        private val SERVER_ERROR_CODES = setOf(
            HttpStatusCode.InternalServerError,
            HttpStatusCode.BadGateway,
            HttpStatusCode.ServiceUnavailable,
            HttpStatusCode.GatewayTimeout,
            HttpStatusCode.VariantAlsoNegotiates,
            HttpStatusCode.InsufficientStorage,
        )
        
        // LLR-3.2.1: Timeout Error Detection Implementation
        private val TIMEOUT_ERROR_CODES = setOf(
            HttpStatusCode.RequestTimeout,
            HttpStatusCode.GatewayTimeout
        )
        
        // LLR-3.3.1: Client Error Detection Implementation
        private val CLIENT_ERROR_CODES = setOf(
            HttpStatusCode.BadRequest,
            HttpStatusCode.Unauthorized,
            HttpStatusCode.PaymentRequired,
            HttpStatusCode.Forbidden,
            HttpStatusCode.NotFound,
            HttpStatusCode.MethodNotAllowed,
            HttpStatusCode.NotAcceptable,
            HttpStatusCode.ProxyAuthenticationRequired,
            HttpStatusCode.Conflict,
            HttpStatusCode.Gone,
            HttpStatusCode.LengthRequired,
            HttpStatusCode.PreconditionFailed,
            HttpStatusCode.PayloadTooLarge,
            HttpStatusCode.UnsupportedMediaType,
            HttpStatusCode.ExpectationFailed,
            HttpStatusCode.UnprocessableEntity,
            HttpStatusCode.Locked,
            HttpStatusCode.FailedDependency,
            HttpStatusCode.TooEarly,
            HttpStatusCode.UpgradeRequired,
            HttpStatusCode.TooManyRequests
        )
    }
    
    /**
     * LLR-3.1.2: Exponential Backoff Implementation
     * LLR-3.2.2: Linear Backoff Implementation
     * 
     * Calculates retry delay based on error type and attempt number
     */
    private fun getRetryDelay(attempt: Int, errorType: ErrorType): Long {
        return when (errorType) {
            ErrorType.SERVER_ERROR -> {
                // LLR-3.1.2: Exponential Backoff Implementation
                val delay = BASE_RETRY_DELAY_MS * pow(EXPONENTIAL_BACKOFF_MULTIPLIER.toDouble(), attempt.toDouble()).toLong()
                Log.d("NetworkInterceptor", "Exponential backoff delay: ${delay}ms for attempt $attempt")
                delay
            }
            ErrorType.TIMEOUT_ERROR -> {
                // LLR-3.2.2: Linear Backoff Implementation
                val delay = BASE_RETRY_DELAY_MS + (LINEAR_BACKOFF_INCREMENT_MS * attempt)
                Log.d("NetworkInterceptor", "Linear backoff delay: ${delay}ms for attempt $attempt")
                delay
            }
            else -> BASE_RETRY_DELAY_MS
        }
    }
    
    /**
     * LLR-3.1.1: Server Error Detection Implementation
     * LLR-3.1.3: Retry Logic Implementation
     * 
     * Handles server errors (5xx) with exponential backoff retry
     */
    private suspend fun handleServerError(response: HttpResponse, attempt: Int): HttpResponse {
        Log.w("NetworkInterceptor", "Server error detected: ${response.status}")
        
        // LLR-3.1.1: Server Error Detection Implementation
        val isServerError = SERVER_ERROR_CODES.contains(response.status)
        
        if (isServerError && attempt < MAX_RETRY_ATTEMPTS) {
            Log.i("NetworkInterceptor", "Retrying server error (attempt $attempt)")
            
            // LLR-3.1.3: Retry Logic Implementation
            val delay = getRetryDelay(attempt, ErrorType.SERVER_ERROR)
            delay(delay)
            
            // Return null to indicate retry should happen
            return response
        } else {
            Log.e("NetworkInterceptor", "Server error max retries reached or non-retryable")
            return response
        }
    }
    
    /**
     * LLR-3.2.1: Timeout Error Detection Implementation
     * LLR-3.2.3: Timeout Retry Logic Implementation
     * 
     * Handles timeout errors with linear backoff retry
     */
    private suspend fun handleTimeoutError(response: HttpResponse, attempt: Int): HttpResponse {
        Log.w("NetworkInterceptor", "Timeout error detected: ${response.status}")
        
        // LLR-3.2.1: Timeout Error Detection Implementation
        val isTimeoutError = TIMEOUT_ERROR_CODES.contains(response.status)
        
        if (isTimeoutError && attempt < MAX_RETRY_ATTEMPTS) {
            Log.i("NetworkInterceptor", "Retrying timeout error (attempt $attempt)")
            
            // LLR-3.2.3: Timeout Retry Logic Implementation
            val delay = getRetryDelay(attempt, ErrorType.TIMEOUT_ERROR)
            delay(delay)
            
            return response
        } else {
            Log.e("NetworkInterceptor", "Timeout error max retries reached")
            return response
        }
    }
    
    /**
     * LLR-3.3.1: Client Error Detection Implementation
     * LLR-3.3.2: No-Retry Policy Implementation
     * 
     * Handles client errors (4xx) with no retry policy
     */
    private fun handleClientError(response: HttpResponse): HttpResponse {
        Log.w("NetworkInterceptor", "Client error detected: ${response.status}")
        
        // LLR-3.3.1: Client Error Detection Implementation
        val isClientError = CLIENT_ERROR_CODES.contains(response.status)
        
        if (isClientError) {
            // LLR-3.3.2: No-Retry Policy Implementation
            Log.i("NetworkInterceptor", "Client error - no retry policy applied")
            
            // Handle specific client errors
            when (response.status) {
                HttpStatusCode.Unauthorized -> {
                    Log.w("NetworkInterceptor", "Unauthorized (401) - triggering token refresh")
                    runBlocking { sessionManager.handleUnauthorized() }
                }
                HttpStatusCode.Forbidden -> {
                    Log.w("NetworkInterceptor", "Forbidden (403) - triggering account deactivation")
                    runBlocking { sessionManager.handleAccountDeactivated() }
                }
                else -> {
                    Log.d("NetworkInterceptor", "Other client error: ${response.status}")
                }
            }
        }
        
        return response
    }
    
    /**
     * LLR-3.4.1: Malformed Response Detection Implementation
     * 
     * Detects malformed responses from network requests
     */
    private fun detectMalformedResponse(response: HttpResponse): Boolean {
        return try {
            Log.d("NetworkInterceptor", "Detecting malformed response")
            
            val isMalformed = when {
                response.status.value < 100 || response.status.value >= 600 -> {
                    Log.w("NetworkInterceptor", "Invalid HTTP status code: ${response.status}")
                    true
                }
                response.contentLength() == 0L && response.status != HttpStatusCode.NoContent -> {
                    Log.w("NetworkInterceptor", "Empty response body for non-204 status")
                    true
                }
                response.headers.isEmpty() -> {
                    Log.w("NetworkInterceptor", "Response missing headers")
                    true
                }
                else -> {
                    Log.d("NetworkInterceptor", "Response validation passed")
                    false
                }
            }
            
            Log.i("NetworkInterceptor", "Malformed response detection result: $isMalformed")
            isMalformed
            
        } catch (e: Exception) {
            Log.e("NetworkInterceptor", "Malformed response detection failed: ${e.message}")
            true
        }
    }
    
    /**
     * LLR-3.4.2: Malformed Response Handling Implementation
     * 
     * Handles malformed responses gracefully
     */
    private fun handleMalformedResponse(response: HttpResponse): HttpResponse {
        Log.w("NetworkInterceptor", "Handling malformed response")
        
        // Log the malformed response for debugging
        Log.e("NetworkInterceptor", "Malformed response details: " +
                "status=${response.status}, " +
                "contentLength=${response.contentLength()}, " +
                "headers=${response.headers}")
        
        // Return the response as-is, but log the issue
        return response
    }
    
    /**
     * LLR-3.5.1: Retry Limit Enforcement Implementation
     * 
     * Main intercept function that orchestrates all error handling and retry logic
     */
    suspend fun intercept(
        request: HttpRequestBuilder,
        execute: suspend (HttpRequestBuilder) -> HttpResponse
    ): HttpResponse {
        var attempt = 0
        var lastException: Exception? = null
        
        Log.i("NetworkInterceptor", "Starting network request interception")
        
        while (attempt <= MAX_RETRY_ATTEMPTS) {
            try {
                Log.d("NetworkInterceptor", "Network request attempt ${attempt + 1}")
                val response = execute(request)
                
                // LLR-3.4.1: Malformed Response Detection Implementation
                if (detectMalformedResponse(response)) {
                    Log.w("NetworkInterceptor", "Malformed response detected, handling gracefully")
                    return handleMalformedResponse(response)
                }
                
                when {
                    // Success responses
                    response.status.value in 200..299 -> {
                        Log.i("NetworkInterceptor", "Request successful: ${response.status}")
                        sessionManager.resetRetryAttempts()
                        return response
                    }
                    
                    // Server errors (5xx) - retry with exponential backoff
                    SERVER_ERROR_CODES.contains(response.status) -> {
                        Log.w("NetworkInterceptor", "Server error: ${response.status}")
                        val handledResponse = handleServerError(response, attempt)
                        if (attempt < MAX_RETRY_ATTEMPTS) {
                            attempt++
                            continue
                        } else {
                            return handledResponse
                        }
                    }
                    
                    // Timeout errors - retry with linear backoff
                    TIMEOUT_ERROR_CODES.contains(response.status) -> {
                        Log.w("NetworkInterceptor", "Timeout error: ${response.status}")
                        val handledResponse = handleTimeoutError(response, attempt)
                        if (attempt < MAX_RETRY_ATTEMPTS) {
                            attempt++
                            continue
                        } else {
                            return handledResponse
                        }
                    }
                    
                    // Client errors (4xx) - no retry
                    CLIENT_ERROR_CODES.contains(response.status) -> {
                        Log.w("NetworkInterceptor", "Client error: ${response.status}")
                        return handleClientError(response)
                    }
                    
                    // Other responses - don't retry
                    else -> {
                        Log.d("NetworkInterceptor", "Other response: ${response.status}")
                        return response
                    }
                }
                
            } catch (e: Exception) {
                lastException = e
                Log.w("NetworkInterceptor", "Network request exception: ${e.message}")
                
                // Check if it's a network-related exception that should be retried
                if (shouldRetryException(e) && attempt < MAX_RETRY_ATTEMPTS) {
                    Log.i("NetworkInterceptor", "Retrying due to exception (attempt $attempt)")
                    attempt++
                    val delay = getRetryDelay(attempt, ErrorType.NETWORK_ERROR)
                    delay(delay)
                    continue
                } else {
                    Log.e("NetworkInterceptor", "Exception not retryable or max attempts reached")
                    throw e
                }
            }
        }
        
        // If we get here, all retries failed
        val errorMessage = "Network request failed after $MAX_RETRY_ATTEMPTS attempts"
        Log.e("NetworkInterceptor", errorMessage)
        throw lastException ?: Exception(errorMessage)
    }
    
    /**
     * Determine if an exception should trigger a retry
     */
    private fun shouldRetryException(exception: Exception): Boolean {
        return when (exception) {
            is SocketTimeoutException -> {
                Log.d("NetworkInterceptor", "Socket timeout - will retry")
                true
            }
            is UnknownHostException -> {
                Log.d("NetworkInterceptor", "Unknown host - will retry")
                true
            }
            is IOException -> {
                Log.d("NetworkInterceptor", "IO exception - will retry")
                true
            }
            is ClientRequestException -> {
                Log.d("NetworkInterceptor", "Client request exception - will not retry")
                false
            }
            is ServerResponseException -> {
                Log.d("NetworkInterceptor", "Server response exception - will retry")
                true
            }
            is RedirectResponseException -> {
                Log.d("NetworkInterceptor", "Redirect response exception - will not retry")
                false
            }
            else -> {
                Log.d("NetworkInterceptor", "Other exception - will retry")
                true
            }
        }
    }
}

/**
 * Error types for retry logic
 */
private enum class ErrorType {
    SERVER_ERROR,
    TIMEOUT_ERROR,
    CLIENT_ERROR,
    NETWORK_ERROR
}
