package com.boatit.boatsharing.features.captain.dashboard.repository

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CaptainActiveVoyagesRepositoryHttpTest {
    @Test
    fun voyages_success_parsesPayload() =
        runBlocking {
            val engine =
                MockEngine {
                    respond(
                        content =
                            """
                            {
                              "Status": 200,
                              "Message": "ok",
                              "obj": {
                                "Pending": [
                                  {
                                    "Id": "p-1",
                                    "Name": "Trip",
                                    "VoyagerUserId": "voy-1",
                                    "VoyagerName": "Voyager",
                                    "VoyagerPhoneNumber": "000",
                                    "PickupDock": "A",
                                    "PickupDockLatitude": 0.0,
                                    "PickupDockLongitude": 0.0,
                                    "DropOffDock": "B",
                                    "DropOffDockLatitude": 1.0,
                                    "DropOffDockLongitude": 1.0,
                                    "NoOfVoyager": 2,
                                    "BookingDateTime": "2026-04-16",
                                    "AmountToPay": 20.0,
                                    "WaterStay": "No",
                                    "Duration": "1h"
                                  }
                                ],
                                "Accepted": [],
                                "Started": []
                              }
                            }
                            """.trimIndent(),
                        status = HttpStatusCode.OK,
                        headers = headersOf("Content-Type", ContentType.Application.Json.toString()),
                    )
                }

            val client =
                HttpClient(engine) {
                    install(ContentNegotiation) {
                        json()
                    }
                }

            val repository = CaptainActiveVoyagesRepository(client, baseUrl = "https://unit.test")
            val result = repository.voyages()

            assertTrue(result.isSuccess)
            assertEquals("ok", result.getOrNull()?.Message)
            assertEquals(1, result.getOrNull()?.obj?.Pending?.size)
        }
}
