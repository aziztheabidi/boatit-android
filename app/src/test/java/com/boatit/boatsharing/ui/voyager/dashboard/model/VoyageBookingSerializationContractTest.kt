package com.boatit.boatsharing.features.voyager.dashboard.model

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class VoyageBookingSerializationContractTest {
    private val json = Json { ignoreUnknownKeys = false }

    @Test
    fun bookVoyageRequest_serializesRequiredFieldsAndSponsorList() {
        val request =
            BookVoyageRequest(
                VoyagerUserId = "u-1",
                Name = "Trip",
                VoyageCategoryId = 2,
                PickupDockId = 10,
                DropOffDockId = 11,
                NoOfVoyagers = 3,
                IsImmediately = true,
                IsSplitPayment = false,
                BookingDate = "2026-04-05",
                StartTime = "10:00:00",
                IsStayOnWater = false,
                EndTime = "11:00:00",
                PerHourRate = 100.0,
                DurationInHours = 1.0,
                NoOfSponsers = 1,
                EstimatedCost = 100.0,
                IndvidualAmount = 100.0,
                Sponsers =
                    listOf(
                        Sponser(
                            VoyagerUserId = "u-2",
                            VoyagerUserName = "Alex",
                            AmountToPay = 100.0,
                            Status = "Pending",
                        ),
                    ),
            )

        val encoded = json.encodeToString(BookVoyageRequest.serializer(), request)

        assertTrue(encoded.contains("\"VoyagerUserId\":\"u-1\""))
        assertTrue(encoded.contains("\"Sponsers\":"))
        assertTrue(encoded.contains("\"VoyagerUserName\":\"Alex\""))
    }

    @Test
    fun bookVoyageResponse_deserializesContract() {
        val payload = """{"Status":201,"Message":"Booked","obj":"voy-1"}"""

        val response = json.decodeFromString(BookVoyageResponse.serializer(), payload)

        assertEquals(201, response.Status)
        assertEquals("Booked", response.Message)
        assertEquals("voy-1", response.obj)
    }

    @Test
    fun confirmBookedVoyage_models_serializeAndDeserializeContract() {
        val request = ConfirmBookedVoyages(Id = "voy-2")
        val requestEncoded = json.encodeToString(ConfirmBookedVoyages.serializer(), request)
        assertTrue(requestEncoded.contains("\"Id\":\"voy-2\""))

        val responsePayload = """{"Status":200,"Message":"Confirmed","obj":"ok"}"""
        val response = json.decodeFromString(ConfirmBookedVoyageResponse.serializer(), responsePayload)
        assertEquals(200, response.Status)
        assertEquals("Confirmed", response.Message)
        assertEquals("ok", response.obj)
    }

    @Test
    fun cancelBookedVoyage_models_serializeAndDeserializeContract() {
        val request = CancelBookedVoyages(Id = "voy-3", Reason = "schedule conflict")
        val requestEncoded = json.encodeToString(CancelBookedVoyages.serializer(), request)
        assertTrue(requestEncoded.contains("\"Reason\":\"schedule conflict\""))

        val responsePayload = """{"Status":200,"Message":"Canceled","obj":"ok"}"""
        val response = json.decodeFromString(CancelBookedVoyageResponse.serializer(), responsePayload)
        assertEquals(200, response.Status)
        assertEquals("Canceled", response.Message)
        assertEquals("ok", response.obj)
    }
}
