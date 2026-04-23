package com.boatit.boatsharing.features.login.domain.usecase

import com.boatit.boatsharing.features.login.model.LoginResponse
import com.boatit.boatsharing.features.login.model.UserData
import com.boatit.boatsharing.features.login.repository.ILoginRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LoginUseCasesTest {
    @Test
    fun loginUserUseCase_returnsGatewayResult() =
        runBlocking {
            val repository =
                object : ILoginRepository {
                    override suspend fun login(
                        username: String,
                        password: String,
                    ): Result<LoginResponse> {
                        return Result.success(
                            LoginResponse(
                                Status = 200,
                                Message = "login ok",
                                obj =
                                    UserData(
                                        Email = username,
                                        Password = password,
                                        UserId = "u-1",
                                        Username = "User",
                                        Role = "Voyager",
                                        MissingStep = 0,
                                        accessToken = "acc",
                                        refreshToken = "ref",
                                    ),
                            ),
                        )
                    }
                }
            val useCase = LoginUserUseCase(repository)

            val result = useCase("user@example.com", "secret")

            assertTrue(result.isSuccess)
            assertEquals("login ok", result.getOrNull()?.message)
            assertEquals("user@example.com", result.getOrNull()?.user?.email)
        }

    @Test
    fun loginUserUseCase_invalidEmail_returnsFailureWithoutGatewayCall() =
        runBlocking {
            var called = false
            val repository =
                object : ILoginRepository {
                    override suspend fun login(
                        username: String,
                        password: String,
                    ): Result<LoginResponse> {
                        called = true
                        return Result.success(LoginResponse(Status = 200, Message = "login ok"))
                    }
                }
            val useCase = LoginUserUseCase(repository)

            val result = useCase("invalid-email", "secret123")

            assertTrue(result.isFailure)
            assertEquals("Enter a valid email address", result.exceptionOrNull()?.message)
            assertFalse(called)
        }

    @Test
    fun loginUserUseCase_trimsInputs_beforeCallingRepository() =
        runBlocking {
            var capturedUsername = ""
            var capturedPassword = ""

            val repository =
                object : ILoginRepository {
                    override suspend fun login(
                        username: String,
                        password: String,
                    ): Result<LoginResponse> {
                        capturedUsername = username
                        capturedPassword = password
                        return Result.success(
                            LoginResponse(
                                Status = 200,
                                Message = "ok",
                                obj =
                                    UserData(
                                        Email = username,
                                        Password = password,
                                        UserId = "u-1",
                                        Username = "User",
                                        Role = "Voyager",
                                        MissingStep = 0,
                                        accessToken = "acc",
                                        refreshToken = "ref",
                                    ),
                            ),
                        )
                    }
                }

            val useCase = LoginUserUseCase(repository)
            useCase("  user@example.com  ", "  secret123  ")

            assertEquals("user@example.com", capturedUsername)
            assertEquals("secret123", capturedPassword)
        }
}
