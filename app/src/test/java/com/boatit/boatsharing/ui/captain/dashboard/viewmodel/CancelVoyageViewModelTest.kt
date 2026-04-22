package com.boatit.boatsharing.features.captain.dashboard.viewmodel

import com.boatit.boatsharing.testutils.MainDispatcherRule
import com.boatit.boatsharing.features.captain.dashboard.model.VoyageCompleteRequest
import com.boatit.boatsharing.features.captain.dashboard.model.VoyageCompleteResponse
import com.boatit.boatsharing.features.captain.domain.usecase.CancelVoyageUseCase
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
class CancelVoyageViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun submit_success_emitsToastAndRefreshEffects() =
        runTest {
            val useCase =
                CancelVoyageUseCase {
                    Result.success(VoyageCompleteResponse(Status = 200, Message = "Voyage cancelled."))
                }
            val viewModel = CancelVoyageViewModel(useCase)

            val toastEffect =
                async {
                    viewModel.uiEffect.first { it is CancelVoyageUiEffect.ShowToast }
                }
            val refreshEffect =
                async {
                    viewModel.uiEffect.first { it is CancelVoyageUiEffect.RefreshActiveVoyages }
                }

            viewModel.onEvent(CancelVoyageUiEvent.Submit(VoyageCompleteRequest("voyage-1")))
            advanceUntilIdle()

            assertFalse(viewModel.uiState.value.isLoading)
            assertEquals(null, viewModel.uiState.value.errorMessage)
            assertTrue(toastEffect.await() is CancelVoyageUiEffect.ShowToast)
            assertTrue(refreshEffect.await() is CancelVoyageUiEffect.RefreshActiveVoyages)
        }

    @Test
    fun submit_failure_setsErrorAndEmitsToast() =
        runTest {
            val useCase =
                CancelVoyageUseCase {
                    Result.failure(Exception("unable to cancel"))
                }
            val viewModel = CancelVoyageViewModel(useCase)

            val toastEffect =
                async {
                    viewModel.uiEffect.first { it is CancelVoyageUiEffect.ShowToast }
                }

            viewModel.cancelVoyage(VoyageCompleteRequest("voyage-1"))
            advanceUntilIdle()

            assertFalse(viewModel.uiState.value.isLoading)
            assertEquals("unable to cancel", viewModel.uiState.value.errorMessage)
            val effect = toastEffect.await() as CancelVoyageUiEffect.ShowToast
            assertTrue(effect.message.contains("unable to cancel"))
        }
}
