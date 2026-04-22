package com.boatit.boatsharing.features.forgotpassword.domain.usecase

import com.boatit.boatsharing.features.forgotpassword.domain.model.ForgotPasswordDomainModel
import com.boatit.boatsharing.features.forgotpassword.repository.IForgotPassRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ForgotPasswordUseCasesTest {
    @Test
    fun sendForgotPasswordUseCase_returnsGatewayResult() =
        runBlocking {
            val repository =
                object : IForgotPassRepository {
                    override suspend fun forgotPassResp(email: String): Result<ForgotPasswordDomainModel> {
                        return Result.success(ForgotPasswordDomainModel(status = 200, message = "sent", reference = "ok"))
                    }
                }
            val useCase = SendForgotPasswordUseCase(repository)

            val result = useCase("user@example.com")

            assertTrue(result.isSuccess)
            assertEquals("sent", result.getOrNull()?.message)
        }

    @Test
    fun sendForgotPasswordUseCase_invalidEmail_returnsFailureWithoutGatewayCall() =
        runBlocking {
            var called = false
            val repository =
                object : IForgotPassRepository {
                    override suspend fun forgotPassResp(email: String): Result<ForgotPasswordDomainModel> {
                        called = true
                        return Result.success(ForgotPasswordDomainModel(status = 200, message = "sent", reference = "ok"))
                    }
                }
            val useCase = SendForgotPasswordUseCase(repository)

            val result = useCase("bad-email")

            assertTrue(result.isFailure)
            assertEquals("Enter a valid email address", result.exceptionOrNull()?.message)
            assertFalse(called)
        }

    @Test
    fun sendForgotPasswordUseCase_trimsEmail_beforeCallingRepository() =
        runBlocking {
            var capturedEmail = ""
            val repository =
                object : IForgotPassRepository {
                    override suspend fun forgotPassResp(email: String): Result<ForgotPasswordDomainModel> {
                        capturedEmail = email
                        return Result.success(ForgotPasswordDomainModel(status = 200, message = "sent", reference = "ok"))
                    }
                }

            val useCase = SendForgotPasswordUseCase(repository)
            useCase("  user@example.com  ")

            assertEquals("user@example.com", capturedEmail)
        }
}
