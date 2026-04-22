package com.boatit.boatsharing.features.signup.captain.domain.usecase

import com.boatit.boatsharing.features.signup.captain.model.CaptainBoat
import com.boatit.boatsharing.features.signup.captain.model.CaptainDocument
import com.boatit.boatsharing.features.signup.captain.model.CaptainProfile
import com.boatit.boatsharing.features.signup.captain.model.CaptainProfileRequest
import com.boatit.boatsharing.features.signup.captain.model.CaptainProfileResponse
import com.boatit.boatsharing.features.signup.captain.model.GetCaptainBoatResponse
import com.boatit.boatsharing.features.signup.captain.model.GetCaptainDocumentResponse
import com.boatit.boatsharing.features.signup.captain.model.GetCaptainProfileResponse
import com.boatit.boatsharing.features.signup.captain.model.SaveCaptainBoatRequest
import com.boatit.boatsharing.features.signup.captain.model.SaveCaptainBoatResponse
import com.boatit.boatsharing.features.signup.captain.model.SaveCaptainDocumentRequest
import com.boatit.boatsharing.features.signup.captain.model.SaveCaptainDocumentResponse
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CaptainSignupUseCasesTest {
    @Test
    fun saveCaptainUseCases_returnGatewayResults() =
        runBlocking {
            val saveProfileUseCase =
                SaveCaptainProfileUseCase {
                    Result.success(CaptainProfileResponse(Status = 200, Message = "profile saved"))
                }
            val saveDocsUseCase =
                SaveCaptainDocsUseCase {
                    Result.success(SaveCaptainDocumentResponse(Status = 200, Message = "docs saved"))
                }
            val saveBoatUseCase =
                SaveCaptainBoatUseCase {
                    Result.success(SaveCaptainBoatResponse(Status = 200, Message = "boat saved"))
                }

            val profileResult = saveProfileUseCase(sampleProfileRequest())
            val docsResult = saveDocsUseCase(sampleDocsRequest())
            val boatResult = saveBoatUseCase(sampleBoatRequest())

            assertTrue(profileResult.isSuccess)
            assertEquals("profile saved", profileResult.getOrNull()?.Message)
            assertTrue(docsResult.isSuccess)
            assertEquals("docs saved", docsResult.getOrNull()?.Message)
            assertTrue(boatResult.isSuccess)
            assertEquals("boat saved", boatResult.getOrNull()?.Message)
        }

    @Test
    fun fetchCaptainUseCases_returnGatewayData() =
        runBlocking {
            val fetchProfileUseCase =
                FetchCaptainProfileUseCase {
                    Result.success(
                        GetCaptainProfileResponse(
                            Status = 200,
                            Message = "ok",
                            obj = sampleProfile(),
                        ),
                    )
                }
            val fetchDocsUseCase =
                FetchCaptainDocsUseCase {
                    Result.success(
                        GetCaptainDocumentResponse(
                            Status = 200,
                            Message = "ok",
                            obj = sampleDocument(),
                        ),
                    )
                }
            val fetchBoatUseCase =
                FetchCaptainBoatUseCase {
                    Result.success(
                        GetCaptainBoatResponse(
                            Status = 200,
                            Message = "ok",
                            obj = sampleBoat(),
                        ),
                    )
                }

            val profileResult = fetchProfileUseCase()
            val docsResult = fetchDocsUseCase()
            val boatResult = fetchBoatUseCase()

            assertTrue(profileResult.isSuccess)
            assertEquals("Captain", profileResult.getOrNull()?.obj?.FirstName)
            assertTrue(docsResult.isSuccess)
            assertEquals("LIC-1", docsResult.getOrNull()?.obj?.LicenseNumber)
            assertTrue(boatResult.isSuccess)
            assertEquals("Boat", boatResult.getOrNull()?.obj?.Name)
        }

    @Test
    fun fetchCaptainBoatUseCase_propagatesFailure() =
        runBlocking {
            val useCase =
                FetchCaptainBoatUseCase {
                    Result.failure(Exception("fetch captain boat failed"))
                }

            val result = useCase()

            assertTrue(result.isFailure)
            assertEquals("fetch captain boat failed", result.exceptionOrNull()?.message)
        }

    private fun sampleProfileRequest(): CaptainProfileRequest {
        return CaptainProfileRequest(
            UserId = "cap-1",
            PhoneNumber = "000000000",
            FirstName = "Captain",
            LastName = "One",
            Address = "A",
            DateOfBirth = "1990-01-01",
            StripeEmail = "cap@example.com",
        )
    }

    private fun sampleDocsRequest(): SaveCaptainDocumentRequest {
        return SaveCaptainDocumentRequest(
            UserId = "cap-1",
            LicenseNumber = "LIC-1",
            LicenseExpiration = "2030-01-01",
            TypeOfLicense = "Type",
            InsuranceCompany = "Insure",
            PolicyNumber = "POL-1",
            PolicyExpiration = "2030-01-01",
        )
    }

    private fun sampleBoatRequest(): SaveCaptainBoatRequest {
        return SaveCaptainBoatRequest(
            UserId = "cap-1",
            Name = "Boat",
            Make = "Make",
            Model = "Model",
            Year = 2020,
            Size = 20,
            Capacity = 8,
        )
    }

    private fun sampleProfile(): CaptainProfile {
        return CaptainProfile(
            PhoneNumber = "000000000",
            FirstName = "Captain",
            LastName = "One",
            Address = "A",
            DateOfBirth = "1990-01-01",
            StripeEmail = "cap@example.com",
            UserId = "cap-1",
            ChangedOn = "2026-04-05",
            ChangedBy = "cap-1",
        )
    }

    private fun sampleDocument(): CaptainDocument {
        return CaptainDocument(
            LicenseNumber = "LIC-1",
            LicenseExpiration = "2030-01-01",
            TypeOfLicense = "Type",
            InsuranceCompany = "Insure",
            PolicyNumber = "POL-1",
            PolicyExpiration = "2030-01-01",
            UserId = "cap-1",
            ChangedOn = "2026-04-05",
            ChangedBy = "cap-1",
        )
    }

    private fun sampleBoat(): CaptainBoat {
        return CaptainBoat(
            Name = "Boat",
            Make = "Make",
            Model = "Model",
            Year = 2020,
            Size = 20,
            Capacity = 8,
            UserId = "cap-1",
            ChangedOn = "2026-04-05",
            ChangedBy = "cap-1",
        )
    }
}
