package com.boatit.boatsharing.features.userroles.repository

import com.boatit.boatsharing.data.network.di.ApiConstants
import com.boatit.boatsharing.data.network.di.executePostRequest
import com.boatit.boatsharing.data.network.di.networkFailure
import com.boatit.boatsharing.domain.core.ExceptionMapper
import com.boatit.boatsharing.features.userroles.data.dto.RoleRequestDto
import com.boatit.boatsharing.features.userroles.data.dto.RoleResponseDto
import com.boatit.boatsharing.features.userroles.domain.model.RoleAssignmentDomainModel
import com.boatit.boatsharing.features.userroles.domain.model.toDomainModel
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.http.HttpStatusCode

class RoleRepository(
    private val httpClient: HttpClient,
) : IRoleRepository {
    override suspend fun assignRole(
        userId: String,
        role: String,
        bearerToken: String?,
    ): Result<RoleAssignmentDomainModel> {
        return executePostRequest<RoleRequestDto, RoleResponseDto>(
            httpClient = httpClient,
            url = "${ApiConstants.BASE_URL}${ApiConstants.Endpoints.ROLE}",
            requestBody = RoleRequestDto(UserId = userId, Role = role),
            successStatus = HttpStatusCode.OK,
            requestConfig = {
                bearerToken?.takeIf { it.isNotBlank() }?.let { token ->
                    header("Authorization", "Bearer $token")
                }
            },
            onApiError = { _, status -> ExceptionMapper.mapHttpException(status.value, status.description) },
            onException = { e -> networkFailure("Error assigning user role", e) },
        ).map { it.toDomainModel() }
    }
}
