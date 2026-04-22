package com.boatit.boatsharing.features.captain.domain.usecase

import com.boatit.boatsharing.features.captain.availabilitystatus.model.CaptainAvailabilityRequest
import com.boatit.boatsharing.features.captain.availabilitystatus.model.CaptainAvailabilityResponse
import com.boatit.boatsharing.features.captain.dashboard.model.AcceptVoyageRequest
import com.boatit.boatsharing.features.captain.dashboard.model.AcceptVoyageResponse
import com.boatit.boatsharing.features.captain.dashboard.model.CaptainActiveVoyagesObj
import com.boatit.boatsharing.features.captain.dashboard.model.CaptainActiveVoyagesResponse
import com.boatit.boatsharing.features.captain.dashboard.model.CaptainFeedbackRequest
import com.boatit.boatsharing.features.captain.dashboard.model.CaptainFeedbackResponse
import com.boatit.boatsharing.features.captain.dashboard.model.VoyageCompleteRequest
import com.boatit.boatsharing.features.captain.dashboard.model.VoyageCompleteResponse
import com.boatit.boatsharing.features.captain.dashboard.model.VoyageData
import com.boatit.boatsharing.features.captain.dashboard.model.VoyageStartRequest
import com.boatit.boatsharing.features.captain.dashboard.model.VoyageStartResponse
import com.boatit.boatsharing.features.captain.dashboard.repository.ICaptainActiveVoyagesRepository
import com.boatit.boatsharing.features.captain.voyages.repository.ICaptainVoyagesRepository
import com.boatit.boatsharing.features.voyager.dashboard.model.CaptainCompletedVoyage
import com.boatit.boatsharing.features.voyager.dashboard.model.CaptainCompletedVoyageResponse
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CaptainDashboardUseCasesTest {
    @Test
    fun acceptAndDeclineVoyageUseCases_returnGatewayResults() =
        runBlocking {
            val request =
                AcceptVoyageRequest(
                    Id = "voy-1",
                    CaptainUserId = "cap-1",
                    CaptainBookingLatitude = 24.86,
                    CaptainBookingLongitude = 67.01,
                )
            val acceptUseCase =
                AcceptVoyageUseCase {
                    Result.success(AcceptVoyageResponse(Status = 200, Message = "accepted"))
                }
            val declineUseCase =
                DeclineVoyageUseCase {
                    Result.success(AcceptVoyageResponse(Status = 200, Message = "declined"))
                }

            val acceptResult = acceptUseCase(request)
            val declineResult = declineUseCase(request)

            assertTrue(acceptResult.isSuccess)
            assertEquals("accepted", acceptResult.getOrNull()?.Message)
            assertTrue(declineResult.isSuccess)
            assertEquals("declined", declineResult.getOrNull()?.Message)
        }

    @Test
    fun startCompleteAndCancelVoyageUseCases_returnGatewayResults() =
        runBlocking {
            val startUseCase =
                StartVoyageUseCase {
                    Result.success(VoyageStartResponse(Status = 200, Message = "started"))
                }
            val completeUseCase =
                CompleteVoyageUseCase {
                    Result.success(VoyageCompleteResponse(Status = 200, Message = "completed"))
                }
            val cancelUseCase =
                CancelVoyageUseCase {
                    Result.success(VoyageCompleteResponse(Status = 200, Message = "cancelled"))
                }

            val startResult = startUseCase(VoyageStartRequest(Id = "voy-1", OTP = "1234"))
            val completeResult = completeUseCase(VoyageCompleteRequest(Id = "voy-1"))
            val cancelResult = cancelUseCase(VoyageCompleteRequest(Id = "voy-1"))

            assertTrue(startResult.isSuccess)
            assertEquals("started", startResult.getOrNull()?.Message)
            assertTrue(completeResult.isSuccess)
            assertEquals("completed", completeResult.getOrNull()?.Message)
            assertTrue(cancelResult.isSuccess)
            assertEquals("cancelled", cancelResult.getOrNull()?.Message)
        }

    @Test
    fun fetchCaptainActiveVoyagesUseCase_returnsGatewayData() =
        runBlocking {
            val expected =
                CaptainActiveVoyagesResponse(
                    Status = 200,
                    Message = "ok",
                    obj =
                        CaptainActiveVoyagesObj(
                            Pending = listOf(sampleActiveVoyage()),
                            Accepted = emptyList(),
                            Started = emptyList(),
                        ),
                )
            val useCase =
                FetchCaptainActiveVoyagesUseCase(
                    object : ICaptainActiveVoyagesRepository {
                        override suspend fun voyages(): Result<CaptainActiveVoyagesResponse> {
                            return Result.success(expected)
                        }
                    },
                )

            val result = useCase()

            assertTrue(result.isSuccess)
            assertEquals("ok", result.getOrNull()?.message)
            assertEquals(1, result.getOrNull()?.pending?.size)
        }

    @Test
    fun submitFeedbackAndUpdateAvailabilityUseCases_returnGatewayResults() =
        runBlocking {
            val feedbackUseCase =
                SubmitCaptainFeedbackUseCase {
                    Result.success(CaptainFeedbackResponse(Status = 201, Message = "saved", obj = "ok"))
                }
            val availabilityUseCase =
                UpdateCaptainAvailabilityUseCase {
                    Result.success(CaptainAvailabilityResponse(Status = 200, Message = "updated"))
                }

            val feedbackResult =
                feedbackUseCase(
                    CaptainFeedbackRequest(Id = "voy-1", Rating = 5, Review = "great"),
                )
            val availabilityResult =
                availabilityUseCase(
                    CaptainAvailabilityRequest(UserId = "cap-1", IsAvailable = true),
                )

            assertTrue(feedbackResult.isSuccess)
            assertEquals("saved", feedbackResult.getOrNull()?.Message)
            assertTrue(availabilityResult.isSuccess)
            assertEquals("updated", availabilityResult.getOrNull()?.Message)
        }

    @Test
    fun fetchCaptainCompletedVoyagesUseCase_returnsFailureFromGateway() =
        runBlocking {
            val useCase =
                FetchCaptainCompletedVoyagesUseCase(
                    object : ICaptainVoyagesRepository {
                        override suspend fun voyages(): Result<CaptainCompletedVoyageResponse> {
                            return Result.failure(Exception("captain completed voyages failed"))
                        }
                    },
                )

            val result = useCase()

            assertTrue(result.isFailure)
            assertEquals("captain completed voyages failed", result.exceptionOrNull()?.message)
        }

    private fun sampleActiveVoyage(): VoyageData {
        return VoyageData(
            Id = "voy-1",
            Name = "Trip",
            VoyagerUserId = "voyager-1",
            VoyagerName = "Voyager",
            VoyagerPhoneNumber = "000000000",
            PickupDock = "A",
            PickupDockLatitude = 0.0,
            PickupDockLongitude = 0.0,
            DropOffDock = "B",
            DropOffDockLatitude = 0.0,
            DropOffDockLongitude = 0.0,
            NoOfVoyager = 2,
            BookingDateTime = "2026-04-05",
            AmountToPay = 10.0,
            WaterStay = "No",
            Duration = "1h",
        )
    }

    @Suppress("unused")
    private fun sampleCompletedVoyage(): CaptainCompletedVoyage {
        return CaptainCompletedVoyage(
            Id = "voy-1",
            Name = "Trip",
            VoyagerUserId = "voyager-1",
            VoyagerName = "Voyager",
            VoyagerPhoneNumber = "000000000",
            Rating = 5.0,
            PickupDock = "A",
            PickupDockLatitude = 0.0,
            PickupDockLongitude = 0.0,
            DropOffDock = "B",
            DropOffDockLatitude = 0.0,
            DropOffDockLongitude = 0.0,
            NoOfVoyager = 2,
            AmountToPay = 10.0,
            WaterStay = "No",
            Duration = "1h",
            BookingDateTime = "2026-04-05",
        )
    }

    @Suppress("unused")
    private fun sampleCompletedVoyagesResponse(): CaptainCompletedVoyageResponse {
        return CaptainCompletedVoyageResponse(
            Status = 200,
            Message = "ok",
            obj = listOf(sampleCompletedVoyage()),
        )
    }
}
