package com.boatit.boatsharing.features.signup.general.domain.usecase

import com.boatit.boatsharing.features.login.model.LoginResponse
import com.boatit.boatsharing.features.login.model.UserData
import com.boatit.boatsharing.features.signup.general.model.GetVoyagerProfileResponse
import com.boatit.boatsharing.features.signup.general.model.RegistrationResponse
import com.boatit.boatsharing.features.signup.general.model.VerifyEmailResponse
import com.boatit.boatsharing.features.signup.general.model.VoyagerProfileData
import com.boatit.boatsharing.features.signup.general.model.VoyagerProfileRequest
import com.boatit.boatsharing.features.signup.general.model.VoyagerProfileResponse
import com.boatit.boatsharing.features.signup.general.repository.IGetVoyagerProfileRepository
import com.boatit.boatsharing.features.signup.general.repository.IPasswordRepository
import com.boatit.boatsharing.features.signup.general.repository.IRegistrationRepository
import com.boatit.boatsharing.features.signup.general.repository.IVerifyEmailRepository
import com.boatit.boatsharing.features.signup.general.repository.IVoyagerProfileRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class GeneralSignupUseCasesTest {
    @Test
    fun registerAndVerifyUseCases_returnGatewayResults() =
        runBlocking {
            val registerUseCase =
                RegisterUserUseCase(
                    object : IRegistrationRepository {
                        override suspend fun tempRegister(
                            username: String,
                            phoneNumber: String,
                            email: String,
                        ): Result<RegistrationResponse> {
                            return Result.success(RegistrationResponse(Status = 200, Message = "registered", obj = email))
                        }
                    },
                )
            val verifyUseCase =
                VerifySignupEmailUseCase(
                    object : IVerifyEmailRepository {
                        override suspend fun verifyEmail(
                            email: String,
                            otp: String,
                        ): Result<VerifyEmailResponse> {
                            return Result.success(VerifyEmailResponse(Status = 200, Message = "verified", obj = "ok"))
                        }
                    },
                )

            val registerResult = registerUseCase("Ali", "000", "a@a.com")
            val verifyResult = verifyUseCase("a@a.com", "1234")

            assertTrue(registerResult.isSuccess)
            assertEquals("registered", registerResult.getOrNull()?.message)
            assertTrue(verifyResult.isSuccess)
            assertEquals("verified", verifyResult.getOrNull()?.message)
        }

    @Test
    fun registerUseCase_invalidEmail_returnsFailure() =
        runBlocking {
            var called = false
            val registerUseCase =
                RegisterUserUseCase(
                    object : IRegistrationRepository {
                        override suspend fun tempRegister(
                            username: String,
                            phoneNumber: String,
                            email: String,
                        ): Result<RegistrationResponse> {
                            called = true
                            return Result.success(RegistrationResponse(Status = 200, Message = "registered", obj = "ok"))
                        }
                    },
                )

            val result = registerUseCase("Ali", "123", "invalid-email")

            assertTrue(result.isFailure)
            assertEquals("Enter a valid email address", result.exceptionOrNull()?.message)
            assertFalse(called)
        }

    @Test
    fun verifyUseCase_invalidOtp_returnsFailure() =
        runBlocking {
            var called = false
            val verifyUseCase =
                VerifySignupEmailUseCase(
                    object : IVerifyEmailRepository {
                        override suspend fun verifyEmail(
                            email: String,
                            otp: String,
                        ): Result<VerifyEmailResponse> {
                            called = true
                            return Result.success(VerifyEmailResponse(Status = 200, Message = "verified", obj = "ok"))
                        }
                    },
                )

            val result = verifyUseCase("a@a.com", "12")

            assertTrue(result.isFailure)
            assertEquals("OTP must be 4 to 6 digits", result.exceptionOrNull()?.message)
            assertFalse(called)
        }

    @Test
    fun registerPasswordUseCase_weakPassword_returnsFailure() =
        runBlocking {
            var called = false
            val useCase =
                RegisterPasswordUseCase(
                    object : IPasswordRepository {
                        override suspend fun passwordRepository(
                            password: String,
                            token: String,
                        ): Result<LoginResponse> {
                            called = true
                            return Result.success(
                                LoginResponse(
                                    Status = 200,
                                    Message = "ok",
                                    obj =
                                        UserData(
                                            Email = "a@a.com",
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
                    },
                )

            val result = useCase("weakpwd", "token")

            assertTrue(result.isFailure)
            assertEquals(
                "Password must be at least 8 characters and include a number and symbol",
                result.exceptionOrNull()?.message,
            )
            assertFalse(called)
        }

    @Test
    fun voyagerProfileUseCases_returnGatewayResults() =
        runBlocking {
            val saveUseCase =
                SaveVoyagerProfileUseCase(
                    object : IVoyagerProfileRepository {
                        override suspend fun saveVoyagerProfile(profile: VoyagerProfileRequest): Result<VoyagerProfileResponse> {
                            return Result.success(VoyagerProfileResponse(Status = 200, Message = "saved"))
                        }
                    },
                )
            val fetchUseCase =
                FetchVoyagerProfileUseCase(
                    object : IGetVoyagerProfileRepository {
                        override suspend fun getVoyagerProfile(): Result<GetVoyagerProfileResponse> {
                            return Result.success(
                                GetVoyagerProfileResponse(
                                    Status = 200,
                                    Message = "ok",
                                    obj =
                                        VoyagerProfileData(
                                            UserId = "voy-1",
                                            PhoneNumber = "000",
                                            FirstName = "Voyager",
                                            LastName = "One",
                                            Address = "A",
                                            DateOfBirth = "2000-01-01",
                                            StripeEmail = "voy@example.com",
                                            ChangedOn = "2026-04-06",
                                            ChangedBy = "voy-1",
                                        ),
                                ),
                            )
                        }
                    },
                )

            val saveResult =
                saveUseCase(
                    VoyagerProfileRequest(
                        UserId = "voy-1",
                        PhoneNumber = "000",
                        FirstName = "Voyager",
                        LastName = "One",
                        Address = "A",
                        DateOfBirth = "2000-01-01",
                        StripeEmail = "voy@example.com",
                    ),
                )
            val fetchResult = fetchUseCase()

            assertTrue(saveResult.isSuccess)
            assertEquals("saved", saveResult.getOrNull()?.message)
            assertTrue(fetchResult.isSuccess)
            assertEquals("voy-1", fetchResult.getOrNull()?.userId)
        }

    @Test
    fun saveVoyagerProfileUseCase_invalidPayoutEmail_returnsFailure() =
        runBlocking {
            var called = false
            val useCase =
                SaveVoyagerProfileUseCase(
                    object : IVoyagerProfileRepository {
                        override suspend fun saveVoyagerProfile(profile: VoyagerProfileRequest): Result<VoyagerProfileResponse> {
                            called = true
                            return Result.success(VoyagerProfileResponse(Status = 200, Message = "saved"))
                        }
                    },
                )

            val result =
                useCase(
                    VoyagerProfileRequest(
                        UserId = "voy-1",
                        PhoneNumber = "000",
                        FirstName = "Voyager",
                        LastName = "One",
                        Address = "A",
                        DateOfBirth = "2000-01-01",
                        StripeEmail = "not-an-email",
                    ),
                )

            assertTrue(result.isFailure)
            assertEquals("Enter a valid payout email", result.exceptionOrNull()?.message)
            assertFalse(called)
        }
}
