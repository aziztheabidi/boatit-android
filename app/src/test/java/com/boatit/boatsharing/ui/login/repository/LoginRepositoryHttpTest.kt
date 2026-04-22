package com.boatit.boatsharing.features.login.repository

import com.boatit.boatsharing.testutils.apiExecutorReturningJson
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LoginRepositoryHttpTest {
    @Test
    fun login_success_parsesPayload() =
        runBlocking {
            val json =
                """
                {
                  "Status": 200,
                  "Message": "ok",
                  "obj": {
                    "Email": "user@example.com",
                    "Password": "secret123",
                    "UserId": "u-1",
                    "Username": "User",
                    "Role": "Voyager",
                    "MissingStep": 0,
                    "Accesstoken": "acc",
                    "Refreshtoken": "ref"
                  }
                }
                """.trimIndent()
            val repository = LoginRepository(apiExecutorReturningJson(json))

            val result = repository.login("user@example.com", "secret123")

            assertTrue(result.isSuccess)
            assertEquals("ok", result.getOrNull()?.Message)
            assertEquals("u-1", result.getOrNull()?.obj?.UserId)
        }
}
