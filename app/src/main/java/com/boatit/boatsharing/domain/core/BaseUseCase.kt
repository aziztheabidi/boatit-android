package com.boatit.boatsharing.domain.core

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Abstract base class for all Use Cases in the domain layer
 * Implements SOLID principles and clean architecture patterns
 *
 * A UseCase orchestrates business logic by:
 * - Combining multiple repositories
 * - Enforcing business rules
 * - Handling error conditions
 * - Managing async operations on appropriate dispatchers
 *
 * Example implementation:
 * ```
 * class GetUserProfileUseCase(
 *     private val userRepository: UserRepository,
 *     private val preferencesRepository: PreferencesRepository,
 *     dispatcher: CoroutineDispatcher = Dispatchers.IO
 * ) : BaseUseCase<String, User>(dispatcher) {
 *
 *     override suspend fun execute(params: String): Resource<User> {
 *         return userRepository.getUser(params)
 *             .onSuccess { user ->
 *                 preferencesRepository.cacheUser(user)
 *             }
 *     }
 * }
 *
 * // Usage
 * val result = getUserProfileUseCase("user-123")
 * ```
 *
 * For no-parameter use cases:
 * ```
 * class GetCurrentUserUseCase(
 *     private val userRepository: UserRepository
 * ) : BaseUseCase<Unit, User>() {
 *
 *     override suspend fun execute(params: Unit): Resource<User> {
 *         return userRepository.getCurrentUser()
 *     }
 * }
 *
 * // Usage
 * val result = getCurrentUserUseCase(Unit)
 * ```
 *
 * @param P Parameter type - use Unit for no parameters
 * @param R Return type (wrapped in Resource<R>)
 */
abstract class BaseUseCase<P, R>(
    protected val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) {
    /**
     * Execute the use case business logic
     * Subclasses must implement this to define their specific behavior
     *
     * @param params Input parameters for the use case
     * @return Resource<R> containing Success, Error, or Loading state
     */
    abstract suspend fun execute(params: P): Resource<R>

    /**
     * Operator invoke() allows calling the use case like a function
     * Example: useCase(params) instead of useCase.execute(params)
     *
     * @param params Input parameters
     * @return Resource<R> result
     */
    suspend operator fun invoke(params: P): Resource<R> = execute(params)

}

/**
 * Base class for use cases with no parameters
 *
 * Example:
 * ```
 * class RefreshUserCacheUseCase(
 *     private val userRepository: UserRepository
 * ) : NoParamUseCase<Unit>() {
 *
 *     override suspend fun execute(params: Unit): Resource<Unit> {
 *         userRepository.refreshCache()
 *         return Resource.Success(Unit)
 *     }
 * }
 * ```
 */
abstract class NoParamUseCase<R>(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseUseCase<Unit, R>(dispatcher) {
    suspend operator fun invoke(): Resource<R> = execute(Unit)
}
