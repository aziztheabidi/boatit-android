package com.boatit.boatsharing.features.captain.dashboard.viewmodel

import com.boatit.boatsharing.testutils.MainDispatcherRule
import com.boatit.boatsharing.features.captain.dashboard.model.VoyageStartRequest
import com.boatit.boatsharing.features.captain.dashboard.model.VoyageStartResponse
import com.boatit.boatsharing.features.captain.domain.usecase.StartVoyageUseCase
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
class StartVoyageViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun submit_success_emitsToastAndRefreshEffects() =
        runTest {
            val useCase =
                StartVoyageUseCase {
                    Result.success(VoyageStartResponse(Status = 200, Message = "Voyage Started."))
                }
            val viewModel = StartVoyageViewModel(useCase)

            val toastEffect =
                async {
                    viewModel.uiEffect.first { it is StartVoyageUiEffect.ShowToast }
                }
            val refreshEffect =
                async {
                    viewModel.uiEffect.first { it is StartVoyageUiEffect.RefreshActiveVoyages }
                }

            viewModel.onEvent(StartVoyageUiEvent.Submit(VoyageStartRequest("voyage-1", "12345")))
            advanceUntilIdle()

            assertFalse(viewModel.uiState.value.isLoading)
            assertEquals(null, viewModel.uiState.value.errorMessage)
            assertTrue(toastEffect.await() is StartVoyageUiEffect.ShowToast)
            assertTrue(refreshEffect.await() is StartVoyageUiEffect.RefreshActiveVoyages)
        }

    @Test
    fun submit_failure_setsErrorAndEmitsToast() =
        runTest {
            val useCase =
                StartVoyageUseCase {
                    Result.failure(Exception("unable to start"))
                }
            val viewModel = StartVoyageViewModel(useCase)

            val toastEffect =
                async {
                    viewModel.uiEffect.first { it is StartVoyageUiEffect.ShowToast }
                }

            viewModel.startVoyage(VoyageStartRequest("voyage-1", "12345"))
            advanceUntilIdle()

            assertFalse(viewModel.uiState.value.isLoading)
            assertEquals("unable to start", viewModel.uiState.value.errorMessage)
            val effect = toastEffect.await() as StartVoyageUiEffect.ShowToast
            assertTrue(effect.message.contains("unable to start"))
        }
}
