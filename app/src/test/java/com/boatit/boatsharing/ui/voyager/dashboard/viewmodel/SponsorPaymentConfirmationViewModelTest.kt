package com.boatit.boatsharing.features.voyager.dashboard.viewmodel

import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.testutils.MainDispatcherRule
import com.boatit.boatsharing.features.voyager.dashboard.domain.usecase.ConfirmSponsorPaymentUseCase
import com.boatit.boatsharing.features.voyager.dashboard.model.PaymentConfirmationRequest
import com.boatit.boatsharing.features.voyager.dashboard.model.PaymentDetails
import com.boatit.boatsharing.features.voyager.dashboard.model.VoyagePaymentResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SponsorPaymentConfirmationViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun payment_success_setsSuccessState() =
        runTest {
            val useCase =
                ConfirmSponsorPaymentUseCase {
                    Result.success(
                        VoyagePaymentResponse(
                            Status = 200,
                            Message = "Sponsor payment confirmed",
                            obj = PaymentDetails(OTP = 4321, CaptainName = "Cap", BoatName = "B1", BoatModel = "M1"),
                        ),
                    )
                }
            val viewModel = SponsorPaymentConfirmationViewModel(useCase)

            viewModel.payment(sampleRequest())
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.networkState is NetworkResponse.Success)
        }

    @Test
    fun payment_failure_setsErrorState() =
        runTest {
            val useCase =
                ConfirmSponsorPaymentUseCase {
                    Result.failure(Exception("sponsor payment failed"))
                }
            val viewModel = SponsorPaymentConfirmationViewModel(useCase)

            viewModel.payment(sampleRequest())
            advanceUntilIdle()

            when (val state = viewModel.uiState.value.networkState) {
                is NetworkResponse.Error -> assertEquals("sponsor payment failed", state.message)
                else -> assertTrue("expected NetworkResponse.Error", false)
            }
        }

    private fun sampleRequest(): PaymentConfirmationRequest {
        return PaymentConfirmationRequest(
            Id = "voy-123",
            PaymentIntentId = "pi_abc",
            PaymentMethodId = "pm_abc",
        )
    }
}
