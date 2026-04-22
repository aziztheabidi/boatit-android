package com.boatit.boatsharing.features.signup.business.domain.usecase

import com.boatit.boatsharing.features.signup.business.model.BusinessInfoData
import com.boatit.boatsharing.features.signup.business.model.BusinessInfoRequest
import com.boatit.boatsharing.features.signup.business.model.BusinessInfoResponse
import com.boatit.boatsharing.features.signup.business.model.BusinessProfileData
import com.boatit.boatsharing.features.signup.business.model.BusinessProfileRequest
import com.boatit.boatsharing.features.signup.business.model.GetBusinessProfileResponse
import com.boatit.boatsharing.features.signup.business.model.SaveBusinessAboutRequest
import com.boatit.boatsharing.features.signup.business.model.SaveBusinessAboutResponse
import com.boatit.boatsharing.features.signup.business.model.SaveBusinessInfoResponse
import com.boatit.boatsharing.features.signup.business.model.SaveBusinessLogoResponse
import com.boatit.boatsharing.features.signup.business.model.SaveBusinessProfileResponse
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

class BusinessSignupUseCasesTest {
    @Test
    fun saveBusinessUseCases_returnGatewayResults() =
        runBlocking {
            val saveProfileUseCase =
                SaveBusinessProfileUseCase {
                    Result.success(SaveBusinessProfileResponse(Status = 200, Message = "profile saved", obj = "ok"))
                }
            val saveInfoUseCase =
                SaveBusinessInfoUseCase {
                    Result.success(SaveBusinessInfoResponse(Status = 200, Message = "info saved", obj = "ok"))
                }
            val saveAboutUseCase =
                SaveBusinessAboutUseCase {
                    Result.success(SaveBusinessAboutResponse(Status = 200, Message = "about saved", obj = "ok"))
                }

            val profileResult =
                saveProfileUseCase(
                    BusinessProfileRequest("biz-1", "000", "Biz", "Owner", "A", "1990-01-01", "biz@example.com"),
                )
            val infoResult =
                saveInfoUseCase(
                    BusinessInfoRequest("biz-1", "Biz", "Dock", "A", "000", "2020", "9-5"),
                )
            val aboutResult =
                saveAboutUseCase(
                    SaveBusinessAboutRequest("biz-1", "desc", true),
                )

            assertTrue(profileResult.isSuccess)
            assertEquals("profile saved", profileResult.getOrNull()?.Message)
            assertTrue(infoResult.isSuccess)
            assertEquals("info saved", infoResult.getOrNull()?.Message)
            assertTrue(aboutResult.isSuccess)
            assertEquals("about saved", aboutResult.getOrNull()?.Message)
        }

    @Test
    fun logoAndFetchUseCases_returnGatewayResults() =
        runBlocking {
            val saveLogoUseCase =
                SaveBusinessLogoUseCase { _, _, _ ->
                    Result.success(SaveBusinessLogoResponse(Status = 200, Message = "logo saved", obj = "ok"))
                }
            val saveGalleryUseCase =
                SaveBusinessGalleryUseCase { _, _ ->
                    Result.success(SaveBusinessLogoResponse(Status = 200, Message = "gallery saved", obj = "ok"))
                }
            val fetchProfileUseCase =
                FetchBusinessProfileUseCase {
                    Result.success(
                        GetBusinessProfileResponse(
                            Status = 200,
                            Message = "ok",
                            obj =
                                BusinessProfileData(
                                    PhoneNumber = "000",
                                    FirstName = "Biz",
                                    LastName = "Owner",
                                    Address = "A",
                                    DateOfBirth = "1990-01-01",
                                    StripeEmail = "biz@example.com",
                                    UserId = "biz-1",
                                    ChangedOn = "2026-04-06",
                                    ChangedBy = "biz-1",
                                ),
                        ),
                    )
                }
            val fetchInfoUseCase =
                FetchBusinessInfoUseCase {
                    Result.success(
                        BusinessInfoResponse(
                            Status = 200,
                            Message = "ok",
                            obj =
                                BusinessInfoData(
                                    Name = "Biz",
                                    Type = "Dock",
                                    Address = "A",
                                    PhoneNumber = "000",
                                    YearOfEstablishment = 2020,
                                    Time = "9-5",
                                    Description = "desc",
                                    IsDock = true,
                                    LogoPath = null,
                                    UserId = "biz-1",
                                    ChangedOn = "2026-04-06",
                                    ChangedBy = "biz-1",
                                    ImagesPath = emptyList(),
                                ),
                        ),
                    )
                }

            val logoResult = saveLogoUseCase("biz-1", File("logo.png"), emptyList())
            val galleryResult = saveGalleryUseCase("biz-1", emptyList())
            val profileResult = fetchProfileUseCase()
            val infoResult = fetchInfoUseCase()

            assertTrue(logoResult.isSuccess)
            assertEquals("logo saved", logoResult.getOrNull()?.Message)
            assertTrue(galleryResult.isSuccess)
            assertEquals("gallery saved", galleryResult.getOrNull()?.Message)
            assertTrue(profileResult.isSuccess)
            assertEquals("biz-1", profileResult.getOrNull()?.obj?.UserId)
            assertTrue(infoResult.isSuccess)
            assertEquals("Biz", infoResult.getOrNull()?.obj?.Name)
        }
}
