package com.boatit.boatsharing.features.userroles.domain.usecase

import com.boatit.boatsharing.features.userroles.domain.model.DeviceTokenUpdateDomainModel
import com.boatit.boatsharing.features.userroles.domain.model.RoleAssignmentDomainModel
import com.boatit.boatsharing.features.userroles.repository.IFCMTokenRepository
import com.boatit.boatsharing.features.userroles.repository.IRoleRepository

class AssignUserRoleUseCase(
    private val roleRepository: IRoleRepository,
) {
    suspend operator fun invoke(
        userId: String,
        role: String,
        token: String?,
    ): Result<RoleAssignmentDomainModel> = roleRepository.assignRole(userId, role, token)
}

class UpdateDeviceTokenUseCase(
    private val fcmTokenRepository: IFCMTokenRepository,
) {
    suspend operator fun invoke(
        userId: String,
        token: String,
    ): Result<DeviceTokenUpdateDomainModel> = fcmTokenRepository.updateDeviceToken(userId, token)
}
