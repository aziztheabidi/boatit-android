package com.boatit.boatsharing.features.captain.dashboard.viewmodel

import com.boatit.boatsharing.testutils.MainDispatcherRule
import com.boatit.boatsharing.features.captain.dashboard.model.CaptainActiveVoyagesObj
import com.boatit.boatsharing.features.captain.dashboard.model.CaptainActiveVoyagesResponse
import com.boatit.boatsharing.features.captain.dashboard.model.VoyageData
import com.boatit.boatsharing.features.captain.dashboard.repository.ICaptainActiveVoyagesRepository
import com.boatit.boatsharing.features.captain.domain.usecase.FetchCaptainActiveVoyagesUseCase
import com.boatit.boatsharing.features.captain.voyages.model.CaptainCurrentVoyagesUiEffect
import com.boatit.boatsharing.features.captain.voyages.model.CaptainCurrentVoyagesUiEvent
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
class CaptainActiveVoyagesViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun initialize_success_populatesTabs() =
        runTest {
            val useCase =
                FetchCaptainActiveVoyagesUseCase(
                    object : ICaptainActiveVoyagesRepository {
                        override suspend fun voyages(): Result<CaptainActiveVoyagesResponse> {
                            return Result.success(
                                CaptainActiveVoyagesResponse(
                                    Status = 200,
                                    Message = "ok",
                                    obj =
                                        CaptainActiveVoyagesObj(
                                            Pending = listOf(sampleVoyage("p-1")),
                                            Accepted = listOf(sampleVoyage("a-1")),
                                            Started = listOf(sampleVoyage("s-1")),
                                        ),
                                ),
                            )
                        }
                    },
                )
            val viewModel = CaptainActiveVoyagesViewModel(useCase)

            viewModel.onEvent(CaptainCurrentVoyagesUiEvent.Initialize)
            advanceUntilIdle()

            assertFalse(viewModel.uiState.value.isLoading)
            assertEquals(1, viewModel.uiState.value.pending.size)
            assertEquals(1, viewModel.uiState.value.accepted.size)
            assertEquals(1, viewModel.uiState.value.started.size)
        }

    @Test
    fun initialize_failure_emitsToast() =
        runTest {
            val useCase =
                FetchCaptainActiveVoyagesUseCase(
                    object : ICaptainActiveVoyagesRepository {
                        override suspend fun voyages(): Result<CaptainActiveVoyagesResponse> {
                            return Result.failure(Exception("active voyages failed"))
                        }
                    },
                )
            val viewModel = CaptainActiveVoyagesViewModel(useCase)

            val effect =
                async {
                    viewModel.uiEffect.first { it is CaptainCurrentVoyagesUiEffect.ShowToast }
                }

            viewModel.onEvent(CaptainCurrentVoyagesUiEvent.Initialize)
            advanceUntilIdle()

            val uiEffect = effect.await() as CaptainCurrentVoyagesUiEffect.ShowToast
            assertTrue(uiEffect.message.contains("active voyages failed"))
            assertEquals("active voyages failed", viewModel.uiState.value.errorMessage)
        }

    private fun sampleVoyage(id: String): VoyageData {
        return VoyageData(
            Id = id,
            Name = "Trip",
            VoyagerUserId = "voy-1",
            VoyagerName = "Voyager",
            VoyagerPhoneNumber = "000",
            PickupDock = "A",
            PickupDockLatitude = 0.0,
            PickupDockLongitude = 0.0,
            DropOffDock = "B",
            DropOffDockLatitude = 1.0,
            DropOffDockLongitude = 1.0,
            NoOfVoyager = 2,
            BookingDateTime = "2026-04-16",
            AmountToPay = 25.0,
            WaterStay = "No",
            Duration = "1h",
        )
    }
}
