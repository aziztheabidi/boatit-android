package com.boatit.boatsharing.application

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.boatit.boatsharing.routes.AppNavGraph
import com.boatit.boatsharing.utils.theme.BoatSharingAppTheme
import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.RestrictionsManager.RESULT_ERROR
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.boatit.boatsharing.ui.login.viewmodel.NotificationViewModel
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class StripeSheetActivity : ComponentActivity() {

    lateinit var paymentSheet: PaymentSheet
    private lateinit var paymentIntentClientSecret: String
    private lateinit var customerConfig: PaymentSheet.CustomerConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        paymentSheet = PaymentSheet(this, ::onPaymentSheetResult)
        paymentSheet = PaymentSheet(this, ::onPaymentSheetResult)
        val publishableKey = getIntent().getStringExtra("publishableKey")
        paymentIntentClientSecret = getIntent().getStringExtra("ClientSecret")!!
        val customerId = getIntent().getStringExtra("customerId")
        val ephemeralKey = getIntent().getStringExtra("ephemeralKey")
//        val publishableKey = "pk_test_51N8B6wIiYO00MT0yE2hZ0oQEf1VyHKzAtZyGuiFCRrx8eo5swxsYKzBKBNEGWuO4hzqHnHCzX9EYBJDLt1mmmsX000BtNUImoB"
//        paymentIntentClientSecret = "pi_3RJadkIiYO00MT0y1BsUCY5v_secret_dsd0PGLz3zkClbfj3h8ldgW64"
//        val customerId = "cus_RuWb1Rjvzm01Nk"
//        val ephemeralKey = "ek_test_YWNjdF8xTjhCNndJaVlPMDBNVDB5LFV1dHR3Q1hiTUVjYTRrcW5MQTNJQnI3UUg5VTlvWjc_005314m3gj"
        customerConfig = PaymentSheet.CustomerConfiguration(
            id = customerId!!, // From backend
            ephemeralKeySecret = ephemeralKey!! // From backend
        )
        PaymentConfiguration.init(this, publishableKey!!)
        presentPaymentSheet()
    }

    fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
        when(paymentSheetResult) {
            is PaymentSheetResult.Canceled -> {
                setResult(RESULT_CANCELED)
                Toast.makeText(this, "${paymentSheetResult.toString()}", Toast.LENGTH_LONG).show()
                finish()
            }
            is PaymentSheetResult.Failed -> {
                setResult(RESULT_ERROR)
                Toast.makeText(this, "${paymentSheetResult.toString()}", Toast.LENGTH_LONG).show()
                println("Error: ${paymentSheetResult.toString()}")
                finish()
            }
            is PaymentSheetResult.Completed -> {
                setResult(RESULT_OK)
                finish()
            }
        }
    }

    fun presentPaymentSheet() {
        paymentSheet.presentWithPaymentIntent (
            paymentIntentClientSecret,
            PaymentSheet.Configuration(
                merchantDisplayName = "Your Merchant Name",
                customer = customerConfig,
                allowsDelayedPaymentMethods = true
            )
        )
    }

    override public fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}
