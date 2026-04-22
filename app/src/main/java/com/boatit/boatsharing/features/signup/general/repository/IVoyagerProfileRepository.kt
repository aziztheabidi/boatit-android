package com.boatit.boatsharing.features.signup.general.repository

import com.boatit.boatsharing.features.signup.general.model.VoyagerProfileRequest
import com.boatit.boatsharing.features.signup.general.model.VoyagerProfileResponse

interface IVoyagerProfileRepository {
    suspend fun saveVoyagerProfile(profile: VoyagerProfileRequest): Result<VoyagerProfileResponse>
}
