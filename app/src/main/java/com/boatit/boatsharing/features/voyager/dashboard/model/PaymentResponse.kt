package com.boatit.boatsharing.features.voyager.dashboard.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class VoyagePaymentRequest(
    val Id: String,
)

@Serializable
data class SponsorVoyagePaymentRequest(
    val Id: String,
    val VoyagerUserId: String,
    @SerialName("SponserUserId")
    val sponsorUserId: String,
)

@Serializable
data class PaymentConfirmationRequest(
    val Id: String,
    val PaymentIntentId: String,
    val PaymentMethodId: String,
)

@Serializable
data class VoyagePaymentResponse(
    val Status: Int,
    val Message: String,
    val obj: PaymentDetails? = null,
)

@Serializable
data class PaymentDetails(
    val OTP: Int,
    val CaptainName: String,
    val BoatName: String,
    val BoatModel: String,
)

@Serializable
data class PaymentSheetConfigResponse(
    val Status: Int,
    val Message: String,
    val obj: PaymentSheetConfig? = null,
)

@Serializable
data class PaymentSheetConfig(
    val ClientSecret: String,
    val CustomerId: String,
    val EphemeralKey: String,
    val EphemeralKey_Secret: String,
    val PublishableKey: String,
    val PaymentIntentId: String,
)
