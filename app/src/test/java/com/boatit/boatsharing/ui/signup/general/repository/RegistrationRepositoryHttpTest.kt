package com.boatit.boatsharing.features.signup.general.repository

import com.boatit.boatsharing.testutils.httpClientReturningJson
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RegistrationRepositoryHttpTest {
    @Test
    fun tempRegister_success_parsesPayload() =
        runBlocking {
            val json =
                """
                {
                  "Status": 200,
                  "Message": "registered",
                  "obj": "user@example.com"
                }
                """.trimIndent()
            val repository = RegistrationRepository(httpClientReturningJson(json))

            val result = repository.tempRegister("Ali", "123", "user@example.com")

            assertTrue(result.isSuccess)
            assertEquals("registered", result.getOrNull()?.Message)
        }
}
