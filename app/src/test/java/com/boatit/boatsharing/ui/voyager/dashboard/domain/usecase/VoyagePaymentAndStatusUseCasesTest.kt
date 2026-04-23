package com.boatit.boatsharing.features.voyager.dashboard.domain.usecase

import com.boatit.boatsharing.features.captain.dashboard.model.DeclineRequest
import com.boatit.boatsharing.features.voyager.dashboard.model.PaymentSheetConfig
import com.boatit.boatsharing.features.voyager.dashboard.model.PaymentSheetConfigResponse
import com.boatit.boatsharing.features.voyager.dashboard.model.SponsorVoyagePaymentRequest
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class VoyagePaymentAndStatusUseCasesTest {
    @Test
    fun paymentSheetUseCases_returnGatewayResults() =
        runBlocking {
            val configResponse =
                PaymentSheetConfigResponse(
                    Status = 200,
                    Message = "ok",
                    obj =
                        PaymentSheetConfig(
                            ClientSecret = "c",
                            CustomerId = "cust",
                            EphemeralKey = "k",
                            EphemeralKey_Secret = "ks",
                            PublishableKey = "pk",
                            PaymentIntentId = "pi",
                        ),
                )
            val paymentConfigUseCase = FetchPaymentSheetConfigUseCase { Result.success(configResponse) }
            val sponsorConfigUseCase = FetchSponsorPaymentSheetConfigUseCase { Result.success(configResponse) }
            val declineUseCase = DeclineSponsorPaymentUseCase { Result.success(configResponse.copy(Message = "declined")) }

            val paymentResult = paymentConfigUseCase("voy-1")
            val sponsorResult =
                sponsorConfigUseCase(
                    SponsorVoyagePaymentRequest(Id = "voy-1", VoyagerUserId = "u-1", sponsorUserId = "u-2"),
                )
            val declineResult = declineUseCase(DeclineRequest(Id = "voy-1"))

            assertTrue(paymentResult.isSuccess)
            assertEquals("ok", paymentResult.getOrNull()?.Message)
            assertTrue(sponsorResult.isSuccess)
            assertEquals("ok", sponsorResult.getOrNull()?.Message)
            assertTrue(declineResult.isSuccess)
            assertEquals("declined", declineResult.getOrNull()?.Message)
        }
}
