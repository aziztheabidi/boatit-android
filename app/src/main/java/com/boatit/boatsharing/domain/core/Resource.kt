package com.boatit.boatsharing.domain.core

sealed class Resource<out T> {
    data class Success<T>(val data: T) : Resource<T>()

    data class Error(val error: ErrorType) : Resource<Nothing>()

    data object Loading : Resource<Nothing>()

    val isSuccess: Boolean get() = this is Success
    val isError: Boolean get() = this is Error
    val isLoading: Boolean get() = this is Loading

    fun getOrNull(): T? =
        when (this) {
            is Success -> data
            else -> null
        }

    inline fun <R> map(transform: (T) -> R): Resource<R> =
        when (this) {
            is Success -> Success(transform(data))
            is Error -> this
            is Loading -> this
        }

    inline fun onSuccess(action: (T) -> Unit): Resource<T> {
        if (this is Success) action(data)
        return this
    }

    inline fun onError(action: (ErrorType) -> Unit): Resource<T> {
        if (this is Error) action(error)
        return this
    }
}

sealed class ErrorType {
    data class Network(val message: String, val code: Int? = null) : ErrorType()

    data class Server(val message: String, val code: Int) : ErrorType()

    data class Validation(val message: String, val field: String? = null) : ErrorType()

    data class Authentication(val message: String) : ErrorType()

    data class Unknown(val message: String, val throwable: Throwable? = null) : ErrorType()

    fun toMessage(): String =
        when (this) {
            is Network -> message
            is Server -> message
            is Validation -> message
            is Authentication -> message
            is Unknown -> message
        }
}

/** True when the user must sign in again (session invalid / unauthorized). */
fun ErrorType.requiresReLogin(): Boolean =
    when (this) {
        is ErrorType.Authentication -> true
        is ErrorType.Network -> code == 401
        else -> false
    }

data class UiError(
    val message: String,
    val actionLabel: String? = null,
    val action: (() -> Unit)? = null,
)

fun ErrorType.toUiError(): UiError =
    when (this) {
        is ErrorType.Network -> UiError(message = "Network error: $message", actionLabel = "Retry")
        is ErrorType.Server -> UiError(message = "Server error: $message")
        is ErrorType.Validation -> UiError(message = message)
        is ErrorType.Authentication -> UiError(message = "Authentication failed: $message", actionLabel = "Login")
        is ErrorType.Unknown -> UiError(message = "An unexpected error occurred: $message")
    }

fun Throwable.toErrorType(): ErrorType =
    when (this) {
        is AuthenticationException ->
            ErrorType.Authentication(message ?: "Unauthorized")
        is AuthorizationException ->
            ErrorType.Network(message ?: "Forbidden", code = 403)
        is ValidationException -> {
            val ex = this
            ErrorType.Validation(ex.message, field = ex.field)
        }
        is NotFoundException ->
            ErrorType.Network(message ?: "Not found", code = 404)
        is ConflictException ->
            ErrorType.Server(message ?: "Conflict", code = 409)
        is RateLimitException ->
            ErrorType.Server(message, code = 429)
        is TimeoutException ->
            ErrorType.Network(message ?: "Request timeout", code = null)
        is ServerException ->
            ErrorType.Server(message ?: "Server error", code = code ?: 500)
        is ApiException ->
            when (code) {
                401 -> ErrorType.Authentication(serverMessage ?: message)
                in 500..599 -> ErrorType.Server(serverMessage ?: message, code)
                in 400..499 -> ErrorType.Network(serverMessage ?: message, code)
                else -> ErrorType.Unknown(message, this)
            }
        is NetworkException ->
            ErrorType.Network(message ?: "Network error", code = null)
        is DomainException ->
            ErrorType.Unknown(message ?: "Unexpected error", this)
        else ->
            ErrorType.Unknown(
                message = message ?: localizedMessage ?: "Unknown error occurred",
                throwable = this,
            )
    }

fun <T> Result<T>.toResource(): Resource<T> =
    fold(
        onSuccess = { Resource.Success(it) },
        onFailure = { Resource.Error(it.toErrorType()) },
    )
