package com.boatit.boatsharing.features.userroles.domain.usecase

import com.boatit.boatsharing.features.userroles.domain.model.DeviceTokenUpdateDomainModel
import com.boatit.boatsharing.features.userroles.domain.model.RoleAssignmentDomainModel
import com.boatit.boatsharing.features.userroles.repository.IFCMTokenRepository
import com.boatit.boatsharing.features.userroles.repository.IRoleRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class UserRoleUseCasesTest {
    @Test
    fun assignAndDeviceTokenUseCases_returnGatewayResults() =
        runBlocking {
            val roleRepository =
                object : IRoleRepository {
                    override suspend fun assignRole(
                        userId: String,
                        role: String,
                        bearerToken: String?,
                    ): Result<RoleAssignmentDomainModel> =
                        Result.success(
                            RoleAssignmentDomainModel(
                                status = 200,
                                message = "role assigned",
                                accessToken = "a",
                                refreshToken = "r",
                            ),
                        )
                }
            val tokenRepository =
                object : IFCMTokenRepository {
                    override suspend fun updateDeviceToken(
                        userId: String,
                        deviceToken: String,
                    ): Result<DeviceTokenUpdateDomainModel> =
                        Result.success(DeviceTokenUpdateDomainModel(status = 200, message = "token updated"))
                }

            val assignRoleUseCase = AssignUserRoleUseCase(roleRepository)
            val updateTokenUseCase = UpdateDeviceTokenUseCase(tokenRepository)

            val roleResult = assignRoleUseCase("u-1", "voyager", "access")
            val tokenResult = updateTokenUseCase("u-1", "fcm-token")

            assertTrue(roleResult.isSuccess)
            assertEquals("role assigned", roleResult.getOrNull()?.message)
            assertTrue(tokenResult.isSuccess)
            assertEquals("token updated", tokenResult.getOrNull()?.message)
        }
}
