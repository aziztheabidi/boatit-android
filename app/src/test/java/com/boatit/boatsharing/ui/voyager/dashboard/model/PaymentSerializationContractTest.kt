package com.boatit.boatsharing.features.voyager.dashboard.model

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PaymentSerializationContractTest {
    private val json = Json { ignoreUnknownKeys = false }

    @Test
    fun paymentConfirmationRequest_serializesExpectedKeys() {
        val payload =
            PaymentConfirmationRequest(
                Id = "voy-1",
                PaymentIntentId = "pi_1",
                PaymentMethodId = "pm_1",
            )

        val encoded = json.encodeToString(PaymentConfirmationRequest.serializer(), payload)

        assertTrue(encoded.contains("\"Id\":\"voy-1\""))
        assertTrue(encoded.contains("\"PaymentIntentId\":\"pi_1\""))
        assertTrue(encoded.contains("\"PaymentMethodId\":\"pm_1\""))
    }

    @Test
    fun voyagePaymentResponse_deserializesNestedPaymentDetails() {
        val payload =
            """
            {
              "Status": 200,
              "Message": "ok",
              "obj": {
                "OTP": 1234,
                "CaptainName": "Captain A",
                "BoatName": "Harbor Star",
                "BoatModel": "Model X"
              }
            }
            """.trimIndent()

        val response = json.decodeFromString(VoyagePaymentResponse.serializer(), payload)

        assertEquals(200, response.Status)
        assertEquals("ok", response.Message)
        assertEquals(1234, response.obj?.OTP)
        assertEquals("Captain A", response.obj?.CaptainName)
    }

    @Test
    fun paymentSheetConfigResponse_deserializesAllFields() {
        val payload =
            """
            {
              "Status": 200,
              "Message": "sheet",
              "obj": {
                "ClientSecret": "cs_test",
                "CustomerId": "cus_1",
                "EphemeralKey": "ek_1",
                "EphemeralKey_Secret": "eks_1",
                "PublishableKey": "pk_test",
                "PaymentIntentId": "pi_1"
              }
            }
            """.trimIndent()

        val response = json.decodeFromString(PaymentSheetConfigResponse.serializer(), payload)

        assertEquals("cs_test", response.obj?.ClientSecret)
        assertEquals("pk_test", response.obj?.PublishableKey)
        assertEquals("pi_1", response.obj?.PaymentIntentId)
    }

    @Test
    fun sponsorPayments_deserializesListContract() {
        val payload =
            """
            {
              "Status": 200,
              "Message": "ok",
              "obj": [
                {
                  "Id": "voy-11",
                  "Name": "Sunset Voyage",
                  "VoyagerName": "Alex",
                  "VoyagerPhoneNumber": "12345",
                  "PickupDock": "Dock A",
                  "PickupDockLatitude": 1.1,
                  "PickupDockLongitude": 2.2,
                  "DropOffDock": "Dock B",
                  "DropOffDockLatitude": 3.3,
                  "DropOffDockLongitude": 4.4,
                  "AmountToPay": 55.5,
                  "NoOfVoyagers": 3,
                  "WaterStay": "No",
                  "Duration": "2h",
                  "BookingDateTime": "2026-04-05T12:00:00",
                  "VoyageStatus": "Accepted"
                }
              ]
            }
            """.trimIndent()

        val response = json.decodeFromString(SponsorPayments.serializer(), payload)

        assertEquals(200, response.Status)
        assertEquals(1, response.obj.size)
        assertEquals("voy-11", response.obj.first().Id)
        assertEquals(55.5, response.obj.first().AmountToPay, 0.0)
    }
}
