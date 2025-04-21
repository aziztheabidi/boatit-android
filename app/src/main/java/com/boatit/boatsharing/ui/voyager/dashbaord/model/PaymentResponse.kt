package com.boatit.boatsharing.ui.voyager.dashbaord.model

import kotlinx.serialization.Serializable

@Serializable
data class VoyagePaymentRequest(
    val Id: String
)

@Serializable
data class VoyagePaymentResponse(
    val Status: Int,
    val Message: String,
    val obj: PaymentDetails? = null
)

@Serializable
data class PaymentDetails(
    val OTP: Int,
    val CaptainName: String,
    val BoatName: String,
    val BoatModel: String
)

@Serializable
data class PaymentSheetConfigResponse(
    val Status: Int,
    val Message: String,
    val obj: PaymentSheetConfig? = null
)

@Serializable
data class PaymentSheetConfig(
    val ClientSecret: String,
    val CustomerId: String,
    val EphemeralKey: String,
    val PublishableKey: String
)