package com.boatit.boatsharing.features.userroles.repository

import com.boatit.boatsharing.data.network.di.ApiConstants
import com.boatit.boatsharing.data.network.retrofit.ApiExecutor
import com.boatit.boatsharing.features.userroles.data.dto.RoleRequestDto
import com.boatit.boatsharing.features.userroles.data.dto.RoleResponseDto
import com.boatit.boatsharing.features.userroles.domain.model.RoleAssignmentDomainModel
import com.boatit.boatsharing.features.userroles.domain.model.toDomainModel

class RoleRepository(
    private val apiExecutor: ApiExecutor,
) : IRoleRepository {
    override suspend fun assignRole(
        userId: String,
        role: String,
        bearerToken: String?,
    ): Result<RoleAssignmentDomainModel> =
        apiExecutor.post<RoleResponseDto>(
            endpoint = ApiConstants.Endpoints.ROLE,
            body = RoleRequestDto(UserId = userId, Role = role),
            successCode = 200,
            authorization = bearerToken?.let { "Bearer $it" },
        ).map { it.toDomainModel() }
}
