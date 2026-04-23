package com.boatit.boatsharing.features.voyager.dashboard.viewmodel

import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.features.voyager.dashboard.domain.usecase.ConfirmBookedVoyageUseCase
import com.boatit.boatsharing.features.voyager.dashboard.model.ConfirmBookedVoyageResponse
import com.boatit.boatsharing.features.voyager.dashboard.model.ConfirmBookedVoyages
import com.boatit.boatsharing.testutils.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ConfirmBookedVoyageViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun submitConfirmation_success_setsSuccessState() =
        runTest {
            val useCase =
                ConfirmBookedVoyageUseCase {
                    Result.success(ConfirmBookedVoyageResponse(Status = 201, Message = "Confirmed", obj = "ok"))
                }
            val viewModel = ConfirmBookedVoyageViewModel(useCase)

            viewModel.submitConfirmation(ConfirmBookedVoyages(Id = "voy-123"))
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.confirmationState is NetworkResponse.Success)
        }

    @Test
    fun submitConfirmation_failure_setsErrorState() =
        runTest {
            val useCase =
                ConfirmBookedVoyageUseCase {
                    Result.failure(Exception("confirm failed"))
                }
            val viewModel = ConfirmBookedVoyageViewModel(useCase)

            viewModel.submitConfirmation(ConfirmBookedVoyages(Id = "voy-123"))
            advanceUntilIdle()

            when (val state = viewModel.uiState.value.confirmationState) {
                is NetworkResponse.Error -> assertEquals("confirm failed", state.message)
                else -> assertTrue("expected NetworkResponse.Error", false)
            }
        }
}
