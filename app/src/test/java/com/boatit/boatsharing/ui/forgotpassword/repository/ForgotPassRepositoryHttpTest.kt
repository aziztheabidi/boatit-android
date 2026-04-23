package com.boatit.boatsharing.features.forgotpassword.repository

import com.boatit.boatsharing.testutils.httpClientReturningJson
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ForgotPassRepositoryHttpTest {
    @Test
    fun forgotPass_success_parsesPayload() =
        runBlocking {
            val json =
                """
                {
                  "Status": 200,
                  "Message": "sent",
                  "obj": "ok"
                }
                """.trimIndent()
            val repository = ForgotPassRepository(httpClientReturningJson(json))

            val result = repository.forgotPassResp("user@example.com")

            assertTrue(result.isSuccess)
            assertEquals("sent", result.getOrNull()?.message)
        }
}
