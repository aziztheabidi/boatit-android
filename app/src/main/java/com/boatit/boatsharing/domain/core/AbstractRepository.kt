package com.boatit.boatsharing.domain.core

import com.boatit.boatsharing.domain.core.Resource

/**
 * Abstract base class for Repository implementations
 * Provides common patterns and error handling for data access layer
 *
 * Key responsibilities:
 * - Handle network errors consistently
 * - Support caching strategies
 * - Implement retry logic
 * - Ensure consistent error mapping
 *
 * Example implementation:
 * ```
 * class UserRepository(
 *     private val userApi: UserApi,
 *     private val userDao: UserDao
 * ) : AbstractRepository() {
 *
 *     suspend fun getUser(id: String): Resource<User> = safeApiCall {
 *         userApi.getUser(id)
 *     }
 * }
 * ```
 */
abstract class AbstractRepository {
    /**
     * Safe API call wrapper with error handling and mapping
     *
     * @param apiCall The suspend function that makes the API call
     * @return Resource<T> containing Success, Error, or Loading state
     */
    protected suspend inline fun <T> safeApiCall(
        crossinline apiCall: suspend () -> Result<T>,
    ): Resource<T> {
        return try {
            val result = apiCall()
            result.fold(
                onSuccess = { Resource.Success(it) },
                onFailure = { throwable ->
                    Resource.Error(mapException(throwable))
                },
            )
        } catch (e: Exception) {
            Resource.Error(mapException(e))
        }
    }

    /**
     * Safe API call with Resource result type
     * Use when API already returns Resource<T>
     */
    protected suspend inline fun <T> safeApiResource(
        crossinline apiCall: suspend () -> Resource<T>,
    ): Resource<T> {
        return try {
            apiCall()
        } catch (e: Exception) {
            Resource.Error(mapException(e))
        }
    }

    /**
     * Map exceptions to appropriate ErrorType
     * Override in subclasses for specific error handling
     */
    protected open fun mapException(throwable: Throwable): ErrorType {
        return when (throwable) {
            is IllegalArgumentException -> ErrorType.Validation(throwable.message.orEmpty())
            else -> throwable.toErrorType()
        }
    }

    /**
     * Handle loading state transitions
     * Can be used to track loading states across repository
     */
    protected fun <T> Resource<T>.onLoadingChange(
        onLoading: (() -> Unit)? = null,
        onComplete: (() -> Unit)? = null,
    ): Resource<T> {
        when (this) {
            is Resource.Loading -> onLoading?.invoke()
            is Resource.Success, is Resource.Error -> onComplete?.invoke()
        }
        return this
    }
}
