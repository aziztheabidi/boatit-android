package com.boatit.boatsharing.features.captain.dashboard.viewmodel

import com.boatit.boatsharing.testutils.MainDispatcherRule
import com.boatit.boatsharing.features.captain.dashboard.model.VoyageCompleteRequest
import com.boatit.boatsharing.features.captain.dashboard.model.VoyageCompleteResponse
import com.boatit.boatsharing.features.captain.domain.usecase.CompleteVoyageUseCase
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
class CompleteVoyageViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun submit_success_emitsToastAndNavigationEffects() =
        runTest {
            val useCase =
                CompleteVoyageUseCase {
                    Result.success(VoyageCompleteResponse(Status = 200, Message = "completed"))
                }
            val viewModel = CompleteVoyageViewModel(useCase)

            val toastEffect =
                async {
                    viewModel.uiEffect.first { it is CompleteVoyageUiEffect.ShowToast }
                }
            val navigationEffect =
                async {
                    viewModel.uiEffect.first { it is CompleteVoyageUiEffect.NavigateToFeedback }
                }

            viewModel.onEvent(CompleteVoyageUiEvent.Submit(VoyageCompleteRequest("voyage-1")))
            advanceUntilIdle()

            assertFalse(viewModel.uiState.value.isLoading)
            assertEquals(null, viewModel.uiState.value.errorMessage)
            assertTrue(toastEffect.await() is CompleteVoyageUiEffect.ShowToast)
            assertTrue(navigationEffect.await() is CompleteVoyageUiEffect.NavigateToFeedback)
        }

    @Test
    fun submit_failure_setsErrorAndEmitsToast() =
        runTest {
            val useCase =
                CompleteVoyageUseCase {
                    Result.failure(Exception("complete voyage failed"))
                }
            val viewModel = CompleteVoyageViewModel(useCase)

            val toastEffect =
                async {
                    viewModel.uiEffect.first { it is CompleteVoyageUiEffect.ShowToast }
                }

            viewModel.completeVoyage(VoyageCompleteRequest("voyage-1"))
            advanceUntilIdle()

            assertFalse(viewModel.uiState.value.isLoading)
            assertEquals("complete voyage failed", viewModel.uiState.value.errorMessage)
            val effect = toastEffect.await() as CompleteVoyageUiEffect.ShowToast
            assertTrue(effect.message.contains("complete voyage failed"))
        }
}
