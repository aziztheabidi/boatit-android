package com.boatit.boatsharing.features.userroles.viewmodel

import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.features.userroles.domain.model.DeviceTokenUpdateDomainModel
import com.boatit.boatsharing.features.userroles.domain.usecase.UpdateDeviceTokenUseCase
import com.boatit.boatsharing.features.userroles.repository.IFCMTokenRepository
import com.boatit.boatsharing.testutils.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FCMTokenViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun updateFcmToken_success_setsSuccessState() =
        runTest {
            val repository =
                object : IFCMTokenRepository {
                    override suspend fun updateDeviceToken(
                        userId: String,
                        deviceToken: String,
                    ): Result<DeviceTokenUpdateDomainModel> =
                        Result.success(DeviceTokenUpdateDomainModel(status = 200, message = "updated"))
                }
            val useCase = UpdateDeviceTokenUseCase(repository)
            val viewModel = FCMTokenViewModel(useCase)

            viewModel.updateFcmToken("user-1", "token-1")
            advanceUntilIdle()

            assertTrue(viewModel.tokenUpdateState.value is NetworkResponse.Success)
        }

    @Test
    fun updateFcmToken_failure_setsErrorState() =
        runTest {
            val repository =
                object : IFCMTokenRepository {
                    override suspend fun updateDeviceToken(
                        userId: String,
                        deviceToken: String,
                    ): Result<DeviceTokenUpdateDomainModel> = Result.failure(Exception("token update failed"))
                }
            val useCase = UpdateDeviceTokenUseCase(repository)
            val viewModel = FCMTokenViewModel(useCase)

            viewModel.updateFcmToken("user-1", "token-1")
            advanceUntilIdle()

            val state = viewModel.tokenUpdateState.value
            assertTrue(state is NetworkResponse.Error)
            assertEquals("token update failed", (state as NetworkResponse.Error).message)
        }
}
