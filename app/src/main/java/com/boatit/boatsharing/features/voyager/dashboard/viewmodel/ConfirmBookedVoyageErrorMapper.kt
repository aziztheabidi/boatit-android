package com.boatit.boatsharing.features.voyager.dashboard.viewmodel

import com.boatit.boatsharing.domain.core.BusinessErrorCodes
import com.boatit.boatsharing.domain.core.ErrorType

internal fun mapConfirmBookedVoyageError(error: ErrorType): ErrorType {
    val text = error.toMessage()
    return if (text.contains("Pay Completely", ignoreCase = true)) {
        ErrorType.Validation(
            message = text,
            field = BusinessErrorCodes.PAY_BEFORE_CONFIRM,
        )
    } else {
        error
    }
}
