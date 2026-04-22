package com.boatit.boatsharing.features.captain.voyages.viewmodel

import com.boatit.boatsharing.testutils.MainDispatcherRule
import com.boatit.boatsharing.features.captain.domain.usecase.FetchCaptainCompletedVoyagesUseCase
import com.boatit.boatsharing.features.captain.voyages.repository.ICaptainVoyagesRepository
import com.boatit.boatsharing.features.voyager.dashboard.model.CaptainCompletedVoyage
import com.boatit.boatsharing.features.voyager.dashboard.model.CaptainCompletedVoyageResponse
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
class CaptainVoyagesViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun voyages_success_updatesUiState() =
        runTest {
            val useCase =
                FetchCaptainCompletedVoyagesUseCase(
                    object : ICaptainVoyagesRepository {
                        override suspend fun voyages(): Result<CaptainCompletedVoyageResponse> {
                            return Result.success(
                                CaptainCompletedVoyageResponse(
                                    Status = 200,
                                    Message = "ok",
                                    obj = listOf(sampleVoyage("voy-1")),
                                ),
                            )
                        }
                    },
                )
            val viewModel = CaptainVoyagesViewModel(useCase)

            viewModel.voyages()
            advanceUntilIdle()

            assertFalse(viewModel.uiState.value.isLoading)
            assertEquals(1, viewModel.uiState.value.voyages.size)
            assertEquals("voy-1", viewModel.uiState.value.voyages.first().id)
        }

    @Test
    fun voyages_failure_emitsToastEffect() =
        runTest {
            val useCase =
                FetchCaptainCompletedVoyagesUseCase(
                    object : ICaptainVoyagesRepository {
                        override suspend fun voyages(): Result<CaptainCompletedVoyageResponse> {
                            return Result.failure(Exception("captain voyages failed"))
                        }
                    },
                )
            val viewModel = CaptainVoyagesViewModel(useCase)

            val effectDeferred =
                async {
                    viewModel.uiEffect.first { it is CaptainVoyagesUiEffect.ShowToast }
                }

            viewModel.voyages()
            advanceUntilIdle()

            val effect = effectDeferred.await() as CaptainVoyagesUiEffect.ShowToast
            assertTrue(effect.message.contains("captain voyages failed"))
        }

    private fun sampleVoyage(id: String): CaptainCompletedVoyage {
        return CaptainCompletedVoyage(
            Id = id,
            Name = "Trip",
            VoyagerUserId = "voy-1",
            VoyagerName = "Voyager",
            VoyagerPhoneNumber = "000",
            Rating = 5.0,
            PickupDock = "A",
            PickupDockLatitude = 0.0,
            PickupDockLongitude = 0.0,
            DropOffDock = "B",
            DropOffDockLatitude = 1.0,
            DropOffDockLongitude = 1.0,
            NoOfVoyager = 2,
            AmountToPay = 12.0,
            WaterStay = "No",
            Duration = "1h",
            BookingDateTime = "2026-04-16",
        )
    }
}
