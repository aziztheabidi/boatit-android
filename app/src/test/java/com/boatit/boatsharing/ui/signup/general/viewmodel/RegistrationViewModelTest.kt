package com.boatit.boatsharing.features.signup.general.viewmodel

import com.boatit.boatsharing.testutils.MainDispatcherRule
import com.boatit.boatsharing.features.signup.general.domain.usecase.RegisterUserUseCase
import com.boatit.boatsharing.features.signup.general.model.RegistrationResponse
import com.boatit.boatsharing.features.signup.general.repository.IRegistrationRepository
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
class RegistrationViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun submit_success_emitsNavigationEffectAndStopsLoading() =
        runTest {
            val useCase =
                RegisterUserUseCase(
                    object : IRegistrationRepository {
                        override suspend fun tempRegister(
                            username: String,
                            phoneNumber: String,
                            email: String,
                        ): Result<RegistrationResponse> {
                            return Result.success(RegistrationResponse(Status = 200, Message = "Registered", obj = email))
                        }
                    },
                )
            val viewModel = RegistrationViewModel(useCase)

            viewModel.onEvent(RegistrationUiEvent.EmailChanged("user@example.com"))
            viewModel.onEvent(RegistrationUiEvent.NameChanged("Test User"))
            viewModel.onEvent(RegistrationUiEvent.PhoneChanged("123456789"))

            val navigateEffect =
                async {
                    viewModel.uiEffect.first { it is RegistrationUiEffect.NavigateToNext }
                }

            viewModel.onEvent(RegistrationUiEvent.Submit)
            advanceUntilIdle()

            assertFalse(viewModel.uiState.value.isLoading)
            assertEquals(null, viewModel.uiState.value.errorMessage)
            val effect = navigateEffect.await()
            assertTrue(effect is RegistrationUiEffect.NavigateToNext)
            assertEquals("user@example.com", (effect as RegistrationUiEffect.NavigateToNext).email)
        }

    @Test
    fun submit_failure_setsErrorMessageAndStopsLoading() =
        runTest {
            val useCase =
                RegisterUserUseCase(
                    object : IRegistrationRepository {
                        override suspend fun tempRegister(
                            username: String,
                            phoneNumber: String,
                            email: String,
                        ): Result<RegistrationResponse> {
                            return Result.failure(Exception("registration failed"))
                        }
                    },
                )
            val viewModel = RegistrationViewModel(useCase)

            viewModel.onEvent(RegistrationUiEvent.EmailChanged("user@example.com"))
            viewModel.onEvent(RegistrationUiEvent.NameChanged("Test User"))
            viewModel.onEvent(RegistrationUiEvent.PhoneChanged("123456789"))

            viewModel.onEvent(RegistrationUiEvent.Submit)
            advanceUntilIdle()

            assertFalse(viewModel.uiState.value.isLoading)
            assertEquals("registration failed", viewModel.uiState.value.errorMessage)
        }
}
