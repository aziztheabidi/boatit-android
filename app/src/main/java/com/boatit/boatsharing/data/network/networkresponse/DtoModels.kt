package com.boatit.boatsharing.data.network.networkresponse

import kotlinx.serialization.Serializable

/**
 * Base class for all API request objects
 * Ensures consistent structure and type safety for all requests
 *
 * Benefits:
 * - Type safety for request validation
 * - Consistent serialization handling
 * - Easy to add common headers or transformations
 *
 * Example:
 * ```
 * @Serializable
 * data class LoginRequestDto(
 *     val email: String,
 *     val password: String
 * ) : RequestModel
 * ```
 */
interface RequestModel

/**
 * Sealed class for API responses
 * Provides type-safe response handling with generic wrapper
 *
 * Example usage:
 * ```
 * @Serializable
 * data class UserResponseDto(
 *     val id: String,
 *     val name: String,
 *     val email: String
 * ) : ResponseModel<UserResponseDto>
 *
 * @Serializable
 * data class ApiResponseWrapper<T : ResponseModel<T>>(
 *     val success: Boolean,
 *     val data: T?,
 *     val message: String?
 * )
 * ```
 */
interface ResponseModel<T>

/**
 * Standard API error response model
 * Implements ResponseModel for consistent error handling
 */
@Serializable
data class ErrorResponseDto(
    val code: String? = null,
    val message: String? = null,
    val errors: Map<String, String>? = null,
    val timestamp: String? = null,
) : ResponseModel<ErrorResponseDto>

/**
 * Generic API response wrapper for consistent API contracts
 * Use this to wrap responses from backend
 *
 * Example from backend:
 * ```
 * {
 *   "success": true,
 *   "message": "Operation successful",
 *   "data": { ... },
 *   "code": 200
 * }
 * ```
 */
@Serializable
data class ApiResponseWrapper<T>(
    val success: Boolean,
    val message: String? = null,
    val data: T? = null,
    val code: Int? = null,
    val statusCode: Int? = null,
)

/**
 * Pagination request parameters
 * Use as a base for paginated requests
 */
@Serializable
data class PaginationRequestDto(
    val pageNumber: Int = 1,
    val pageSize: Int = 20,
) : RequestModel

/**
 * Pagination response metadata
 * Include this in paginated responses
 */
@Serializable
data class PaginationMetadata(
    val pageNumber: Int = 1,
    val pageSize: Int = 20,
    val totalPages: Int = 0,
    val totalItems: Int = 0,
    val hasNextPage: Boolean = false,
    val hasPreviousPage: Boolean = false,
)
