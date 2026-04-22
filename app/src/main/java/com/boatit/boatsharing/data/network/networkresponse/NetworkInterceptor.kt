package com.boatit.boatsharing.data.network.networkresponse

/**
 * Interceptor for request/response processing
 * Allows adding cross-cutting concerns without modifying repository code
 *
 * Typical uses:
 * - Logging
 * - Timing/metrics
 * - Request modification (adding headers, tokens)
 * - Response caching
 * - Error tracking
 */
interface NetworkInterceptor {
    /**
     * Called before request is sent
     *
     * @param endpoint API endpoint being called
     * @param request Request body (can be null)
     * @return Modified request or original
     */
    fun onRequest(
        endpoint: String,
        request: RequestModel?,
    ): RequestModel?

    /**
     * Called after successful response
     *
     * @param endpoint API endpoint that was called
     * @param response The response received
     * @return Modified response or original
     */
    fun onResponse(
        endpoint: String,
        response: ResponseModel<*>,
    ): ResponseModel<*>

    /**
     * Called when an error occurs
     *
     * @param endpoint API endpoint that failed
     * @param error The error/exception
     */
    fun onError(
        endpoint: String,
        error: Throwable,
    )
}

/**
 * Base implementation of NetworkInterceptor
 * Override only the methods you need
 */
abstract class BaseNetworkInterceptor : NetworkInterceptor {
    override fun onRequest(
        endpoint: String,
        request: RequestModel?,
    ): RequestModel? = request

    override fun onResponse(
        endpoint: String,
        response: ResponseModel<*>,
    ): ResponseModel<*> = response

    override fun onError(
        endpoint: String,
        error: Throwable,
    ) {
        // Override to handle errors
    }
}

/**
 * Composite interceptor that chains multiple interceptors
 * Useful for combining multiple behaviors
 *
 * Example:
 * ```
 * val chain = CompositeNetworkInterceptor(
 *     LoggingInterceptor(),
 *     CachingInterceptor(),
 *     MetricsInterceptor()
 * )
 * ```
 */
class CompositeNetworkInterceptor(
    private val interceptors: List<NetworkInterceptor>,
) : NetworkInterceptor {
    override fun onRequest(
        endpoint: String,
        request: RequestModel?,
    ): RequestModel? {
        var modifiedRequest = request
        for (interceptor in interceptors) {
            modifiedRequest = interceptor.onRequest(endpoint, modifiedRequest)
        }
        return modifiedRequest
    }

    override fun onResponse(
        endpoint: String,
        response: ResponseModel<*>,
    ): ResponseModel<*> {
        var modifiedResponse = response
        for (interceptor in interceptors) {
            modifiedResponse = interceptor.onResponse(endpoint, modifiedResponse)
        }
        return modifiedResponse
    }

    override fun onError(
        endpoint: String,
        error: Throwable,
    ) {
        for (interceptor in interceptors) {
            interceptor.onError(endpoint, error)
        }
    }
}
