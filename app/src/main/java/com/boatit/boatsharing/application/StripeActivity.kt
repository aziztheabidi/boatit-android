package com.boatit.boatsharing.application

import android.content.Intent
import android.content.RestrictionsManager.RESULT_ERROR
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import com.boatit.boatsharing.application.viewmodel.StripeSheetViewModel
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult

class StripeSheetActivity : ComponentActivity() {
    private val stripeSheetViewModel: StripeSheetViewModel by viewModels()
    lateinit var paymentSheet: PaymentSheet
    private lateinit var paymentIntentClientSecret: String
    private lateinit var customerConfig: PaymentSheet.CustomerConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        paymentSheet = PaymentSheet(this, ::onPaymentSheetResult)
        val args = stripeSheetViewModel.parseArgs(intent)

        if (args == null) {
            setResult(RESULT_ERROR)
            finish()
            return
        }

        paymentIntentClientSecret = args.paymentIntentClientSecret
        customerConfig = args.customerConfig
        PaymentConfiguration.init(this, args.publishableKey)
        presentPaymentSheet()
    }

    fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
        setResult(stripeSheetViewModel.mapResultCode(paymentSheetResult))
        when (paymentSheetResult) {
            is PaymentSheetResult.Canceled -> {
                Toast.makeText(this, "$paymentSheetResult", Toast.LENGTH_LONG).show()
                finish()
            }
            is PaymentSheetResult.Failed -> {
                Toast.makeText(this, "$paymentSheetResult", Toast.LENGTH_LONG).show()
                finish()
            }
            is PaymentSheetResult.Completed -> {
                finish()
            }
        }
    }

    fun presentPaymentSheet() {
        paymentSheet.presentWithPaymentIntent(
            paymentIntentClientSecret,
            PaymentSheet.Configuration(
                merchantDisplayName = "Your Merchant Name",
                customer = customerConfig,
                allowsDelayedPaymentMethods = true,
            ),
        )
    }

    public override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
    ) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}
