package com.boatit.boatsharing.features.voyager.dashboard.domain.usecase

import com.boatit.boatsharing.features.voyager.dashboard.model.BookVoyageRequest
import com.boatit.boatsharing.features.voyager.dashboard.model.BookVoyageResponse
import com.boatit.boatsharing.features.voyager.dashboard.model.CalculateFair
import com.boatit.boatsharing.features.voyager.dashboard.model.CancelBookedVoyageResponse
import com.boatit.boatsharing.features.voyager.dashboard.model.CancelBookedVoyages
import com.boatit.boatsharing.features.voyager.dashboard.model.ConfirmBookedVoyages
import com.boatit.boatsharing.features.voyager.dashboard.model.DockRate
import com.boatit.boatsharing.features.voyager.dashboard.model.FindBoatRequest
import com.boatit.boatsharing.features.voyager.dashboard.model.FindBoatResponse
import com.boatit.boatsharing.features.voyager.dashboard.model.Sponsor
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class VoyageBookingUseCasesTest {
    @Test
    fun bookVoyageUseCase_returnsSuccessFromGateway() =
        runBlocking {
            val request = sampleBookRequest()
            val expected = BookVoyageResponse(Status = 201, Message = "Booked", obj = "voy-1")
            val useCase = BookVoyageUseCase { Result.success(expected) }

            val result = useCase(request)

            assertTrue(result.isSuccess)
            assertEquals("voy-1", result.getOrNull()?.obj)
        }

    @Test
    fun confirmBookedVoyageUseCase_returnsFailureFromGateway() =
        runBlocking {
            val useCase = ConfirmBookedVoyageUseCase { Result.failure(Exception("confirm failed")) }

            val result = useCase(ConfirmBookedVoyages(Id = "voy-2"))

            assertTrue(result.isFailure)
            assertEquals("confirm failed", result.exceptionOrNull()?.message)
        }

    @Test
    fun cancelBookedVoyageUseCase_returnsSuccessFromGateway() =
        runBlocking {
            val expected = CancelBookedVoyageResponse(Status = 201, Message = "Canceled", obj = "voy-2")
            val useCase = CancelBookedVoyageUseCase { Result.success(expected) }

            val result = useCase(CancelBookedVoyages(Id = "voy-2", Reason = "changed plans"))

            assertTrue(result.isSuccess)
            assertEquals("Canceled", result.getOrNull()?.Message)
        }

    @Test
    fun findBoatUseCase_returnsSuccessFromGateway() =
        runBlocking {
            val request =
                FindBoatRequest(
                    VoyagerUserId = "u-1",
                    Name = "Voyage",
                    VoyageCategoryId = 1,
                    PickupDockId = 2,
                    DropOffDockId = 3,
                    NoOfVoyagers = 4,
                    EstimatedCost = 25.0,
                    IsImmediately = true,
                    IsSplitPayment = false,
                    BookingDate = "2026-04-05",
                )
            val expected = FindBoatResponse(Status = 201, Message = "ok", obj = "voy-10")
            val useCase = FindBoatUseCase { Result.success(expected) }

            val result = useCase(request)

            assertTrue(result.isSuccess)
            assertEquals("voy-10", result.getOrNull()?.obj)
        }

    @Test
    fun calculateVoyageFareUseCase_returnsSuccessFromGateway() =
        runBlocking {
            val expected =
                CalculateFair(
                    Status = 201,
                    Message = "ok",
                    obj = DockRate(PerHourRate = 20.0, TotalFair = 40.0),
                )
            val useCase = CalculateVoyageFareUseCase { _, _, _, _, _ -> Result.success(expected) }

            val result =
                useCase(
                    durationInHours = "2",
                    fromDockId = 1,
                    toDockId = 2,
                    voyageCategoryId = 3,
                    noOfVoyagers = 4,
                )

            assertTrue(result.isSuccess)
            assertEquals(40.0, result.getOrNull()?.obj?.TotalFair)
        }

    private fun sampleBookRequest(): BookVoyageRequest {
        return BookVoyageRequest(
            VoyagerUserId = "u-1",
            Name = "Event",
            VoyageCategoryId = 1,
            PickupDockId = 2,
            DropOffDockId = 3,
            NoOfVoyagers = 4,
            IsImmediately = true,
            IsSplitPayment = false,
            BookingDate = "2026-04-05",
            StartTime = "10:00",
            IsStayOnWater = false,
            EndTime = "11:00",
            PerHourRate = 20.0,
            DurationInHours = 1.0,
            noOfSponsors = 0,
            EstimatedCost = 20.0,
            IndvidualAmount = 20.0,
            sponsors = listOf(Sponsor("u-1", "name", 20.0, "Pending")),
        )
    }
}
