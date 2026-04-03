package com.boatit.boatsharing.ui.signup.general.viewmodel


import com.boatit.boatsharing.data.remote.api.UserProfileApi
import com.boatit.boatsharing.data.remote.RemoteMapper
import com.boatit.boatsharing.ui.signup.general.model.VoyagerProfileRequest
import com.boatit.boatsharing.ui.signup.general.model.VoyagerProfileResponse

class VoyagerProfileRepository(private val api: UserProfileApi) {
    suspend fun saveVoyagerProfile(profile: VoyagerProfileRequest): Result<VoyagerProfileResponse> {
        return try {
            RemoteMapper.toResult(api.saveVoyagerProfile(profile))
        } catch (e: Exception) {
            Result.failure(Exception("Network Error: ${e.localizedMessage}", e))
        }
    }
}
