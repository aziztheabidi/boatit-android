package com.boatit.boatsharing.features.forgotpassword.repository

import com.boatit.boatsharing.testutils.apiExecutorReturningJson
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
            val repository = ForgotPassRepository(apiExecutorReturningJson(json))

            val result = repository.forgotPassResp("user@example.com")

            assertTrue(result.isSuccess)
            assertEquals("sent", result.getOrNull()?.message)
        }
}
