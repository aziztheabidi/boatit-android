package com.boatit.boatsharing.ui.userroles.repository

import com.boatit.boatsharing.data.remote.api.AccountApi
import com.boatit.boatsharing.data.remote.RemoteMapper
import com.boatit.boatsharing.ui.userroles.model.RoleRequest
import com.boatit.boatsharing.ui.userroles.model.RoleResponse

class RoleRepository(private val api: AccountApi) {
    suspend fun login(userid: String, role: String): Result<RoleResponse> {
        return try {
            RemoteMapper.toResult(api.updateRole(RoleRequest(userid, role)))
        } catch (e: Exception) {
            Result.failure(Exception("Error registering: ${e.localizedMessage}", e))
        }
    }
}
