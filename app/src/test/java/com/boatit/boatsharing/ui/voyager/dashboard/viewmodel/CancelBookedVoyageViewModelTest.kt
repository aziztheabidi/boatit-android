package com.boatit.boatsharing.features.voyager.dashboard.viewmodel

import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.features.voyager.dashboard.domain.usecase.CancelBookedVoyageUseCase
import com.boatit.boatsharing.features.voyager.dashboard.model.CancelBookedVoyageResponse
import com.boatit.boatsharing.features.voyager.dashboard.model.CancelBookedVoyages
import com.boatit.boatsharing.testutils.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CancelBookedVoyageViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun fetchNearbyPlaces_success_resetsBackToLoadingState() =
        runTest {
            val useCase =
                CancelBookedVoyageUseCase {
                    Result.success(CancelBookedVoyageResponse(Status = 201, Message = "Canceled", obj = "ok"))
                }
            val viewModel = CancelBookedVoyageViewModel(useCase)

            viewModel.fetchNearbyPlaces(CancelBookedVoyages(Id = "voy-123", Reason = "changed plans"))
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.nearbyPlaces is NetworkResponse.Loading)
        }

    @Test
    fun fetchNearbyPlaces_failure_setsErrorState() =
        runTest {
            val useCase =
                CancelBookedVoyageUseCase {
                    Result.failure(Exception("cancel failed"))
                }
            val viewModel = CancelBookedVoyageViewModel(useCase)

            viewModel.fetchNearbyPlaces(CancelBookedVoyages(Id = "voy-123", Reason = "changed plans"))
            advanceUntilIdle()

            when (val state = viewModel.uiState.value.nearbyPlaces) {
                is NetworkResponse.Error -> assertEquals("cancel failed", state.message)
                else -> assertTrue("expected NetworkResponse.Error", false)
            }
        }
}
