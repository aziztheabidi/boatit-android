package com.boatit.boatsharing.application.viewmodel

import android.content.Intent
import android.content.RestrictionsManager.RESULT_ERROR
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import com.boatit.boatsharing.core.presentation.BaseViewModel
import com.boatit.boatsharing.core.presentation.UiEffect
import com.boatit.boatsharing.core.presentation.UiEvent
import com.boatit.boatsharing.core.presentation.UiState
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult

class StripeSheetViewModel :
    BaseViewModel<StripeSheetUiState, StripeSheetUiEvent, StripeSheetUiEffect>(StripeSheetUiState()) {
    override fun onEvent(event: StripeSheetUiEvent) = Unit

    fun parseArgs(intent: Intent): StripeSheetArgs? {
        val publishableKey = intent.getStringExtra("publishableKey")
        val paymentIntentClientSecret = intent.getStringExtra("ClientSecret").orEmpty()
        val customerId = intent.getStringExtra("customerId")
        val ephemeralKey = intent.getStringExtra("ephemeralKey")
        if (publishableKey.isNullOrBlank() ||
            paymentIntentClientSecret.isBlank() ||
            customerId.isNullOrBlank() ||
            ephemeralKey.isNullOrBlank()
        ) {
            return null
        }

        return StripeSheetArgs(
            publishableKey = publishableKey,
            paymentIntentClientSecret = paymentIntentClientSecret,
            customerConfig =
                PaymentSheet.CustomerConfiguration(
                    id = customerId,
                    ephemeralKeySecret = ephemeralKey,
                ),
        )
    }

    fun mapResultCode(paymentSheetResult: PaymentSheetResult): Int =
        when (paymentSheetResult) {
            is PaymentSheetResult.Canceled -> RESULT_CANCELED
            is PaymentSheetResult.Failed -> RESULT_ERROR
            is PaymentSheetResult.Completed -> RESULT_OK
        }
}

data class StripeSheetArgs(
    val publishableKey: String,
    val paymentIntentClientSecret: String,
    val customerConfig: PaymentSheet.CustomerConfiguration,
)

data class StripeSheetUiState(val initialized: Boolean = false) : UiState

sealed interface StripeSheetUiEvent : UiEvent

sealed interface StripeSheetUiEffect : UiEffect
