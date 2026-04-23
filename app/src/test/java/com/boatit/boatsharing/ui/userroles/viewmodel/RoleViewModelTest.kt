package com.boatit.boatsharing.features.userroles.viewmodel

import com.boatit.boatsharing.data.local.prefmanager.IRoleProvider
import com.boatit.boatsharing.data.local.prefmanager.ITokenProvider
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.features.userroles.domain.model.RoleAssignmentDomainModel
import com.boatit.boatsharing.features.userroles.domain.usecase.AssignUserRoleUseCase
import com.boatit.boatsharing.features.userroles.repository.IRoleRepository
import com.boatit.boatsharing.testutils.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RoleViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun selectRole_success_updatesStateAndSavesRoleAndTokens() =
        runTest {
            val roleProvider = FakeRoleProvider()
            val tokenProvider = FakeTokenProvider(accessToken = "existing")
            val repository =
                object : IRoleRepository {
                    override suspend fun assignRole(
                        userId: String,
                        role: String,
                        bearerToken: String?,
                    ): Result<RoleAssignmentDomainModel> =
                        Result.success(
                            RoleAssignmentDomainModel(
                                status = 200,
                                message = "ok",
                                accessToken = "new-access",
                                refreshToken = "new-refresh",
                            ),
                        )
                }
            val useCase = AssignUserRoleUseCase(repository)

            val viewModel = RoleViewModel(useCase, roleProvider, tokenProvider)

            viewModel.selectRole("user-1", "Captain")
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.roleState is NetworkResponse.Success)
            assertEquals("Captain", roleProvider.savedRole)
            assertEquals("new-access", tokenProvider.savedAccess)
            assertEquals("new-refresh", tokenProvider.savedRefresh)
            assertEquals(false, viewModel.uiState.value.isLoading)
        }

    @Test
    fun selectRole_failure_setsErrorStateAndMessage() =
        runTest {
            val roleProvider = FakeRoleProvider()
            val tokenProvider = FakeTokenProvider(accessToken = "existing")
            val repository =
                object : IRoleRepository {
                    override suspend fun assignRole(
                        userId: String,
                        role: String,
                        bearerToken: String?,
                    ): Result<RoleAssignmentDomainModel> = Result.failure(Exception("assign failed"))
                }
            val useCase = AssignUserRoleUseCase(repository)

            val viewModel = RoleViewModel(useCase, roleProvider, tokenProvider)

            viewModel.selectRole("user-1", "Captain")
            advanceUntilIdle()

            val state = viewModel.uiState.value.roleState
            assertTrue(state is NetworkResponse.Error)
            assertEquals("assign failed", (state as NetworkResponse.Error).message)
            assertEquals("assign failed", viewModel.uiState.value.errorMessage)
            assertEquals(false, viewModel.uiState.value.isLoading)
        }

    private class FakeRoleProvider : IRoleProvider {
        var savedRole: String? = null

        override fun getRole(): String? = savedRole

        override fun saveRole(role: String) {
            savedRole = role
        }

        override fun clearRole() {
            savedRole = null
        }
    }

    private class FakeTokenProvider(
        private var accessToken: String? = null,
        private var refreshToken: String? = null,
    ) : ITokenProvider {
        var savedAccess: String? = null
        var savedRefresh: String? = null

        override fun getAccessToken(): String? = accessToken

        override fun getRefreshToken(): String? = refreshToken

        override fun saveTokens(
            accessToken: String?,
            refreshToken: String?,
        ) {
            savedAccess = accessToken
            savedRefresh = refreshToken
            this.accessToken = accessToken
            this.refreshToken = refreshToken
        }

        override fun clearTokens() {
            accessToken = null
            refreshToken = null
        }

        override fun clearAll() {
            clearTokens()
        }
    }
}
