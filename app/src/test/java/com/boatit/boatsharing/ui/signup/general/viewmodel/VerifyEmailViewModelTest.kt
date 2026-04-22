package com.boatit.boatsharing.features.signup.general.viewmodel

import com.boatit.boatsharing.testutils.MainDispatcherRule
import com.boatit.boatsharing.features.signup.general.domain.usecase.VerifySignupEmailUseCase
import com.boatit.boatsharing.features.signup.general.model.VerifyEmailResponse
import com.boatit.boatsharing.features.signup.general.repository.IVerifyEmailRepository
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
class VerifyEmailViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun submit_success_emitsNavigateEffect() =
        runTest {
            val useCase =
                VerifySignupEmailUseCase(
                    object : IVerifyEmailRepository {
                        override suspend fun verifyEmail(
                            email: String,
                            otp: String,
                        ): Result<VerifyEmailResponse> {
                            return Result.success(VerifyEmailResponse(Status = 200, Message = "Verified", obj = "token-123"))
                        }
                    },
                )
            val viewModel = VerifyEmailViewModel(useCase)

            val navigateEffect =
                async {
                    viewModel.uiEffect.first { it is VerifyEmailUiEffect.NavigateToCreatePassword }
                }

            viewModel.onEvent(VerifyEmailUiEvent.Submit("user@example.com", "12345"))
            advanceUntilIdle()

            assertFalse(viewModel.uiState.value.isLoading)
            val effect = navigateEffect.await()
            assertTrue(effect is VerifyEmailUiEffect.NavigateToCreatePassword)
            assertEquals("token-123", (effect as VerifyEmailUiEffect.NavigateToCreatePassword).token)
        }

    @Test
    fun submit_failure_setsErrorMessage() =
        runTest {
            val useCase =
                VerifySignupEmailUseCase(
                    object : IVerifyEmailRepository {
                        override suspend fun verifyEmail(
                            email: String,
                            otp: String,
                        ): Result<VerifyEmailResponse> {
                            return Result.failure(Exception("invalid otp"))
                        }
                    },
                )
            val viewModel = VerifyEmailViewModel(useCase)

            viewModel.onEvent(VerifyEmailUiEvent.Submit("user@example.com", "99999"))
            advanceUntilIdle()

            assertFalse(viewModel.uiState.value.isLoading)
            assertEquals("invalid otp", viewModel.uiState.value.errorMessage)
        }
}
