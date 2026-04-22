package com.boatit.boatsharing.features.captain.availabilitystatus.viewmodel

import com.boatit.boatsharing.testutils.MainDispatcherRule
import com.boatit.boatsharing.features.captain.availabilitystatus.model.CaptainAvailabilityRequest
import com.boatit.boatsharing.features.captain.availabilitystatus.model.CaptainAvailabilityResponse
import com.boatit.boatsharing.features.captain.domain.usecase.UpdateCaptainAvailabilityUseCase
import com.boatit.boatsharing.data.local.prefmanager.ICaptainStatusProvider
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
class UpdateStatusViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun toggleStatus_onlineSuccess_emitsToastAndNavigateDashboard() =
        runTest {
            val statusProvider = FakeCaptainStatusProvider(initialStatus = false)
            val useCase =
                UpdateCaptainAvailabilityUseCase { request: CaptainAvailabilityRequest ->
                    Result.success(CaptainAvailabilityResponse(Status = 200, Message = if (request.IsAvailable) "online" else "offline"))
                }
            val viewModel = UpdateStatusViewModel(useCase, statusProvider)

            viewModel.setOnlineStatus(true)

            val toastEffect =
                async {
                    viewModel.uiEffect.first { it is CaptainStatusUiEffect.ShowToast }
                }
            val navEffect =
                async {
                    viewModel.uiEffect.first { it is CaptainStatusUiEffect.NavigateToDashboard }
                }

            viewModel.toggleStatus("captain-1")
            advanceUntilIdle()

            assertFalse(viewModel.uiState.value.isLoading)
            assertEquals(true, viewModel.uiState.value.isOnline)
            assertEquals(true, statusProvider.isCaptainOnline())
            assertTrue(toastEffect.await() is CaptainStatusUiEffect.ShowToast)
            assertTrue(navEffect.await() is CaptainStatusUiEffect.NavigateToDashboard)
        }

    @Test
    fun toggleStatus_failure_setsErrorAndRevertsStatus() =
        runTest {
            val statusProvider = FakeCaptainStatusProvider(initialStatus = false)
            val useCase =
                UpdateCaptainAvailabilityUseCase {
                    Result.failure(Exception("status update failed"))
                }
            val viewModel = UpdateStatusViewModel(useCase, statusProvider)

            viewModel.setOnlineStatus(true)

            val toastEffect =
                async {
                    viewModel.uiEffect.first { it is CaptainStatusUiEffect.ShowToast }
                }

            viewModel.toggleStatus("captain-1")
            advanceUntilIdle()

            assertFalse(viewModel.uiState.value.isLoading)
            assertEquals(false, viewModel.uiState.value.isOnline)
            assertEquals("status update failed", viewModel.uiState.value.errorMessage)
            val effect = toastEffect.await() as CaptainStatusUiEffect.ShowToast
            assertTrue(effect.message.contains("status update failed"))
        }

    private class FakeCaptainStatusProvider(initialStatus: Boolean) : ICaptainStatusProvider {
        private var online: Boolean = initialStatus

        override fun setCaptainStatus(isOnline: Boolean) {
            online = isOnline
        }

        override fun isCaptainOnline(): Boolean {
            return online
        }
    }
}
