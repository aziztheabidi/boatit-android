package com.boatit.boatsharing.features.userroles.repository

import com.boatit.boatsharing.features.userroles.domain.model.RoleAssignmentDomainModel

interface IRoleRepository {
    suspend fun assignRole(
        userId: String,
        role: String,
        bearerToken: String?,
    ): Result<RoleAssignmentDomainModel>
}
