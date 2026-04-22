package com.boatit.boatsharing.domain.core

import java.io.IOException

/**
 * Custom exception hierarchy for domain layer
 * Provides type-safe error handling and recovery strategies
 */

/**
 * Base exception for domain layer
 * All custom exceptions should extend this
 */
open class DomainException(
    message: String,
    cause: Throwable? = null,
) : Exception(message, cause)

/**
 * Network-related exceptions
 * Indicates connectivity or protocol-level issues
 *
 * Typical causes:
 * - No internet connection
 * - Timeout
 * - DNS resolution failure
 */
class NetworkException(
    message: String = "Network error occurred",
    cause: Throwable? = null,
) : DomainException(message, cause)

/**
 * API server exceptions
 * Indicates HTTP errors from backend (4xx, 5xx)
 *
 * Properties:
 * - code: HTTP status code
 * - serverMessage: Error message from server
 */
data class ApiException(
    val code: Int,
    val serverMessage: String? = null,
    override val message: String = "API Error: $code",
    override val cause: Throwable? = null,
) : DomainException(message, cause)

/**
 * Validation exceptions
 * Indicates business logic or input validation failure
 *
 * Properties:
 * - field: Name of the field that failed validation
 * - rules: Description of validation rules that failed
 */
data class ValidationException(
    val field: String? = null,
    val rules: String? = null,
    override val message: String = "Validation failed",
) : DomainException(message)

/**
 * Authentication exceptions
 * Indicates authentication failure or token expiration
 *
 * Common causes:
 * - Invalid credentials
 * - Token expired
 * - Unauthorized access
 */
class AuthenticationException(
    message: String = "Authentication failed",
    cause: Throwable? = null,
) : DomainException(message, cause)

/**
 * Authorization exceptions
 * Indicates insufficient permissions
 */
class AuthorizationException(
    message: String = "Insufficient permissions",
    cause: Throwable? = null,
) : DomainException(message, cause)

/**
 * Generic server exception
 * Indicates unexpected server error (5xx)
 */
class ServerException(
    message: String = "Server error occurred",
    val code: Int? = null,
    cause: Throwable? = null,
) : DomainException(message, cause)

/**
 * Not found exception
 * Indicates resource doesn't exist (404)
 */
class NotFoundException(
    val resourceId: String? = null,
    message: String = "Resource not found",
) : DomainException(message)

/**
 * Conflict exception
 * Indicates resource conflict (409)
 * Example: Duplicate entry, concurrent modification
 */
class ConflictException(
    message: String = "Resource conflict",
    cause: Throwable? = null,
) : DomainException(message, cause)

/**
 * Rate limit exception
 * Indicates too many requests (429)
 */
data class RateLimitException(
    val retryAfterSeconds: Int? = null,
    override val message: String = "Rate limit exceeded",
) : DomainException(message)

/**
 * Timeout exception
 * Indicates request timeout
 */
class TimeoutException(
    val timeoutMs: Long? = null,
    message: String = "Request timeout",
    cause: Throwable? = null,
) : DomainException(message, cause)

/**
 * Exception mapping utility
 * Converts various exceptions into domain exceptions with proper context
 */
object ExceptionMapper {
    /**
     * Map any exception to appropriate domain exception
     *
     * @param throwable The exception to map
     * @return Mapped domain exception
     */
    fun mapException(throwable: Throwable): DomainException {
        return when (throwable) {
            is DomainException -> throwable
            is IOException -> NetworkException("Network error: ${throwable.message}", throwable)
            is IllegalArgumentException -> ValidationException(message = throwable.message ?: "Validation failed")
            else -> DomainException("Unexpected error: ${throwable.message}", throwable)
        }
    }

    /**
     * Map HTTP status code to appropriate exception
     *
     * @param code HTTP status code
     * @param message Server message or body
     * @return Appropriate domain exception
     */
    fun mapHttpException(code: Int, message: String?): DomainException {
        return when (code) {
            400 -> ValidationException(message = message ?: "Bad request")
            401 -> AuthenticationException(message ?: "Unauthorized")
            403 -> AuthorizationException(message ?: "Forbidden")
            404 -> NotFoundException(message = message ?: "Not found")
            409 -> ConflictException(message ?: "Conflict")
            429 -> RateLimitException(message = message ?: "Too many requests")
            in 500..599 -> ServerException(message ?: "Server error", code)
            else -> ApiException(code, message, "HTTP Error: $code")
        }
    }
}
