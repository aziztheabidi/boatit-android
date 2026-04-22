package com.boatit.boatsharing.features.captain.dashboard.viewmodel

import com.boatit.boatsharing.testutils.MainDispatcherRule
import com.boatit.boatsharing.features.captain.dashboard.model.AcceptVoyageRequest
import com.boatit.boatsharing.features.captain.dashboard.model.AcceptVoyageResponse
import com.boatit.boatsharing.features.captain.domain.usecase.AcceptVoyageUseCase
import com.boatit.boatsharing.features.captain.domain.usecase.DeclineVoyageUseCase
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
class AcceptRequestViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private fun sampleRequest(): AcceptVoyageRequest {
        return AcceptVoyageRequest(
            Id = "voy-1",
            CaptainUserId = "capt-1",
            CaptainBookingLatitude = 1.0,
            CaptainBookingLongitude = 2.0,
        )
    }

    @Test
    fun accept_success_emitsAcceptedEffect() =
        runTest {
            val acceptUseCase =
                AcceptVoyageUseCase {
                    Result.success(AcceptVoyageResponse(Status = 200, Message = "accepted"))
                }
            val declineUseCase =
                DeclineVoyageUseCase {
                    Result.success(AcceptVoyageResponse(Status = 200, Message = "declined"))
                }
            val viewModel = AcceptRequestViewModel(acceptUseCase, declineUseCase)

            val effectDeferred =
                async {
                    viewModel.uiEffect.first { it is AcceptRequestUiEffect.Accepted }
                }

            viewModel.accept(sampleRequest())
            advanceUntilIdle()

            assertFalse(viewModel.uiState.value.isLoading)
            assertEquals(null, viewModel.uiState.value.errorMessage)
            val effect = effectDeferred.await() as AcceptRequestUiEffect.Accepted
            assertEquals("voy-1", effect.voyageId)
            assertTrue(effect.message.contains("accepted"))
        }

    @Test
    fun decline_failure_emitsToastAndSetsError() =
        runTest {
            val acceptUseCase =
                AcceptVoyageUseCase {
                    Result.success(AcceptVoyageResponse(Status = 200, Message = "accepted"))
                }
            val declineUseCase =
                DeclineVoyageUseCase {
                    Result.failure(Exception("decline failed"))
                }
            val viewModel = AcceptRequestViewModel(acceptUseCase, declineUseCase)

            val effectDeferred =
                async {
                    viewModel.uiEffect.first { it is AcceptRequestUiEffect.ShowToast }
                }

            viewModel.decline(sampleRequest())
            advanceUntilIdle()

            assertFalse(viewModel.uiState.value.isLoading)
            assertEquals("decline failed", viewModel.uiState.value.errorMessage)
            val effect = effectDeferred.await() as AcceptRequestUiEffect.ShowToast
            assertTrue(effect.message.contains("decline failed"))
        }
}
