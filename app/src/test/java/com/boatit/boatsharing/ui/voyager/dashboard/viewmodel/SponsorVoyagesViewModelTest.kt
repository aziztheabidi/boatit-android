package com.boatit.boatsharing.features.voyager.dashboard.viewmodel

import com.boatit.boatsharing.testutils.MainDispatcherRule
import com.boatit.boatsharing.features.voyager.dashboard.domain.usecase.FetchSponsorPaymentsUseCase
import com.boatit.boatsharing.features.voyager.dashboard.model.SponsorPayments
import com.boatit.boatsharing.features.voyager.dashboard.model.SponsorVoyagerPayment
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
class SponsorVoyagesViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun voyages_success_updatesUiState() =
        runTest {
            val useCase =
                FetchSponsorPaymentsUseCase {
                    Result.success(
                        SponsorPayments(
                            Status = 200,
                            Message = "ok",
                            obj = listOf(samplePayment("voy-1")),
                        ),
                    )
                }
            val viewModel = SponsorVoyagesViewModel(useCase)

            viewModel.voyages()
            advanceUntilIdle()

            assertFalse(viewModel.uiState.value.isLoading)
            assertEquals(1, viewModel.uiState.value.voyages.size)
            assertEquals("voy-1", viewModel.uiState.value.voyages.first().id)
        }

    @Test
    fun voyages_failure_setsError_andEmitsToastEffect() =
        runTest {
            val useCase =
                FetchSponsorPaymentsUseCase {
                    Result.failure(Exception("sponsor failed"))
                }
            val viewModel = SponsorVoyagesViewModel(useCase)

            val effectDeferred =
                async {
                    viewModel.uiEffect.first { it is SponsorVoyagesUiEffect.ShowToast }
                }

            viewModel.voyages()
            advanceUntilIdle()

            val effect = effectDeferred.await() as SponsorVoyagesUiEffect.ShowToast
            assertEquals("sponsor failed", effect.message)
            assertTrue(viewModel.uiState.value.errorMessage?.contains("sponsor failed") == true)
        }

    private fun samplePayment(id: String): SponsorVoyagerPayment {
        return SponsorVoyagerPayment(
            Id = id,
            Name = "Trip",
            VoyagerName = "Voy",
            VoyagerPhoneNumber = "000",
            PickupDock = "A",
            PickupDockLatitude = 0.0,
            PickupDockLongitude = 0.0,
            DropOffDock = "B",
            DropOffDockLatitude = 1.0,
            DropOffDockLongitude = 1.0,
            AmountToPay = 42.0,
            NoOfVoyagers = 2,
            WaterStay = "Short",
            Duration = "2h",
            BookingDateTime = "2026-04-16",
            VoyageStatus = "Pending",
        )
    }
}
