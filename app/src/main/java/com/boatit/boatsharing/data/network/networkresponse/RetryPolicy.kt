package com.boatit.boatsharing.data.network.networkresponse

import kotlin.math.min

/**
 * Retry policy for network requests
 * Implements exponential backoff with jitter for robust retries
 *
 * Strategy:
 * - Exponential backoff: delay doubles with each retry
 * - Maximum jitter to prevent thundering herd
 * - Maximum retries limit to prevent infinite loops
 * - Configurable for different scenarios
 *
 * Example:
 * ```
 * val retryPolicy = RetryPolicy(
 *     maxRetries = 3,
 *     initialDelayMs = 1000,
 *     maxDelayMs = 30000
 * )
 *
 * var result: Resource<T>? = null
 * for (attempt in 1..retryPolicy.maxRetries) {
 *     result = apiCall()
 *     if (result is Resource.Success) break
 *     if (attempt < retryPolicy.maxRetries) {
 *         delay(retryPolicy.getDelayMs(attempt))
 *     }
 * }
 * ```
 */
data class RetryPolicy(
    val maxRetries: Int = 3,
    val initialDelayMs: Long = 1000,
    val maxDelayMs: Long = 30000,
    val backoffMultiplier: Float = 2f,
) {
    /**
     * Get delay in milliseconds for attempt number
     * Implements exponential backoff with jitter
     */
    fun getDelayMs(attemptNumber: Int): Long {
        val exponentialDelay = (initialDelayMs * (backoffMultiplier.pow(attemptNumber - 1))).toLong()
        val cappedDelay = min(exponentialDelay, maxDelayMs)

        // Add random jitter (±10% of delay)
        val jitter = (cappedDelay * 0.1f * (Math.random() - 0.5f)).toLong()
        return (cappedDelay + jitter).coerceAtLeast(0)
    }

    /**
     * Check if should retry given the attempt number and exception
     */
    fun shouldRetry(
        attemptNumber: Int,
        exception: Throwable,
    ): Boolean {
        if (attemptNumber >= maxRetries) return false

        // Retry on network errors
        return exception is java.io.IOException ||
            exception.message?.contains("timeout", ignoreCase = true) == true ||
            exception.message?.contains("connection", ignoreCase = true) == true
    }
}

/**
 * Default retry policies for common scenarios
 */
object RetryPolicies {
    val DEFAULT = RetryPolicy(
        maxRetries = 3,
        initialDelayMs = 1000,
        maxDelayMs = 30000,
    )

    val AGGRESSIVE = RetryPolicy(
        maxRetries = 5,
        initialDelayMs = 500,
        maxDelayMs = 60000,
    )

    val CONSERVATIVE = RetryPolicy(
        maxRetries = 1,
        initialDelayMs = 500,
        maxDelayMs = 5000,
    )

    val NONE = RetryPolicy(maxRetries = 0)
}

/**
 * Extension function for Float to calculate power
 */
private fun Float.pow(exponent: Int): Float {
    return (0 until exponent).fold(1f) { acc, _ -> acc * this }
}
