package com.boatit.boatsharing.features.voyager.dashboard.viewmodel

import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.testutils.MainDispatcherRule
import com.boatit.boatsharing.features.voyager.dashboard.domain.usecase.BookVoyageUseCase
import com.boatit.boatsharing.features.voyager.dashboard.model.BookVoyageRequest
import com.boatit.boatsharing.features.voyager.dashboard.model.BookVoyageResponse
import com.boatit.boatsharing.features.voyager.dashboard.model.BookVoyageUiEffect
import com.boatit.boatsharing.features.voyager.dashboard.model.BookVoyageUiEvent
import com.boatit.boatsharing.features.voyager.dashboard.model.Sponsor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BookVoyageViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun submitBookVoyage_success_updatesStateAndEmitsSuccessEffect() =
        runTest {
            val useCase =
                BookVoyageUseCase {
                    Result.success(BookVoyageResponse(Status = 201, Message = "Booked", obj = "voy-123"))
                }
            val viewModel = BookVoyageViewModel(useCase)
            val effectDeferred = async { viewModel.uiEffect.first() }

            viewModel.onEvent(BookVoyageUiEvent.SubmitBookVoyage(sampleRequest()))
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.bookRequest is NetworkResponse.Success)
            assertFalse(viewModel.uiState.value.isSubmitting)
            val effect = effectDeferred.await()
            assertTrue(effect is BookVoyageUiEffect.BookedSuccess)
            assertEquals("voy-123", (effect as BookVoyageUiEffect.BookedSuccess).voyageId)
        }

    @Test
    fun submitBookVoyage_failure_setsErrorStateAndEmitsErrorEffect() =
        runTest {
            val useCase =
                BookVoyageUseCase {
                    Result.failure(Exception("booking failed"))
                }
            val viewModel = BookVoyageViewModel(useCase)
            val effectDeferred = async { viewModel.uiEffect.first() }

            viewModel.onEvent(BookVoyageUiEvent.SubmitBookVoyage(sampleRequest()))
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.bookRequest is NetworkResponse.Error)
            assertTrue(viewModel.uiState.value.showErrorDialog)
            assertEquals("booking failed", viewModel.uiState.value.errorMessage)
            val effect = effectDeferred.await()
            assertTrue(effect is BookVoyageUiEffect.BookedError)
            assertEquals("booking failed", (effect as BookVoyageUiEffect.BookedError).message)
        }

    private fun sampleRequest(): BookVoyageRequest {
        return BookVoyageRequest(
            VoyagerUserId = "u1",
            Name = "Event",
            VoyageCategoryId = 1,
            PickupDockId = 10,
            DropOffDockId = 11,
            NoOfVoyagers = 2,
            IsImmediately = true,
            IsSplitPayment = false,
            BookingDate = "2026-04-05",
            StartTime = "10:00",
            IsStayOnWater = false,
            EndTime = "11:00",
            PerHourRate = 100.0,
            DurationInHours = 1.0,
            noOfSponsors = 1,
            EstimatedCost = 100.0,
            IndvidualAmount = 100.0,
            sponsors = listOf(Sponsor("u2", "Alice", 100.0, "Pending")),
        )
    }
}
