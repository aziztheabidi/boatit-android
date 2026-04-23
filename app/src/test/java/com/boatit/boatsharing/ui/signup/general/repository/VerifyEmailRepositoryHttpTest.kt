package com.boatit.boatsharing.features.signup.general.repository

import com.boatit.boatsharing.testutils.httpClientReturningJson
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class VerifyEmailRepositoryHttpTest {
    @Test
    fun verifyEmail_success_parsesPayload() =
        runBlocking {
            val json =
                """
                {
                  "Status": 200,
                  "Message": "verified",
                  "obj": "token-123"
                }
                """.trimIndent()
            val repository = VerifyEmailRepository(httpClientReturningJson(json))

            val result = repository.verifyEmail("user@example.com", "1234")

            assertTrue(result.isSuccess)
            assertEquals("verified", result.getOrNull()?.Message)
        }
}
