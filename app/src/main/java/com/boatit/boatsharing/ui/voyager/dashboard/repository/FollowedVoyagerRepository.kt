package com.boatit.boatsharing.ui.voyager.dashboard.repository

import com.boatit.boatsharing.data.remote.RemoteMapper
import com.boatit.boatsharing.data.remote.api.VoyageApi
import com.boatit.boatsharing.ui.voyager.dashboard.model.FollowedVoyagersResponse
import com.boatit.boatsharing.utils.AppConstants

class FollowedVoyagerRepository(private val api: VoyageApi) {
    suspend fun FollowedVoyagerRepoFunc(): Result<FollowedVoyagersResponse> {
        return try {
            val userId = AppConstants.USER_ID ?: return Result.failure(Exception("User id missing"))
            RemoteMapper.toResult(api.getFollowedVoyagers(userId))
        } catch (e: Exception) {
            Result.failure(Exception("Network Error: ${e.localizedMessage}", e))
        }
    }
}
