package com.boatit.boatsharing.data.network.networkresponse

import com.boatit.boatsharing.domain.core.ErrorType

sealed class NetworkResponse<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : NetworkResponse<T>(data)

    class Error<T>(
        val errorType: ErrorType,
        data: T? = null,
    ) : NetworkResponse<T>(data, errorType.toMessage())

    class Loading<T> : NetworkResponse<T>()
}
