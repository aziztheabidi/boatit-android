package com.boatit.boatsharing.features.captain.dashboard.viewmodel

import com.boatit.boatsharing.testutils.MainDispatcherRule
import com.boatit.boatsharing.features.captain.dashboard.model.CaptainFeedbackRequest
import com.boatit.boatsharing.features.captain.domain.usecase.SubmitCaptainFeedbackUseCase
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
class CaptainFeedbackViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun submit_success_emitsToastAndNavigationEffects() =
        runTest {
            val useCase =
                SubmitCaptainFeedbackUseCase {
                    Result.success(
                        com.boatit.boatsharing.features.captain.dashboard.model.CaptainFeedbackResponse(
                            Status = 200,
                            Message = "ok",
                        ),
                    )
                }
            val viewModel = CaptainFeedbackViewModel(useCase)

            val toastEffect =
                async {
                    viewModel.uiEffect.first { it is CaptainFeedbackUiEffect.ShowToast }
                }
            val navigationEffect =
                async {
                    viewModel.uiEffect.first { it is CaptainFeedbackUiEffect.NavigateToDashboard }
                }

            viewModel.onEvent(CaptainFeedbackUiEvent.Submit(CaptainFeedbackRequest("voyage-1", 5, "great")))
            advanceUntilIdle()

            assertFalse(viewModel.uiState.value.isLoading)
            assertEquals(null, viewModel.uiState.value.errorMessage)
            assertTrue(toastEffect.await() is CaptainFeedbackUiEffect.ShowToast)
            assertTrue(navigationEffect.await() is CaptainFeedbackUiEffect.NavigateToDashboard)
        }

    @Test
    fun submit_failure_setsErrorAndEmitsToast() =
        runTest {
            val useCase =
                SubmitCaptainFeedbackUseCase {
                    Result.failure(Exception("feedback failed"))
                }
            val viewModel = CaptainFeedbackViewModel(useCase)

            val toastEffect =
                async {
                    viewModel.uiEffect.first { it is CaptainFeedbackUiEffect.ShowToast }
                }

            viewModel.captainFeedbackFunc(CaptainFeedbackRequest("voyage-1", 2, "bad"))
            advanceUntilIdle()

            assertFalse(viewModel.uiState.value.isLoading)
            assertEquals("feedback failed", viewModel.uiState.value.errorMessage)
            val effect = toastEffect.await() as CaptainFeedbackUiEffect.ShowToast
            assertTrue(effect.message.contains("feedback failed"))
        }
}
